package mykyta.anyshchenko.isdlr5;

import mykyta.anyshchenko.isdlr5.controller.BookConsoleController;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.ManagementFactory;
import java.util.Scanner;

@SpringBootApplication
public class IsdLr5Application implements CommandLineRunner {

    private static Logger LOGGER;

    @Autowired
    BookConsoleController bookConsoleController;

    public static void main(String[] args) {
        SpringApplication.run(IsdLr5Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        configureLogLevel();
        LOGGER = LogManager.getLogger(IsdLr5Application.class);
        LOGGER.info("Application started");

        Scanner scanner = new Scanner(System.in);
        bookConsoleController.start(scanner);

        LOGGER.info("Application closed");
    }

    private static void configureLogLevel(){

        if(ManagementFactory.getRuntimeMXBean().getInputArguments().contains("-verbose")) {

            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            LoggerConfig log = ctx.getConfiguration().getRootLogger();
            log.setLevel(Level.TRACE);
            ctx.updateLoggers();

            System.out.println("aaaaaaaaaaaaaaaaaa" + ctx.getRootLogger().getLevel());
            LogManager.getLogger(IsdLr5Application.class).trace("ssssssssssssssssssssssssssss");
        }
    }

}
