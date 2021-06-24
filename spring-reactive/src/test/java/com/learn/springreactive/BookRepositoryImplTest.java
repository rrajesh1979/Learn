package com.learn.springreactive;

import com.learn.springreactive.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryImplTest {

    BookRepositoryImpl bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepositoryImpl();
    }

    @Test
    void getByISBNBlock() {
        Mono<Book> bookMono = bookRepository.getByISBN(1);
        Book book = bookMono.block();
        System.out.println(book.toString());
    }

    @Test
    void getByISBNSubscribe() {
        Mono<Book> bookMono = bookRepository.getByISBN(1);

        bookMono.subscribe(book -> {
            System.out.println(book.toString());
        });
    }

    @Test
    void getByISBNMapFunction() {
        Mono<Book> bookMono = bookRepository.getByISBN(1);

        bookMono.map(Book::getAuthor).subscribe(author -> {
            System.out.println("Author :: " + author);
        });
    }

    @Test
    void fluxTestBlock() {
        Flux<Book> bookFlux = bookRepository.findAll();

        Book book = bookFlux.blockFirst();
        System.out.println(book.toString());
    }

    @Test
    void fluxTestSubscribe() {
        Flux<Book> bookFlux = bookRepository.findAll();

        bookFlux.subscribe(book -> {
            System.out.println(book.toString());
        });
    }

    @Test
    void fluxTestToListMono() {
        Flux<Book> bookFlux = bookRepository.findAll();

        Mono<List<Book>> bookListMono = bookFlux.collectList();
        bookListMono.subscribe(list -> {
            list.forEach(book -> {
                System.out.println(book.toString());
            });
        });
    }
}