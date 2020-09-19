package uk.co.foundationsedge.infrastructure;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;
import uk.co.foundationsedge.domain.author.Author;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.book.BookRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Repository
public class RedisBookRepository implements BookRepository {

    private final JedisPool jedisPool;
    private final Comparator<Book> byTitle = Comparator.comparing(Book::getTitle);


    public RedisBookRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Book byId(String bookId) {
        final JReJSON client = new JReJSON(jedisPool);
        try {
            var book = client.get(bookId, Book.class, Path.ROOT_PATH);
            book.setId(bookId); // Need to add ID to book object stored in Redis
            return book;
        } catch (Exception e) {
            return new Book();
        }
    }

    @Override
    public SortedSet<Book> byKeywords(Set<String> keywords) {

        var keywordSets = keywords.stream().map(String::toLowerCase)
            .map(this::retrieve)
            .collect(toList());

        var intersection = keywordSets.stream()
            .skip(1)
            .collect(() -> new HashSet<>(keywordSets.get(0)), Set::retainAll, Set::retainAll);

        return intersection.stream()
            .map(this::byId)
            .collect(Collectors.toCollection(() -> new TreeSet<>(byTitle)));
    }

    private Set<String> retrieve(String keyword) {
        return jedisPool.getResource().smembers(format("keyword:%s", keyword));
    }
}
