package com.github.apixandru.utils;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public final class ReflectionUtil {

    private ReflectionUtil() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot instantiate " + className, e);
        }
    }

}
