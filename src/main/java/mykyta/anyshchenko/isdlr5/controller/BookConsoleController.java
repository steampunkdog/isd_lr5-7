package mykyta.anyshchenko.isdlr5.controller;

import mykyta.anyshchenko.isdlr5.model.Book;
import mykyta.anyshchenko.isdlr5.service.BookService;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class BookConsoleController {

    private static Logger LOGGER;

    private BookService bookService;

    private static final String GET_ALL_COMMAND = "getAll";
    private static final String GET_BY_ID_COMMAND = "getById";
    private static final String DELETE_BY_FIRST_CHAR_COMMAND = "delByFirstChar";
    private static final String SOFT_DELETE_BY_IDS_COMMAND = "softDelByIds";
    private static final String EXIT_COMMAND = "exit";

    public BookConsoleController(BookService bookService) {
        LOGGER = LogManager.getLogger(BookConsoleController.class);
        this.bookService = bookService;
    }

    @PostConstruct
    public void init(){
        Stream.of(
                new Book("TMITHC", "The Man in the High Castle", "Philip Kindred Dick"),
                new Book("DADOES", "Do Androids Dream of Electric Sheep?", "Philip Kindred Dick"),
                new Book("TTOTA",  "The Transmigration of Timothy Archer", "Philip Kindred Dick"),
                new Book("TPT",  "The Penultimate Truth", "Philip Kindred Dick"),
                new Book("UBK",  "Ubik", "Philip Kindred Dick")
        ).forEach(book -> bookService.save(book));
    }

    public void start(Scanner scanner) {
        System.out.println("Hello, its Lab #5 from Anyshchenko Mykyta AI-171");
        System.out.println("Commands:");
        System.out.println("\t " + GET_ALL_COMMAND + " - retrieve all entities");
        System.out.println("\t " + GET_BY_ID_COMMAND + " {id} - retrieve specific entity | example: getById abc");
        System.out.println("\t " + DELETE_BY_FIRST_CHAR_COMMAND + " {c} - deletes entities with a name starting with the specified character | example: deleteByFirstCharInName A");
        System.out.println("\t " + SOFT_DELETE_BY_IDS_COMMAND + " {id1 id2 id3 ...} - soft deletes entities with specified ids | example: softDeleteByIds a b 23");
        System.out.println("\t " + EXIT_COMMAND + " - close program");
        System.out.println("NOTE: soft deleted entity can't be retrieved \"getAll\" command \nbut you can find it by id(\"getById\" command)");


        boolean exit = false;
        while (!exit) {
            try {

                String currentCommand = scanner.nextLine().trim();
                String[] args = currentCommand.split(" ");

                if(args[0] == null || args[0].equals("")){
                    throw new IllegalArgumentException("Command can`t be empty");
                }


                switch (args[0]){
                    case GET_ALL_COMMAND:
                        getAll();
                        break;
                    case GET_BY_ID_COMMAND:
                        getById(args);
                        break;
                    case DELETE_BY_FIRST_CHAR_COMMAND:
                        deleteByFirstCharInName(args);
                        break;
                    case SOFT_DELETE_BY_IDS_COMMAND:
                        softDeleteByIds(args);
                        break;
                    case EXIT_COMMAND:
                        exit = true;
                        break;
                }

            } catch (Exception e){
                LOGGER.error(e.getMessage());
            }
        }

    }

    private void deleteByFirstCharInName(String[] args){
        if(args.length < 2){
            throw new IllegalArgumentException("char doesn`t provided");
        }
        bookService.deleteByFirstCharInName(args[1].charAt(0))
                .forEach(id -> System.out.println("DELETED: '" + id + "'"));
    }

    private void softDeleteByIds(String[] args){
        if(args.length < 2){
            throw new IllegalArgumentException("ids doesn`t provided");
        }

        bookService.softDeleteByIds(Arrays.asList(Arrays.copyOfRange(args, 1, args.length).clone()))
            .forEach(id -> System.out.println("MARKED AS DELETED: '" + id + "'"));
    }

    private void getAll(){
        bookService.getAll()
                .forEach(book -> System.out.println(book.toString()));
    }

    private void getById(String[] args){
        if(args.length < 2){
            throw new IllegalArgumentException("id doesn`t provided");
        }

        System.out.println(bookService.getById(args[1]).toString());
    }

}
