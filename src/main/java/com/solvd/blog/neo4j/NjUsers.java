package com.solvd.blog.neo4j;

import com.solvd.blog.mapper.impl.BooleanMapper;
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
public class NjUsers implements Users {

    private final Driver driver;
    private final UserMapper userMapper;
    private final BooleanMapper booleanMapper;

    @Override
    public List<User> iterate() {
        try (Session session = driver.session()) {
            return session.run("MATCH (user:User) RETURN user")
                    .list(this.userMapper::toEntity);
        }
    }

    @Override
    public User user(final Long id) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "MATCH (user:User) WHERE ID(user)=$id RETURN user",
                    Map.of("id", id)
            );
            return this.userMapper.toEntity(session.run(query).single());
        }
    }

    @Override
    public User add(final User user) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "CREATE (user:User {name:$name, email:$email}) "
                            + "RETURN user",
                    Map.of("name", user.name(),
                            "email", user.email()
                    )
            );
            return this.userMapper.toEntity(session.run(query).single());
        }
    }

    @Override
    public User update(final User user) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "MATCH (user:User) WHERE ID(user)=$id SET user.name=$name "
                            + "RETURN user",
                    Map.of("id", user.id(),
                            "name", user.name()
                    )
            );
            return this.userMapper.toEntity(session.run(query).single());
        }
    }

    @Override
    public Boolean exists(final String email) {
        try (Session session = driver.session()) {
            Query query = new Query(
                    "MATCH (user:User{email:$email}) "
                            + "RETURN COUNT(user) > 0 as exists",
                    Map.of("email", email)
            );
            return this.booleanMapper.toEntity(session.run(query).single());
        }
    }

}
