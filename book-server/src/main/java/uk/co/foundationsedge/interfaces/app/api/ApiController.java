package uk.co.foundationsedge.interfaces.app.api;

import org.springframework.web.bind.annotation.*;
import uk.co.foundationsedge.application.BookService;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.list.BookList;
import uk.co.foundationsedge.interfaces.app.api.dto.BookDto;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import static java.lang.String.format;

@RestController
public class ApiController {

    private final BookService bookService;

    public ApiController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/api/{user:justin}/works/{id}")
    public Book book(@PathVariable("user") String user, @PathVariable("id") String id) {
        return bookService.bookById(format("/works/%s", id));
    }

    @GetMapping("/api/works")
    public SortedSet<Book> book(@RequestParam("keywords") Set<String> keywords) {
        return bookService.booksByKeywords(keywords);
    }


    @PostMapping("/api/{user:justin}/lists")
    public BookList createList(@PathVariable("user") String user, @RequestParam("name") String name) {
        return bookService.createList(user, name);
    }

    @GetMapping("/api/{user:justin}/lists")
    public List<BookList> listsForUser(@PathVariable("user") String user) {
        final List<BookList> bookLists = bookService.listsByUser(user);
        return bookLists;
    }

    @GetMapping("/api/{user:justin}/lists/{id}")
    public BookList listById(@PathVariable("user") String user,
                             @PathVariable("id") UUID id) {
        return bookService.listsById(user, id);
    }

    @PostMapping("/api/{user:justin}/lists/{id}")
    public void addBookToList(@PathVariable("user") String user,
                              @PathVariable("id") UUID id,
                              @RequestBody BookDto book) {
        bookService.addBookToList(user, id, book);
    }

}
