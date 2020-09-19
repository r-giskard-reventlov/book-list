package uk.co.foundationsedge.domain.list;

import java.util.List;
import java.util.UUID;

public interface BookListRepository {
    void save(String user, BookList bookList);
    List<BookList> byUser(String user);
    BookList byId(String user, UUID id);}
