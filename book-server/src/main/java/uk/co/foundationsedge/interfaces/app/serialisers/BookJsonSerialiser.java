package uk.co.foundationsedge.interfaces.app.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import uk.co.foundationsedge.domain.author.Author;
import uk.co.foundationsedge.domain.author.AuthorRepository;
import uk.co.foundationsedge.domain.book.Book;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;

public class BookJsonSerialiser extends StdSerializer<Book> {

    private final Function<String, Author> authorFn;

    public BookJsonSerialiser(AuthorRepository authorRepository) {
        super(Book.class);
        this.authorFn = authorRepository::byId;
    }

    @Override
    public void serialize(
        Book value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        try {
            Field idField = value.getClass().getDeclaredField("id");
            idField.setAccessible(true);

            Field titleField = value.getClass().getDeclaredField("title");
            titleField.setAccessible(true);

            Field descriptionField = value.getClass().getDeclaredField("description");
            descriptionField.setAccessible(true);

            Field subjectsField = value.getClass().getDeclaredField("subjects");
            subjectsField.setAccessible(true);

            Field authorsField = value.getClass().getDeclaredField("authors");
            authorsField.setAccessible(true);

            Field coversField = value.getClass().getDeclaredField("covers");
            coversField.setAccessible(true);

            jgen.writeStartObject();
            jgen.writeStringField("id", (String) idField.get(value));
            jgen.writeStringField("title", (String) titleField.get(value));
            jgen.writeStringField("description", (String) descriptionField.get(value));

            jgen.writeFieldName("subjects");
            jgen.writeStartArray();
            var subjects = (List<String>) subjectsField.get(value);
            if(subjects != null) {
                for (String subject : subjects) {
                    jgen.writeString(subject);
                }
            }
            jgen.writeEndArray();

            jgen.writeFieldName("authors");
            jgen.writeStartArray();
            var authors = (List<String>) authorsField.get(value);
            if(authors != null) {
                for (String author : authors) {
                    jgen.writeObject(authorFn.apply(author));
                }
            }
            jgen.writeEndArray();

            jgen.writeFieldName("covers");
            jgen.writeStartArray();
            var covers = (List<String>) coversField.get(value);
            if(covers != null) {
                for (String cover : covers) {
                    jgen.writeString(cover);
                }
            }
            jgen.writeEndArray();

            jgen.writeEndObject();
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to serialise book [%s]", value), e);
        }
    }
}
