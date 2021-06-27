package com.learn.springboot.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleREST {

    @GetMapping(path = "/api/hello/{name}")
    public String hello(@PathVariable(value = "name") String name) {
        return "Hello " + name + " from Spring AOP!";
    }

    @GetMapping(path = "/api/book/{Id}")
    public String getBookDetails(@PathVariable(value = "Id") String id) {
        return "Book details for " + id + " !";
    }
}
