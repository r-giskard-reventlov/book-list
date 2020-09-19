package uk.co.foundationsedge.infrastructure;

import com.redislabs.modules.rejson.JReJSON;
import com.redislabs.modules.rejson.Path;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;
import uk.co.foundationsedge.domain.author.Author;
import uk.co.foundationsedge.domain.author.AuthorRepository;

@Repository
public class RedisAuthorRepository implements AuthorRepository {

    private final JedisPool jedisPool;

    public RedisAuthorRepository(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Author byId(String id) {
        final JReJSON client = new JReJSON(jedisPool);
        try {
            var author = client.get(id, Author.class, Path.ROOT_PATH);
            author.setId(id);
            return author;
        } catch (Exception e) {
            return new Author();
        }
    }
}
