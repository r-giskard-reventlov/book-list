package uk.co.foundationsedge.domain.book;

import uk.co.foundationsedge.domain.author.Author;

import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;

public interface BookRepository {
    Book byId(String bookId);
    SortedSet<Book> byKeywords(Set<String> keywords);
}
