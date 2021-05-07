package com.learn.mongodb;

import com.google.gson.Gson;
import com.learn.models.Book;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class APIRoutes {
    private final Logger logger;
    private final MongoClient mongoClient;

    BookDAL bookDAL;

    private String lastError;

    private final JsonWriterSettings plainJSON = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED)
            .binaryConverter((value, writer) -> writer.writeString(Base64.getEncoder().encodeToString(value.getData())))
            .dateTimeConverter((value, writer) -> {
                ZonedDateTime zonedDateTime = Instant.ofEpochMilli(value).atZone(ZoneOffset.UTC);
                writer.writeString(DateTimeFormatter.ISO_DATE_TIME.format(zonedDateTime));
            }).decimal128Converter((value, writer) -> writer.writeString(value.toString()))
            .objectIdConverter((value, writer) -> writer.writeString(value.toHexString()))
            .symbolConverter((value, writer) -> writer.writeString(value)).build();

    public APIRoutes(MongoClient mongoClient) {
        logger = LoggerFactory.getLogger(APIRoutes.class);
        this.mongoClient = mongoClient;

        //Pre-flight check
        mongoClient.getDatabase("any").runCommand(new Document("ping", 1));
        logger.info("Connection to MongoDB tested");

    }

    public String getHome(Request req, Response res) {
        return new Document("ok", true).append("success", "Default message").toJson();
    }

    public String addBook(Request req, Response res) {
        Integer bookId;
        String bookName;
        String authorName;
        boolean status;

        Document bookDoc = new Document(BookDAL.SCHEMA_VERSION, 1);

        try {
            bookId = Integer.parseInt(req.queryParams("bookId"));
            bookName = req.queryParams("bookName");
            authorName = req.queryParams("authorName");

            bookDoc.append(BookDAL.BOOK_ID, bookId);
            bookDoc.append(BookDAL.BOOK_NAME, bookName);
            bookDoc.append(BookDAL.AUTHOR_NAME, authorName);

        } catch (Exception e) {
            lastError = e.getMessage();
            return new Document("error", true).append("error", "Error parsing query params").toJson();
        }

        try {
            bookDAL = new BookDAL(mongoClient, bookDoc);
            status = bookDAL.addBook();
        } catch (MongoException e) {
            lastError = e.getMessage();
            return new Document("error", true).append("error", "Error adding book").toJson();
        }

        if (!status) {
            return new Document("ok", false).append("error", "Error adding book").toJson(plainJSON);
        } else {
            return new Document("ok", true).append("book", bookDoc).toJson(plainJSON);
        }


    }

    public String getBook(Request req, Response res) {
        String inputBookId = req.splat()[0];
        Integer bookId;

        if (inputBookId == null) {
            res.status(404);
            return new Document("ok", false).append("error", "Book ID missing").toJson();
        }

        bookId = Integer.parseInt(inputBookId);
        Document bookDoc;

        try {
            bookDAL = new BookDAL(mongoClient);
            bookDoc = bookDAL.getBook(bookId);
        } catch (MongoException e) {
            lastError = e.getMessage();
            return new Document("error", true).append("error", "Error adding book").toJson();
        }

        return new Document("ok", true).append("bookDoc", bookDoc).toJson(plainJSON);
    }

    public String getBooks(Request req, Response res) {
        bookDAL = new BookDAL(mongoClient);
        List<Book> books = bookDAL.getBooks();
        ArrayList<Document> result = new ArrayList<>();

        try {
            for (Book book: books) {
                logger.info(book.toJson());
                result.add(book.toDocument());
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            logger.debug("Error building return response for /books");
        }

        return new Gson().toJson(result);
    }
}
