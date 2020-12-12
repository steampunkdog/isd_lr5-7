package mykyta.anyshchenko.isdlr5.service.impl;

import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;
import mykyta.anyshchenko.isdlr5.service.BookService;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private static Logger LOGGER;

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        LOGGER = LogManager.getLogger(BookServiceImpl.class);
        this.bookRepository = bookRepository;
    }

    @Override
    public Collection<String> deleteByFirstCharInName(char firstCharacter) {
        LOGGER.trace("deleteByFirstCharInName('" + firstCharacter + "')");

        List<String> ids = getAll().stream()
                .filter(book -> book.getName().charAt(0) == firstCharacter)
                .map(book -> deleteById(book.getId()))
                .collect(Collectors.toList());

        LOGGER.debug("deleteByFirstCharInName('" + firstCharacter + "') returned " + ids);
        return ids;
    }

    @Override
    public Collection<String> softDeleteByIds(Collection<String> ids) {
        LOGGER.trace("softDeleteByIds('" + ids + "')");

        if(ids == null){
            throw new IllegalArgumentException("Collection of ids cannot be null");
        }

        List<String> deletedIds = ids.stream()
                .peek(id -> {
                    if (id == null) {
                        throw new IllegalArgumentException("Id can`t be null");
                    }
                })
                .map(this::getById)
                .peek(book -> book.setDeleted(true))
                .map(this::update)
                .collect(Collectors.toList());

        LOGGER.debug("softDeleteByIds('" + ids + "') returned " + deletedIds);
        return deletedIds;
    }

    @Override
    public Collection<Book> getAll() {
        LOGGER.trace("getAll()");
        return bookRepository.getAll();
    }

    @Override
    public Book getById(String id) {
        LOGGER.trace("getById('" + id + "')");
        return bookRepository.getById(id);
    }

    @Override
    public String save(Book book) {
        LOGGER.trace("save('" + book + "')");
        return bookRepository.save(book);
    }

    @Override
    public String update(Book book) {
        LOGGER.trace("update('" + book + "')");
        return bookRepository.update(book);
    }

    @Override
    public String deleteById(String id) {
        LOGGER.trace("deleteById('" + id + "')");
        return bookRepository.deleteById(id);
    }
}
