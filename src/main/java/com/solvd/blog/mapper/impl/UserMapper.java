package com.solvd.blog.mapper.impl;

import com.solvd.blog.mapper.Mapper;
import com.solvd.blog.model.User;
import com.solvd.blog.neo4j.NjUser;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Node;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper implements Mapper<User> {

    @Override
    public User toEntity(final Record record) {
        Node node = record.get("user").asNode();
        return new NjUser(
                node.id(),
                node.get("name").asString(),
                node.get("email").asString(),
                new ArrayList<>()
        );
    }

}
