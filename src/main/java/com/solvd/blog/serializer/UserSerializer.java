package com.solvd.blog.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.solvd.blog.neo4j.NeoUser;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<NeoUser> {

    @Override
    public void serialize(NeoUser value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.id());
        gen.writeStringField("name", value.name());
        gen.writeStringField("email", value.email());
        gen.writeFieldName("posts");
        gen.writeObject(value.posts());
        gen.writeEndObject();
    }

}