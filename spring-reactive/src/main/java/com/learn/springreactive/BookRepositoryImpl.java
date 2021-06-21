package com.learn.springreactive;

import com.learn.springreactive.domain.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BookRepositoryImpl implements BookRepository {

    Book harryPotter = new Book(1, "Harry Potter and the Sorcerer's stone", "J K Rowling");
    Book hitchHikersGuide = new Book(2, "The Hitchhiker's Guide to the Galaxy", "Douglas Adams");
    Book hardyBoys = new Book(3, "Hardy Boys and The Tower Treasure", "Franklin W. Dixon");
    Book nancyDrew = new Book(4, "Nancy Drew and The Secret of the Old Clock", "Carolyn Keene");

    @Override
    public Mono<Book> getByISBN(Integer isbn) {
        return Mono.just(harryPotter);
    }

    @Override
    public Flux<Book> findAll() {
        return Flux.just(harryPotter, hitchHikersGuide, hardyBoys, nancyDrew);
    }
}
