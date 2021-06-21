package com.learn.springreactive;

import com.learn.springreactive.domain.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository {
    Mono<Book> getByISBN(Integer isbn);
    Flux<Book> findAll();
}
