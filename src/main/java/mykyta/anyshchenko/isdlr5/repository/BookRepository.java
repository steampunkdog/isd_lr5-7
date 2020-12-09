package mykyta.anyshchenko.isdlr5.repository;

import mykyta.anyshchenko.isdlr5.model.Book;

import java.util.Collection;

public interface BookRepository {

    Collection<Book> getAll();

    Book getById(String id);

    String save(Book book);

    String update(Book book);

    String deleteById(String id);

}
