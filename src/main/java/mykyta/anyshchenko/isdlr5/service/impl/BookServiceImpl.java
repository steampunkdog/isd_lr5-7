package mykyta.anyshchenko.isdlr5.service.impl;

import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;
import mykyta.anyshchenko.isdlr5.service.BookService;

import java.util.Collection;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Collection<String> deleteByFirstCharInName(char firstCharacter) {
        return getAll().stream()
                .filter(book -> book.getName().charAt(0) == firstCharacter)
                .map(book -> deleteById(book.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> softDeleteByIds(Collection<String> ids) {
        if(ids == null){
            throw new IllegalArgumentException("Collection of ids cannot be null");
        }
        return ids
                .stream()
                .peek(id -> {
                    if(id == null) {
                        throw new IllegalArgumentException("Id can`t be null");
                    }
                })
                .map(this::getById)
                .peek(book -> book.setDeleted(true))
                .map(this::update)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Book> getAll() {
        return bookRepository.getAll();
    }

    @Override
    public Book getById(String id) {
        return bookRepository.getById(id);
    }

    @Override
    public String save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public String update(Book book) {
        return bookRepository.update(book);
    }

    @Override
    public String deleteById(String id) {
        return bookRepository.deleteById(id);
    }
}
