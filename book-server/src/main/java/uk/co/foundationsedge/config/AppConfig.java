package uk.co.foundationsedge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.foundationsedge.domain.author.Author;
import uk.co.foundationsedge.domain.author.AuthorRepository;
import uk.co.foundationsedge.domain.book.Book;
import uk.co.foundationsedge.domain.list.BookList;
import uk.co.foundationsedge.interfaces.app.serialisers.AuthorJsonSerialiser;
import uk.co.foundationsedge.interfaces.app.serialisers.BookJsonSerialiser;
import uk.co.foundationsedge.interfaces.app.serialisers.BookListJsonSerialiser;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {


    private final AuthorRepository authorRepository;

    public AppConfig(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(Book.class, new BookJsonSerialiser(authorRepository));
        builder.serializerByType(BookList.class, new BookListJsonSerialiser());
        builder.serializerByType(Author.class, new AuthorJsonSerialiser());
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

}
