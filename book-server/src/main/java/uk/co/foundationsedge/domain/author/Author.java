package uk.co.foundationsedge.domain.author;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.co.foundationsedge.interfaces.app.serialisers.AuthorJsonSerialiser;

@JsonSerialize(using = AuthorJsonSerialiser.class)
public class Author {

    private String id;
    private final String name;
    private final String title;
    private final String dob;
    private final String dod;
    private final String bio;

    public Author() {
        this("", "", "", "", "");
    }

    public Author(String name, String title, String dob, String dod, String bio) {
        this.name = name;
        this.title = title;
        this.dob = dob;
        this.dod = dod;
        this.bio = bio;
    }

    public void setId(String id) {
        this.id = id;
    }
}
