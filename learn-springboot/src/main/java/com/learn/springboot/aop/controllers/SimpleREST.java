package com.learn.springboot.aop.controllers;

import com.learn.springboot.aop.annotations.LogMethodSignature;
import com.learn.springboot.aop.annotations.LogPerformance;
import com.learn.springboot.aop.annotations.RetryOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
public class SimpleREST {

    @LogMethodSignature
    @GetMapping(path = "/api/hello/{name}")
    public String hello(@PathVariable(value = "name") String name) {
        return "Hello " + name + " :: From Spring AOP!";
    }

    @LogMethodSignature
    @LogPerformance
    @GetMapping(path = "/api/book/{Id}")
    public String getBookDetails(@PathVariable(value = "Id") String id) {
        return "Book details for " + id + " !";
    }

    @LogMethodSignature
    @LogPerformance
    @RetryOperation
    @GetMapping(path = "/api/books")
    public String getBooksSimulateException() {
        if (new Random().nextBoolean()) {
            log.info("Un-successful attempt !!");
            throw new RuntimeException();
        } else {
            log.info("Successful attempt !!");
        }
        return "Book List !";
    }
}
