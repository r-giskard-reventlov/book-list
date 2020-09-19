package uk.co.foundationsedge.domain.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.co.foundationsedge.interfaces.app.serialisers.BookJsonSerialiser;

import java.util.List;

/**
 * Need to add Id to book so serialisation is simpler
 */
@JsonSerialize(using = BookJsonSerialiser.class)
public class Book {
    private String id;

    private final String title;
    private final String description;
    private final List<String> subjects;
    private final List<String> authors;
    private final List<String> covers;

    public Book() {
        this("", "", List.of(), List.of(), List.of());
    }

    public Book(String title, String description, List<String> subjects, List<String> authors, List<String> covers) {
        this.title = title;
        this.description = description;
        this.subjects = subjects;
        this.authors = authors;
        this.covers = covers;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

}
