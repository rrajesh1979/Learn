package com.learn.mongodb;

import com.learn.models.Book;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class BookDAL {
    public static final String DB = "learn";
    public static final String BOOKS_COLLECTION = "books";

    public static final String SCHEMA_VERSION = "schemaVersion";
    public static final String ID = "_id";
    public static final String BOOK_ID = "bookId";
    public static final String BOOK_NAME = "bookName";
    public static final String AUTHOR_NAME = "authorName";

    private final int schemaVersion = 1;
    private ObjectId id;
    private Integer bookId;
    private String bookName;
    private String authorName;

    private boolean populated;

    MongoClient mongoClient;
    MongoCollection<Document> bookCollection;

    Logger logger;

    private String lastError;

    String getLastError() {
        return lastError;
    }

    public BookDAL(MongoClient mongoClient) {
        logger = LoggerFactory.getLogger(APIRoutes.class);
        this.mongoClient = mongoClient;
        bookCollection = this.mongoClient.getDatabase(DB).getCollection(BOOKS_COLLECTION);
        logger.info("BooK DAL Initialized");
    }

    public BookDAL(MongoClient mongoClient, Document bookDoc) {
        logger = LoggerFactory.getLogger(APIRoutes.class);
        this.mongoClient = mongoClient;
        bookCollection = this.mongoClient.getDatabase(DB).getCollection(BOOKS_COLLECTION);
        logger.info("BooK DAL Initialized");

        parseDocument(bookDoc);
    }

    private void parseDocument(Document bookDoc) {
        Integer schemaVersion = bookDoc.getInteger(SCHEMA_VERSION);
        switch (schemaVersion) {
            case 1:
                parseDocumentV1(bookDoc);
        }

    }

    private void parseDocumentV1(Document bookDoc) {
        populated = false;

        try {
            id = bookDoc.getObjectId(ID);
            bookId = bookDoc.getInteger(BOOK_ID);
            bookName = bookDoc.getString(BOOK_NAME);
            authorName = bookDoc.getString(AUTHOR_NAME);
        } catch (Exception e) {
            lastError = e.getMessage();
            populated = false;
            return;
        }

        populated = true;
    }

    public boolean addBook() {
        if(!populated) {
            this.lastError = "Book DAL not populated";
            return false;
        }

        Document bookDoc = new Document(BOOK_ID, bookId)
                .append(BOOK_NAME, bookName)
                .append(AUTHOR_NAME, authorName)
                .append(SCHEMA_VERSION, schemaVersion);

        try {
            bookCollection.insertOne(bookDoc);
            parseDocument(bookDoc);
        } catch (MongoException e) {
            lastError = e.getMessage();
            return false;
        }

        return true;
    }

    public Document getBook(Integer bookId) {
        Document bookDoc;

        try {
            bookDoc = bookCollection.find(eq(BOOK_ID, bookId)).first();
        } catch (MongoException e) {
            lastError = e.getMessage();
            return new Document("ok", false).append("error", "Error finding book");
        }
        if (bookDoc == null) {
            return new Document("ok", false).append("error", "Error finding book");
        }
        return bookDoc;
    }

    public List<Book> getBooks() {
        List<Document> bookDocs = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        try {
            bookCollection
                    .find()
                    .iterator()
                    .forEachRemaining(bookDocs::add);

            books = bookDocs
                    .stream()
                    .map(BookDocumentMapper::mapDocumentToBook)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            lastError = e.getMessage();
            logger.debug("Error fetching book list");
        }

        return books;
    }
}
