package com.github.apixandru.utils;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 12, 2016
 */
public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(final String string) {
        return null == string || string.isEmpty();
    }

}
