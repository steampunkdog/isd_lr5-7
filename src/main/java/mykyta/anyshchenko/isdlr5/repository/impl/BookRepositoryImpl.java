package mykyta.anyshchenko.isdlr5.repository.impl;

import mykyta.anyshchenko.isdlr5.exception.ObjectNotFoundException;
import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

public class BookRepositoryImpl implements BookRepository {

    public static final String GET_ALL = "SELECT * FROM book WHERE deleted = false";
    public static final String GET_BY_ID = "SELECT * FROM book WHERE id = '%s'";
    public static final String INSERT = "INSERT INTO book (id, name, author, deleted) VALUES ('%s', '%s', '%s' , %b);";
    public static final String DELETE_BY_ID = "DELETE FROM book WHERE id = '%s'";
    public static final String UPDATE_BY_ID = "UPDATE book " +
                                                "SET id = '%s', name = '%s', author = '%s', deleted = %b " +
                                                "WHERE id = '%s'";

    JdbcTemplate jdbcTemplate;
    RowMapper<Book> bookRowMapper = (resultSet, i) -> {
        Book book = new Book();
        book.setId(resultSet.getString("id"));
        book.setName(resultSet.getString("name"));
        book.setAuthor(resultSet.getString("author"));
        book.setDeleted(resultSet.getBoolean("deleted"));
        return book;
    };

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Book> getAll() {
        return jdbcTemplate.query(GET_ALL, bookRowMapper);
    }

    @Override
    public Book getById(String id) {
        try{
            return jdbcTemplate.queryForObject(String.format(GET_BY_ID, id), bookRowMapper);
        } catch (EmptyResultDataAccessException e){
            throw new ObjectNotFoundException("Object with id '" + id + "' not found");
        }
    }

    @Override
    public String save(Book book) {
        if(getById(book.getId()) != null){
            return update(book);
        }

        jdbcTemplate.execute(String.format(
                INSERT,
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.isDeleted()
            ));

        return book.getId();
    }

    @Override
    public String update(Book book) {
        getById(book.getId());

        jdbcTemplate.execute(
                    String.format(
                        UPDATE_BY_ID,
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.isDeleted(),
                        book.getId()
                    ));

        return book.getId();
    }

    @Override
    public String deleteById(String id) {
        getById(id);

        jdbcTemplate.execute(String.format(DELETE_BY_ID, id));

        return id;
    }
}
