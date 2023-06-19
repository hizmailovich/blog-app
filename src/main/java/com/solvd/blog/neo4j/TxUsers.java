package com.solvd.blog.neo4j;

import com.solvd.blog.model.User;
import com.solvd.blog.model.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TxUsers implements Users {

    private final Users users;

    public TxUsers(@Qualifier("neoUsers") final Users users) {
        this.users = users;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> iterate() {
        return this.users.iterate();
    }

    @Override
    @Transactional(readOnly = true)
    public User user(Long id) {
        return this.users.user(id);
    }

    @Override
    @Transactional
    public User add(User user) {
        return this.users.add(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return this.users.update(user);
    }

}
