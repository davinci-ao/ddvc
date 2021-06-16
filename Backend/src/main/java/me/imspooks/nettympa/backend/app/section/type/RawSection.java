package me.imspooks.nettympa.backend.app.section.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imspooks.nettympa.backend.app.section.Section;

/**
 * Created by Nick on 28 Jun 2020.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public abstract class RawSection implements Section {

    @Getter private final String name;
}