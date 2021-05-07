package com.learn.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mongodb.client.model.Aggregates.match;
import static java.util.Arrays.asList;

public class BookChangeStreamListener {
    private Logger logger;
    private MongoClient mongoClient;
    private MongoCollection<Document> bookCollection;
    private BookDAL bookDAL;

    public BookChangeStreamListener(MongoClient mongoClient) {
        logger = LoggerFactory.getLogger(BookChangeStreamListener.class);
        this.mongoClient = mongoClient;
        bookCollection = mongoClient.getDatabase("learn").getCollection("books");

        List<Bson> pipeline = asList(match(Filters.in("operationType", asList("insert"))));
//        bookCollection.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP).forEach(logBookDetails);
        MongoCursor<ChangeStreamDocument<Document>> bookCursor = bookCollection.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP).iterator();

        while (bookCursor.hasNext()) {
            ChangeStreamDocument<Document> nextBook = bookCursor.next();
            logger.info("Book change stream info: " + nextBook);
        }

        logger.info("Book change stream created");
    }

//    Block<ChangeStreamDocument<Document>> logBookDetails = new Block<ChangeStreamDocument<Document>>() {
//        @Override
//        public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {
//            logger.info("Book change stream info: " + changeStreamDocument);
//        }
//    };
}
