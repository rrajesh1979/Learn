package com.learn.mongodb;

import com.learn.models.Book;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookDocumentMapper {
    private static final Logger logger = LoggerFactory.getLogger(BookDocumentMapper.class);
    private static String lastError;

    public String getLastError() {
        return lastError;
    }

    public static Book mapDocumentToBook(Bson bookBson) {
        int bookSchemaVersion;

        Book book = new Book();
        Document bookDoc = (Document) bookBson;

        bookSchemaVersion = bookDoc.getInteger(Book.SCHEMA_VERSION);

        switch (bookSchemaVersion) {
            case 1:
                parseDocumentV1(bookDoc, book);
        }

        return book;
    }

    private static void parseDocumentV1(Document bookDoc, Book book) {
        book.setPopulated(false);

        try {
            book.setId(bookDoc.getObjectId(Book.ID));
            book.setBookId(bookDoc.getInteger(Book.BOOK_ID));
            book.setBookName(bookDoc.getString(Book.BOOK_NAME));
            book.setAuthorName(bookDoc.getString(Book.AUTHOR_NAME));
            book.setSchemaVersion(bookDoc.getInteger(Book.SCHEMA_VERSION));
        } catch (Exception e) {
            lastError = e.getMessage();
            logger.warn("Unable to map document to Book object");
            logger.warn("Skipping document");
            book.setPopulated(false);
            return;
        }

        book.setPopulated(true);
    }
}
