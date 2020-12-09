package mykyta.anyshchenko.isdlr5;

import mykyta.anyshchenko.isdlr5.configuration.BookConfiguration;
import mykyta.anyshchenko.isdlr5.controller.BookConsoleController;
import mykyta.anyshchenko.isdlr5.exception.ObjectNotFoundException;
import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;


@Configuration
@Import(BookConfiguration.class)
public class BookServiceTestConfiguration {

    private static final Map<String, Book> BOOKS_MAP = new ConcurrentHashMap<>();

    @Bean
    public BookRepository bookRepository() {

        BookRepository bookRepository = Mockito.mock(BookRepository.class);


        Mockito.when(bookRepository.getAll())
                .then(invocation ->
                        BOOKS_MAP.values()
                                .stream()
                                .filter(book -> !book.isDeleted())
                                .collect(Collectors.toList())
                );

        Mockito.when(bookRepository.deleteById(any()))
                .then(invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    checkThatBookExists(id);
                    BOOKS_MAP.remove(id);
                    return id;
                });

        Mockito.when(bookRepository.getById(any()))
                .then(invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    checkThatBookExists(id);
                    return BOOKS_MAP.get(id);
                });

        Mockito.when(bookRepository.update(any()))
                .then(invocationOnMock -> {
                    Book book = invocationOnMock.getArgument(0);
                    checkThatBookExists(book.getId());
                    BOOKS_MAP.put(book.getId(), book);
                    return book.getId();
                });

        Mockito.when(bookRepository.save(any()))
                .then(invocationOnMock -> {
                    Book book = invocationOnMock.getArgument(0);
                    if (BOOKS_MAP.containsKey(book.getId())) {
                        bookRepository.update(book);
                    }
                    BOOKS_MAP.put(book.getId(), book);
                    return book.getId();
                });


        return bookRepository;
    }

    @Bean
    public BookConsoleController bookConsoleController() {
        return Mockito.mock(BookConsoleController.class);
    }

    private void checkThatBookExists(String id) {
        if (!BOOKS_MAP.containsKey(id)) {
            throw new ObjectNotFoundException("Object with id=" + id + " not found");
        }
    }
}