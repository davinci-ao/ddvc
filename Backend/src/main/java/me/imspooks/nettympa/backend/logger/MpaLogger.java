package me.imspooks.nettympa.backend.logger;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Nick on 07 jun. 2021.
 * Copyright © ImSpooks
 */
public class MpaLogger {
    @Getter private static final Logger logger = LoggerFactory.getLogger(MpaLogger.class);
}