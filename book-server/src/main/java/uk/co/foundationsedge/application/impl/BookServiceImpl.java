package uk.co.foundationsedge.application.impl;

import org.springframework.stereotype.Service;
import uk.co.foundationsedge.application.BookService;
import uk.co.foundationsedge.application.StopwordService;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.book.BookRepository;
import uk.co.foundationsedge.domain.list.BookList;
import uk.co.foundationsedge.domain.list.BookListRepository;
import uk.co.foundationsedge.interfaces.app.api.dto.BookDto;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookListRepository bookListRepository;
    private final StopwordService stopwordService;

    public BookServiceImpl(BookRepository bookRepository,
                           BookListRepository bookListRepository,
                           StopwordService stopwordService) {
        this.bookRepository = bookRepository;
        this.bookListRepository = bookListRepository;
        this.stopwordService = stopwordService;
    }

    @Override
    public Book bookById(String bookId) {
        return bookRepository.byId(bookId);
    }

    @Override
    public SortedSet<Book> booksByKeywords(Set<String> keywords) {
        return bookRepository.byKeywords(stopwordService.removeFrom(keywords));
    }

    @Override
    public BookList createList(String user, String name) {
        final BookList bookList = new BookList(name, bookRepository::byId);
        bookListRepository.save(user, bookList);
        return bookList;
    }

    @Override
    public List<BookList> listsByUser(String user) {
        return bookListRepository.byUser(user).stream()
            // TODO: Doesn't feel good to do this, but Gson is responsible for object creation!!
            .peek(bl -> bl.setBookFn(bookRepository::byId))
            .collect(toList());
    }

    @Override
    public BookList listsById(String user, UUID id) {
        var bookList = bookListRepository.byId(user, id);

        // TODO: Doesn't feel good to do this, but Gson is responsible for object creation!!
        bookList.setBookFn(bookRepository::byId);

        return bookList;
    }

    @Override
    public void addBookToList(String user, UUID id, BookDto book) {
        final var bookList = bookListRepository.byId(user, id);
        bookList.add(book.getId());
        bookListRepository.save(user, bookList);
    }
}
