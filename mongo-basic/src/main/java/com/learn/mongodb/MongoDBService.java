package com.learn.mongodb;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

import static spark.Spark.*;

public class MongoDBService     {
        static final String version = "0.0.1";
        static Logger logger;


        public static void main(String[] args) {
            port(5000);
            staticFileLocation("static");

            LogManager.getLogManager().reset();
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();

            logger = LoggerFactory.getLogger(MongoDBService.class);
            logger.info("MongoDB Service version " + version);

            //Initialize MongoDB Client
            MongoConnection mongoConnection = MongoConnection.getMongoConnection();
            mongoConnection.init();

            MongoClient mongoClient;
            mongoClient = mongoConnection.getMongo();

            APIRoutes apiRoutes = new APIRoutes(mongoClient);

            get("/home",(req,res) -> apiRoutes.getHome(req,res));
            put("/books",(req,res) -> apiRoutes.addBook(req,res));
            get("/books/*",(req,res) -> apiRoutes.getBook(req,res));

            after((req, res) -> {
                res.type("application/json");
            });

            RegisterBookChangeStreamListener(mongoClient);

        }

        private static void RegisterBookChangeStreamListener(MongoClient mongoClient) {
            BookChangeStreamListener bookChangeStreamListener = new BookChangeStreamListener(mongoClient);
        }

}