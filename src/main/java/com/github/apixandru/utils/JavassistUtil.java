package com.github.apixandru.utils;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since March 06, 2016
 */
public class JavassistUtil {

    public static CtClass makeClass(final byte[] bytes) throws IOException {
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            return ClassPool.getDefault().makeClass(stream);
        }
    }

    @SuppressWarnings("unchecked")
    public static void mockDependencies(final CtClass clasz, final List<CtClass> classes) {
        for (String dependency : (Collection<String>) clasz.getRefClasses()) {
            final ClassPool classPool = clasz.getClassPool();
            try {
                classPool.get(dependency);
            } catch (javassist.NotFoundException e) {
                classes.add(classPool.makeClass(dependency));
            }
        }
    }

}
