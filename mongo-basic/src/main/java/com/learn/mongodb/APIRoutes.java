package com.learn.mongodb;

import com.google.gson.Gson;
import com.learn.models.Book;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
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
    private MongoCollection<Document> bookCollection;

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
        if (!preFlightChecks()) {
            logger.info("Pre-flight checks failed");
        } else {
            logger.info("Pre-flight checks successful");
        }
    }

    private boolean preFlightChecks() {
        try {
            mongoClient.getDatabase("any").runCommand(new Document("ping", 1));
            logger.info("Connection to MongoDB tested");
            boolean bookIdIndexPresent = false;
            boolean authorNameIndexPresent = false;

            bookCollection = this.mongoClient.getDatabase(DBConstants.DB).getCollection(DBConstants.BOOKS_COLLECTION);
            for (Document index : bookCollection.listIndexes()) {
                System.out.println(index.toJson());
                if (index.get("name") == "authorName_1") {
                    authorNameIndexPresent = true;
                }
                if (index.get("name") == "bookId_1_bookName_1") {
                    bookIdIndexPresent = true;
                }
            }
            if (bookIdIndexPresent && authorNameIndexPresent) {
                logger.info("Required indexes present");
            } else {
                logger.info("Required indexes missing");
                return false;
            }
        } catch (Exception e) {
            lastError = e.getMessage();
            logger.error("Error completing pre-flight checks");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getHome(Request req, Response res) {
        return new Document("ok", true).append("success", "Default message").toJson();
    }

    public String addBook(Request req, Response res) {
        Integer bookId;
        String bookName;
        String authorName;
        boolean status;

        Document requestBody;
        Document bookDoc = new Document(BookDAL.SCHEMA_VERSION, 1);

        try {
            requestBody = Document.parse(req.body());
            bookId = Integer.parseInt(requestBody.getString(BookDAL.BOOK_ID));
            bookName = requestBody.getString(BookDAL.BOOK_NAME);
            authorName = requestBody.getString(BookDAL.AUTHOR_NAME);

            bookDoc.append(BookDAL.BOOK_ID, bookId);
            bookDoc.append(BookDAL.BOOK_NAME, bookName);
            bookDoc.append(BookDAL.AUTHOR_NAME, authorName);

        } catch (Exception e) {
            lastError = e.getMessage();
            logger.error("Error parsing query params");
            return new Document("error", true).append("error", "Error parsing query params").toJson();
        }

        try {
            bookDAL = new BookDAL(mongoClient, bookDoc);
            status = bookDAL.addBook();
        } catch (MongoException e) {
            lastError = e.getMessage();
            logger.error("Error adding book");
            return new Document("error", true).append("error", "Error adding book").toJson();
        }

        if (!status) {
            logger.error("Error adding book");
            return new Document("ok", false).append("error", "Error adding book").toJson(plainJSON);
        } else {
            logger.error("Book added successfully");
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
