package uk.co.foundationsedge.application;

import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.list.BookList;
import uk.co.foundationsedge.interfaces.app.api.dto.BookDto;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

public interface BookService {
    Book bookById(String bookId);
    SortedSet<Book> booksByKeywords(Set<String> keywords);
    BookList createList(String user, String name);
    List<BookList> listsByUser(String user);
    BookList listsById(String user, UUID id);
    void addBookToList(String user, UUID id, BookDto book);
}
