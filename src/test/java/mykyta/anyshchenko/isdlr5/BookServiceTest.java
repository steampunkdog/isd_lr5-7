package mykyta.anyshchenko.isdlr5;

import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.service.BookService;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(classes = BookServiceTestConfiguration.class, properties="spring.main.allow-bean-definition-overriding=true")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    private static final Map<String, Book> DEFAULT_BOOKS = new HashMap<String, Book>(){{
        put("TMITHC", new Book("TMITHC", "The Man in the High Castle", "Philip Kindred Dick"));
        put("DADOES", new Book("DADOES", "Do Androids Dream of Electric Sheep?", "Philip Kindred Dick"));
        put("TTOTA", new Book("TTOTA",  "The Transmigration of Timothy Archer", "Philip Kindred Dick"));
        put("TPT", new Book("TPT",  "The Penultimate Truth", "Philip Kindred Dick"));
        put("UBK", new Book("UBK",  "Ubik", "Philip Kindred Dick"));
    }};


    @BeforeEach
    public void refreshRepository(){
        DEFAULT_BOOKS.values().stream().map(Book::new).forEach(bookService::save);
    }

    @Test
    public void testDeleteByFirstCharInNameWithCorrectInput(){

        Collection<Book> books = bookService.getAll();
        int numOfExistingEntitiesOnStart = books.size();

        Assertions.assertThat(books)
                .withFailMessage("Provide Book with name starts with 'T' for correct testing")
                .anyMatch(book -> book.getName().charAt(0) == 'T');

        Collection<String> deletedBooksIds = bookService.deleteByFirstCharInName('T');
        int numOfDeleted = deletedBooksIds.size();

        Assertions.assertThat(deletedBooksIds)
                .withFailMessage("Some Books which names doesnt starts with specified symbol('T') are deleted")
                .allMatch(id -> DEFAULT_BOOKS.get(id).getName().charAt(0) == 'T');

        Assertions.assertThat(numOfExistingEntitiesOnStart - numOfDeleted == bookService.getAll().size())
                .withFailMessage("Deleted more entities then method returned")
                .isTrue();

        Assertions.assertThat(bookService.getAll())
                .withFailMessage("Some Books which names starts with specified symbol('T') arn`t deleted")
                .allMatch(book -> book.getName().charAt(0) != 'T');

    }

    @Test
    public void testDeleteByFirstCharInNameWithNotExisting(){

        int numOfExistingEntitiesOnStart = bookService.getAll().size();

        Assertions.assertThat(bookService.deleteByFirstCharInName('|'))
                .withFailMessage("Method deleted entity\\ies that should not even exist, check test data, or method logic")
                .isEmpty();


        Assertions.assertThat(numOfExistingEntitiesOnStart == bookService.getAll().size())
                .withFailMessage("Method deleted entity\\ies that should not be deleted")
                .isTrue();

    }

    @Test
    public void testDeleteByFirstCharInNameWithEmptyDatabase(){

        bookService.getAll()
                .forEach(book -> bookService.deleteById(book.getId()));

        Assertions.assertThat(bookService.deleteByFirstCharInName('T'))
                .withFailMessage("Method deleted entity\\ies that should not even exist")
                .isEmpty();

    }


    @Test
    public void testSoftDeleteByIdsWithCorrectInput(){

        Collection<Book> booksOnStart = bookService.getAll();

        List<String> idsOfBooksToDelete = booksOnStart.stream()
                .map(Book::getId)
                .limit(2)
                .collect(Collectors.toList());

        Collection<String> deletedBooksIds = bookService.softDeleteByIds(idsOfBooksToDelete);

        Collection<Book> booksAfterSoftDelete = bookService.getAll();

        Assertions.assertThat(booksAfterSoftDelete)
                .withFailMessage("Some entities which should be soft-deleted still present in response")
                .map(Book::getId)
                .isNotIn(idsOfBooksToDelete);

        Assertions.assertThat(deletedBooksIds)
                .withFailMessage("Some entities that should be marked as deleted isn`t marked so")
                .map(bookService::getById)
                .allMatch(Book::isDeleted);

    }

    @Test
    public void testSoftDeleteByIdsWithNullInput(){
        Assertions.assertThatThrownBy(() -> bookService.softDeleteByIds(null))
                .withFailMessage("Method should throw IllegalArgumentException")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSoftDeleteByIdsWithOneOfIdsNull(){

        List<String> idsOfBooksToDelete = bookService.getAll().stream()
                .map(Book::getId)
                .limit(2)
                .collect(Collectors.toList());

        idsOfBooksToDelete.add(null);

        Assertions.assertThatThrownBy(() -> bookService.softDeleteByIds(idsOfBooksToDelete))
                .withFailMessage("Method should throw IllegalArgumentException")
                .isInstanceOf(IllegalArgumentException.class);

    }

}
