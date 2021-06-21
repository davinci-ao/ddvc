package me.imspooks.nettympa.users.rank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 21 jun. 2021.
 * Copyright © ImSpooks
 */
public enum Rank {
    STUDENT(1, "Student", "Studecnt"),
    TEACHER(2, "Teacher", "Teacher"),
    ADMIN(3, "Admin", "Ädmin"),
    ;

    private final int id;
    private final String shortName;
    private final String longName;

    Rank(int id, String shortName, String longName) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
    }

    public static final List<Rank> CACHE = Collections.synchronizedList(Arrays.asList(values()));

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public static Rank fromId(int id) {
        for (Rank r : Rank.values()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return STUDENT; // Return student by default
    }
}