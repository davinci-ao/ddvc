package me.imspooks.nettympa.backend.app.response;

import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 26 Jun 2020.
 * Copyright © ImSpooks
 */
@RequiredArgsConstructor
public enum ResponseType {

    PLAIN("Plain", "text/plain", new String[0]),

    // STANDARD
    HTML("HyperText Markup Language", "text/html; charset=utf-8", new String[] {"htm", "html"}),
    JAVASCRIPT("JavaScript", "text/javascript", new String[] {"js"}),
    CSS("Cascading Style Sheets", "text/css", new String[] {"css"}),
    JSON("JSON", "application/json", new String[] {"json"}),

    // FONT
    FONT_WOFF("Web Open Font Format", "font/woff", new String[] {"woff"}),
    FONT_WOFF_2("Web Open Font Format", "font/woff2", new String[] {"woff2"}),
    TTF("TrueType Font", "font/tff", new String[] {"ttf"}),
    OTF("OpenType Font", "font/otf", new String[] {"otf"}),

    // IMAGE
    BMP("Windows OS/2 Bitmap Graphics", "image/bmp", new String[] {"bmp"}),
    GIF("Graphics Interchange Format", "image/gif", new String[] {"gif"}),
    ICON("Icon format", "image/x-icon", new String[] {"ico"}),
    JPEG("JPEG imagest", "image/jpeg", new String[] {"jpg", "jpeg"}),
    PNG("Portable Network Graphics", "image/png", new String[] {"png"}),
    SVG("Scalable Vector Graphics", "image/svg+xml", new String[] {"svg"}),
    TIFF("Tagged Image File Format", "image/tiff", new String[] {"tif", "tiff"}),
    WEBP("WEBP image", "image/webp", new String[] {"webp"}),

    // VIDEO
    AVI("Audio Video Interleave", "video/x-msvideo", new String[] {"avi"}),
    MPEG("MPEG Video", "video/mpeg", new String[] {"mpeg"}),
    OGV("OGG Video", "video/ogg", new String[] {"ogv"}),
    TS("MPEG transport stream", "video/mp2t", new String[] {"ts"}),
    WEBM("WEBM video", "video/webm", new String[] {"webm"}),

    // AUDIO
    AAC("AAC audio", "audio/aac", new String[] {"acc"}),
    MIDI("Musical Instrument Digital Interface", "audio/midi", new String[] {"mid", "midi"}),
    MP3("MP3 audio", "audio/mpeg", new String[] {"mp3"}),
    OGG("OGG audio", "audio/ogg", new String[] {"ogg", "oga"}),
    OPUS("Opus audio", "audio/opus", new String[] {"opus"}),
    WAVE("Waveform Audio Format", "audio/wav", new String[] {"wav"}),
    WEBA("WEBM audio", "audio/webm", new String[] {"weba"}),

    NBS("WEBM audio", "text/plain", new String[] {"nbs"}),
    ;

    public static final ResponseType[] CACHE = values();

    private final String name;
    private final String contentType;
    private final String[] extensions;

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public static ResponseType getFromName(String name) {
        for (ResponseType type : CACHE) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return PLAIN;
    }

    public static ResponseType getFromMime(String mime) {
        for (ResponseType type : CACHE) {
            if (type.getContentType().equalsIgnoreCase(mime)) {
                return type;
            }
        }

        return PLAIN;
    }

    public static ResponseType getFromExtension(String extension) {
        for (ResponseType type : CACHE) {
            for (String extensions : type.getExtensions()) {
                if (extensions.equalsIgnoreCase(extension)) {
                    return type;
                }
            }
        }

        return PLAIN;
    }
}