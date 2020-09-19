package uk.co.foundationsedge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${REDIS_HOST:localhost}")
    private String redisHost;

    @Value("${REDIS_PORT:6379}")
    private Integer redisPort;

    @Value("${REDIS_PASSWORD:}")
    private String redisPassword;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxWaitMillis(8);
        config.setMaxTotal(3000);
        return config;
    }

    @Bean
    public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig) {
        return switch(redisPassword) {
            case "" -> new JedisPool(jedisPoolConfig, redisHost, redisPort, 3000);
            default -> new JedisPool(jedisPoolConfig, redisHost, redisPort, 3000, redisPassword);
        };
    }

//    @Bean
//    public JedisConnectionFactory redisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }
//
//    @Bean
//    public StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
//        final StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//        return template;
//    }

//    @Bean
//    public ObjectMapper objectMapper(ObjectMapper objectMapper) {
//        SimpleModule module = new SimpleModule("book-module");
//        module.addSerializer(Book.class, new BookSerialiser());
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
}
