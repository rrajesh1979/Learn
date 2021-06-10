package com.learn;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

public class LombokFirst {

    static final String VERSION = "0.0.1";
    static Logger logger;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    public static void main(String[] args) {

        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        logger = LoggerFactory.getLogger(LombokFirst.class);
        logger.info("Lombok First version :: {}", VERSION);

        LombokFirst lombokFirstInstance = new LombokFirst();
        lombokFirstInstance.setFirstName("Mary");
        lombokFirstInstance.setLastName("Joe");

        logger.info("Using Lombok Getter & Setter :: {}, {}",
                lombokFirstInstance.getLastName(),
                lombokFirstInstance.getFirstName());
    }
}
