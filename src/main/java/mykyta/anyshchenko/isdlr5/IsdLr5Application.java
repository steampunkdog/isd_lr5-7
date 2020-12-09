package mykyta.anyshchenko.isdlr5;

import mykyta.anyshchenko.isdlr5.controller.BookConsoleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class IsdLr5Application implements CommandLineRunner {

    @Autowired
    BookConsoleController bookConsoleController;

    public static void main(String[] args) {
        SpringApplication.run(IsdLr5Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        bookConsoleController.start(scanner);
    }

}
