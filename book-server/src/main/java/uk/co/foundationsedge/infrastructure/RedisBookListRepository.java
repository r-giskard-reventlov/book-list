package uk.co.foundationsedge.infrastructure;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;
import uk.co.foundationsedge.domain.list.BookList;
import uk.co.foundationsedge.domain.list.BookListRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Repository
public class RedisBookListRepository implements BookListRepository {

    private final JedisPool jedisPool;

    public RedisBookListRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void save(String user, BookList bookList) {
        // TODO: store as list to preserve order
        final JReJSON client = new JReJSON(jedisPool);
        jedisPool.getResource().sadd(format("%s/lists", user), bookList.getId().toString());
        client.set(format("%s/lists/%s", "justin", bookList.getId()), bookList);
    }

    @Override
    public List<BookList> byUser(String user) {
        final Set<String> lists = jedisPool.getResource().smembers(format("%s/lists", user));

        // TODO: store and retrieve as list
        return lists.stream()
            .map(bookId -> byId(user, UUID.fromString(bookId)))
            .collect(toList());
    }

    @Override
    public BookList byId(String user, UUID id) {
        final JReJSON client = new JReJSON(jedisPool);
        return client.get(format("%s/lists/%s", user, id.toString()), BookList.class, Path.ROOT_PATH);
    }

}
