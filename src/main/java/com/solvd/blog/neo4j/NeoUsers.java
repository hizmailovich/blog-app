package com.solvd.blog.neo4j;

import com.solvd.blog.mapper.impl.UserMapper;
import com.solvd.blog.model.User;
import com.solvd.blog.model.Users;
import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public final class NeoUsers implements Users {

    private final Driver driver;
    private final UserMapper mapper;

    @Override
    public List<User> iterate() {
        try (Session session = driver.session()) {
            return session.run("MATCH (user:User) RETURN user")
                    .list(this.mapper::toEntity);
        }
    }

    @Override
    public User user(Long id) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "MATCH (user:User) WHERE user.id=$id RETURN user",
                    Map.of("id", id)
            );
            return this.mapper.toEntity(session.run(query).single());
        }
    }

    @Override
    public User add(User user) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "CREATE (user:User {id:$id, name:$name, email:$email}) RETURN user",
                    Map.of("id", user.id(),
                            "name", user.name(),
                            "email", user.email()
                    )
            );
            return this.mapper.toEntity(session.run(query).single());
        }
    }

    @Override
    public User update(User user) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "MATCH (user:User {id:$id}) SET user.name=$name RETURN user",
                    Map.of("id", user.id(),
                            "name", user.name()
                    )
            );
            return this.mapper.toEntity(session.run(query).single());
        }
    }

}