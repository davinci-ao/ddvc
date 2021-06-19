package me.imspooks.nettympa.backend.util;

import lombok.*;

/**
 * Created by Nick on 01 Nov 2020.
 * Copyright © ImSpooks
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter @Setter
public class Pair<K, V> {
    private K key;
    private V value;

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public static <K, V> Pair<K, V> of() {
        return new Pair<>();
    }
}