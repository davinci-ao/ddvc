package me.imspooks.nettympa.users;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import me.imspooks.nettympa.database.UserDatabaseTemplate;
import me.imspooks.nettympa.users.rank.Rank;
import org.bson.Document;

/**
 * Created by Nick on 21 jun. 2021.
 * Copyright © ImSpooks
 */
public class UserDatabase implements UserDatabaseTemplate {

    private MongoDatabase internalDatabase;

    public UserDatabase(MongoDatabase internalDatabase) {
        this.internalDatabase = internalDatabase;
    }

    private Document getCursor(int userId, String collection, MongoCursor<Document> cursor) {
        MongoCollection<Document> coll = this.internalDatabase.getCollection(collection);
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            Document newDocument = new Document("uuid", userId);
            coll.insertOne(newDocument);
            return newDocument;
        }
    }

    @Override
    public void createIndex() {
        for (String colls : new String[]{"users"}) {
            MongoCollection<?> coll = this.internalDatabase.getCollection(colls);
            coll.createIndex(new Document("uuid", -1)); // newest first
        }
    }

    @Override
    public Document getDocument(int userId) {
        MongoCollection<Document> coll = this.internalDatabase.getCollection("users");
        MongoCursor<Document> cursor = coll.find(new Document("uuid", userId)).iterator();
        return this.getCursor(userId, "players", cursor);
    }

    @Override
    public Document getDocument(String userName) {
        MongoCollection<Document> coll = this.internalDatabase.getCollection("users");
        MongoCursor<Document> cursor = coll.find(new Document("username", userName)).iterator();
        if (cursor.hasNext())
            return cursor.next();
        return null;
    }

    @Override
    public Document getDocument(int userId, String documentName) {
        MongoCollection<Document> coll = this.internalDatabase.getCollection(documentName);
        MongoCursor<Document> cursor = coll.find(new Document("uuid", userId)).iterator();
        return this.getCursor(userId, documentName, cursor);
    }

    @Override
    public Document createUser(User user, String password) {
        MongoCollection<Document> coll = this.internalDatabase.getCollection("users");
        MongoCursor<Document> cursor = coll.find(new Document("username", user.getUsername())).iterator();
        if (cursor.hasNext()) {
            return cursor.next();
        } else {
            Document newDocument = new Document("username", user.getUsername());
            coll.insertOne(newDocument);
            coll.updateOne(newDocument, new Document("$set", new Document("uuid", this.getNewestId("users")).append("email", user.getEmail()).append("password", password + " - TODO HASH")));
            return newDocument;
        }
    }

    @Override
    public Rank getRank(Document document) {
        return Rank.fromId(document.getInteger("rank"));
    }

    @Override
    public Rank getRank(int userId) {
        return this.getRank(this.getDocument(userId));
    }

    @Override
    public void setRank(int userId, Rank rank) {
        this.setData(userId, "rank", rank.getId());
    }

    @Override
    public Object getData(Document document, String key) {
        Document doc = document;
        while (key.contains(".")) {
            String splitKey = key.split(".")[0];
            doc = (Document) doc.get(key.split(".")[0]);
            key = key.substring(splitKey.length());

        }
        return doc.get(key);
    }

    @Override
    public void setData(int userId, String key, Object data) {
        MongoCollection<?> coll = this.internalDatabase.getCollection("users");
        Document document = this.getDocument(userId);
        Document update = new Document().append(key, data);
        coll.updateMany(document, new Document("$set", update));
    }

    @Override
    public void unsetData(int userId, String key) {
        MongoCollection<?> coll = this.internalDatabase.getCollection("users");
        Document document = this.getDocument(userId);
        coll.updateMany(document, new Document("$unset", key));
    }

    private int getNewestId(String collection) {
        MongoCollection<?> coll = this.internalDatabase.getCollection(collection);
        FindIterable<?> cursor = coll.find().sort(new Document("uuid", -1)).limit(1);

        for (Object o : cursor) {
            if (o instanceof Document && ((Document) o).get("uuid") != null) {
                return ((Document) o).getInteger("uuid") + 1;
            }
        }

        return 1;

        /*
        function getnextid($database,$collections){

     $m = new MongoClient();
    $db = $m->selectDB($database);
    $cursor = $collection->find()->sort(array("_id" => -1))->limit(1);
    $array = iterator_to_array($cursor);

    foreach($array as $value){



        return $value["_id"] + 1;

    }
 }
         */
    }
}