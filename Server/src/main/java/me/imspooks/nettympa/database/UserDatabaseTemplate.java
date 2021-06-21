package me.imspooks.nettympa.database;

import me.imspooks.nettympa.users.User;
import me.imspooks.nettympa.users.rank.Rank;
import org.bson.Document;

/**
 * Created by Nick on 21 jun. 2021.
 * Copyright © ImSpooks
 */
public interface UserDatabaseTemplate extends CoreDatabaseMethods {

    Document createUser(User user, String password);

    Rank getRank(Document document);

    Rank getRank(int userId);

    void setRank(int userId, Rank rank);

    Object getData(Document document, String key);

    void setData(int userId, String key, Object data);

    void unsetData(int userId, String key);


}