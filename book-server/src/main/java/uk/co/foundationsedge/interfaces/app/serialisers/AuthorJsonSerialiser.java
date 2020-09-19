package uk.co.foundationsedge.interfaces.app.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import uk.co.foundationsedge.domain.author.Author;

import java.io.IOException;
import java.lang.reflect.Field;

import static java.lang.String.format;

public class AuthorJsonSerialiser extends StdSerializer<Author> {

    public AuthorJsonSerialiser() {
        super(Author.class);
    }

    @Override
    public void serialize(Author author, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            Field nameField = author.getClass().getDeclaredField("name");
            nameField.setAccessible(true);

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", (String) nameField.get(author));
            jsonGenerator.writeEndObject();
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to serialise author [%s]", author), e);
        }
    }
}
