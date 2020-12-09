package mykyta.anyshchenko.isdlr5.service;

import mykyta.anyshchenko.isdlr5.model.Book;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface BookService {

    @Transactional
    Collection<String> deleteByFirstCharInName(char firstCharacter);

    @Transactional
    Collection<String> softDeleteByIds(Collection<String> ids);

    Collection<Book> getAll();

    Book getById(String id);

    @Transactional
    String save(Book book);

    @Transactional
    String update(Book book);

    @Transactional
    String deleteById(String id);

}
