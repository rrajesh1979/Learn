package com.learn;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

@Slf4j
public class LombokFirst {

    static final String VERSION = "0.0.1";

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    public static void main(String[] args) {

        log.info("Lombok First version & using Lombok @Slf4j :: {}", VERSION);

        LombokFirst lombokFirstInstance = new LombokFirst();
        lombokFirstInstance.setFirstName("Mary");
        lombokFirstInstance.setLastName("Joe");

        log.info("Using Lombok Getter & Setter :: {}, {}",
                lombokFirstInstance.getLastName(),
                lombokFirstInstance.getFirstName());

        try {
            lombokFirstInstance.nonNullFunctionArgument(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nonNullFunctionArgument(@NonNull String firstName) {
        this.setFirstName(firstName);
    }
}
