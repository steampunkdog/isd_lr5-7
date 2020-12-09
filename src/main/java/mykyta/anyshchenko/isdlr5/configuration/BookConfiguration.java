package mykyta.anyshchenko.isdlr5.configuration;

import mykyta.anyshchenko.isdlr5.controller.BookConsoleController;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;
import mykyta.anyshchenko.isdlr5.repository.impl.BookRepositoryImpl;
import mykyta.anyshchenko.isdlr5.service.BookService;
import mykyta.anyshchenko.isdlr5.service.impl.BookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class BookConfiguration {

    @Bean
    public BookRepository bookRepository(JdbcTemplate jdbcTemplate){
        return new BookRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public BookService bookService(BookRepository bookRepository){
        return new BookServiceImpl(bookRepository);
    }

    @Bean
    public BookConsoleController bookConsoleController (BookService bookService){
        return new BookConsoleController(bookService);
    }
}
