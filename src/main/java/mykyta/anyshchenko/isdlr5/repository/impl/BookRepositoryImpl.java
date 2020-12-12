package mykyta.anyshchenko.isdlr5.repository.impl;

import mykyta.anyshchenko.isdlr5.exception.ObjectNotFoundException;
import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.repository.BookRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private static Logger LOGGER;


    private static final String GET_ALL = "SELECT * FROM book WHERE deleted = false";
    private static final String GET_BY_ID = "SELECT * FROM book WHERE id = '%s'";
    private static final String INSERT = "INSERT INTO book (id, name, author, deleted) VALUES ('%s', '%s', '%s' , %b);";
    private static final String DELETE_BY_ID = "DELETE FROM book WHERE id = '%s'";
    private static final String UPDATE_BY_ID = "UPDATE book " +
                                                "SET id = '%s', name = '%s', author = '%s', deleted = %b " +
                                                "WHERE id = '%s'";

    private JdbcTemplate jdbcTemplate;
    private RowMapper<Book> bookRowMapper = (resultSet, i) -> {
        Book book = new Book();
        book.setId(resultSet.getString("id"));
        book.setName(resultSet.getString("name"));
        book.setAuthor(resultSet.getString("author"));
        book.setDeleted(resultSet.getBoolean("deleted"));
        return book;
    };

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        LOGGER = LogManager.getLogger(BookRepositoryImpl.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Book> getAll() {
        LOGGER.trace("getAll()");
        List<Book> books = jdbcTemplate.query(GET_ALL, bookRowMapper);
        LOGGER.trace("getAll() - finished");
        return books;
    }

    @Override
    public Book getById(String id) {
        LOGGER.trace("getById('" + id + "')");
        try{
            Book book = jdbcTemplate.queryForObject(String.format(GET_BY_ID, id), bookRowMapper);
            LOGGER.trace("getById('" + id + "') - finished");
            return book;
        } catch (EmptyResultDataAccessException e){
            throw new ObjectNotFoundException("Object with id '" + id + "' not found");
        }
    }

    @Override
    public String save(Book book) {
        LOGGER.trace("save('" + book + "')");

        try{

            String id = update(book);
            LOGGER.trace("save('" + book + "') - finished");
            return id;

        } catch (ObjectNotFoundException e){

            jdbcTemplate.execute(String.format(
                    INSERT,
                    book.getId(),
                    book.getName(),
                    book.getAuthor(),
                    book.isDeleted()
            ));

            LOGGER.trace("save('" + book + "') - finished");
            return book.getId();
        }
    }

    @Override
    public String update(Book book) {
        LOGGER.trace("update('" + book + "')");
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

        LOGGER.trace("update('" + book + "') - finished");
        return book.getId();
    }

    @Override
    public String deleteById(String id) {
        LOGGER.trace("deleteById('" + id + "')");

        getById(id);

        jdbcTemplate.execute(String.format(DELETE_BY_ID, id));


        LOGGER.trace("deleteById('" + id + "') - finished");
        return id;
    }
}
