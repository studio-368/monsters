package edu.bsu.storygame.core.util;

public final class ScreamingCapitalizer {
    public static String convert(String s) {
        return s.toUpperCase().replace(" ", "_");
    }
}
