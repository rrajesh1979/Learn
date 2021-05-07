package com.learn.mongodb;

import com.mongodb.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.String.format;

public class MongoConnection {
    Logger logger;
    private static MongoConnection mongoConnection = new MongoConnection();
    private MongoClient mongoClient = null;

    Properties appProps = new Properties();
    String appConfigPath;

    String URI;

    public MongoConnection() {
        logger = LoggerFactory.getLogger(MongoConnection.class);
    }
    public MongoConnection(String newURI) {
        logger = LoggerFactory.getLogger(MongoConnection.class);
        URI = newURI;
        logger.debug("Setting MongoDB URL :" + URI);
    }

    public static MongoConnection getMongoConnection() {
        return mongoConnection;
    }

    public void init() {
        logger.debug("Bootstraping MongoDB Connection");

        URI = System.getenv("MONGODB_URI");
        getMongo();
    }

    public MongoClient getMongo() throws RuntimeException {
        if (mongoClient == null) {
            logger.debug("Starting MongoDB Connection");
            MongoClientOptions.Builder connectionOptions = MongoClientOptions.builder()
                    .connectionsPerHost(4)
                    .maxConnectionIdleTime(60*1000)
                    .maxConnectionLifeTime(120*1000)
                    .writeConcern(WriteConcern.MAJORITY)
                    .retryWrites(true)
                    ;

            MongoClientURI mongoClientURI = new MongoClientURI(URI, connectionOptions);

            logger.info("About to connect to MongoDB @ " + mongoClientURI.toString());

            try {
                mongoClient = new MongoClient(mongoClientURI);
            } catch (MongoException ex) {
                logger.error("An error occoured when connecting to MongoDB", ex);
            } catch (Exception ex) {
                logger.error("An error occoured when connecting to MongoDB", ex);
            }

        }
        return mongoClient;
    }

    public void close() {
        logger.info("Closing MongoDB connection");
        if (mongoClient != null) {
            try {
                mongoClient.close();
                mongoClient = null;
            } catch (Exception ex) {
                logger.error(format("An error occurred when closing the MongoDB connection\n%s", ex.getMessage()));
            }
        }
    }
}
