package me.imspooks.nettympa.database;

import org.bson.Document;

/**
 * Created by Nick on 21 jun. 2021.
 * Copyright © ImSpooks
 */
public interface CoreDatabaseMethods {

    void createIndex();
    Document getDocument(int userId);
    Document getDocument(String userName);

    Document getDocument(int userId, String documentName);
}