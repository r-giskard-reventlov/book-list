package uk.co.foundationsedge.interfaces.app.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.list.BookList;

import java.io.IOException;
import java.lang.reflect.Field;

import static java.lang.String.format;


public class BookListJsonSerialiser extends StdSerializer<BookList> {

    public BookListJsonSerialiser() {
        this(null);
    }

    public BookListJsonSerialiser(Class<BookList> t) {
        super(t);
    }

    @Override
    public void serialize(
        BookList value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        try {
            Field idField = value.getClass().getDeclaredField("id");
            idField.setAccessible(true);

            Field nameField = value.getClass().getDeclaredField("name");
            nameField.setAccessible(true);

            jgen.writeStartObject();
            jgen.writeObjectField("id", idField.get(value));
            jgen.writeStringField("name", (String) nameField.get(value));

            jgen.writeFieldName("books");
            jgen.writeStartArray();
            for (Book book : value.getBooks()) {
                jgen.writeObject(book);
            }
            jgen.writeEndArray();
            jgen.writeEndObject();

        } catch (Exception e) {
            throw new RuntimeException(format("Failed to serialise book list [%s]", value), e);
        }
    }
}

