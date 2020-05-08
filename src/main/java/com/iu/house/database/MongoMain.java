package com.iu.house.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Level;

public class MongoMain {

    public MongoCollection mongoConnect(){

        String uri="mongodb+srv://admin:admin@cluster0-r9eqp.mongodb.net/test";

        MongoClientURI clientURI=new MongoClientURI(uri);
        MongoClient mongoClient=new MongoClient(clientURI);

        MongoDatabase mongoDatabase=mongoClient.getDatabase("HomeRecords");
        MongoCollection collection=mongoDatabase.getCollection("records");

        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);

        return collection;
    }
}
