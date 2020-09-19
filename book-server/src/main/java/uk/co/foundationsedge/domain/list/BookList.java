package uk.co.foundationsedge.domain.list;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.interfaces.app.serialisers.BookListJsonSerialiser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonSerialize(using = BookListJsonSerialiser.class)
public class BookList {
    private final UUID id;
    private final String name;
    private final List<String> booksIds;
    private final LocalDateTime created;
    private transient Function<String, Book> bookFn;

    public BookList(String name, Function<String, Book> bookFn) {
        this(name, List.of(), bookFn);
    }

    public BookList(String name, List<String> booksIds, Function<String, Book> bookFn) {
        this(name, UUID.randomUUID(), booksIds, LocalDateTime.now(), bookFn);
    }

    public BookList(String name, UUID id, List<String> booksIds, LocalDateTime created, Function<String, Book> bookFn) {
        this.id = id;
        this.name = name;
        this.booksIds = booksIds;
        this.created = created;
        this.bookFn = bookFn;
    }


    public UUID getId() {
        return id;
    }

    public void add(String bookId) {
        booksIds.add(bookId);
    }

    public List<Book> getBooks() {
        if(booksIds.isEmpty()) return List.of();
        return booksIds.stream()
            .map(bookFn)
            .collect(Collectors.toList());
    }

    public void setBookFn(Function<String, Book> bookFn) {
        this.bookFn = bookFn;
    }
}
