package com.github.apixandru.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public final class ReflectionUtil {

    private ReflectionUtil() {

    }

    public static <T> T newInstance(final Class<T> clasz, final Object... parameters) {
        return newInstance(getConstructor(clasz, parameters), parameters);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found " + className, e);
        }
    }

    private static <T> T newInstance(final Constructor<T> constructor, final Object... parameters) {
        try {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(parameters);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot instantiate " + constructor.getName(), e);
        }
    }

    private static <T> Constructor<T> getConstructor(final Class<T> clasz, final Object... parameters) {
        final Class<?>[] classes = getClasses(parameters);
        try {
            return clasz.getDeclaredConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No constructor in class with parameters " + Arrays.toString(classes));
        }
    }

    private static Class<?>[] getClasses(Object... parameters) {
        return Arrays.stream(parameters)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }

}
