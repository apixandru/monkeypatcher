package com.apixandru.monkeypatcher.util;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

public class JavassistUtil {

    public static CtClass makeClass(byte[] bytes) throws IOException {
        try (InputStream stream = new ByteArrayInputStream(bytes)) {
            return ClassPool.getDefault()
                    .makeClass(stream);
        }
    }

    public static void mockDependencies(CtClass ctClass, List<CtClass> classes) {
        ClassPool classPool = ctClass.getClassPool();
        for (String dependency : ctClass.getRefClasses()) {
            mockDependency(classPool, dependency)
                    .ifPresent(classes::add);
        }
    }

    private static Optional<CtClass> mockDependency(ClassPool classPool, String dependency) {
        try {
            classPool.get(dependency);
            return empty();
        } catch (javassist.NotFoundException e) {
            return Optional.of(classPool.makeClass(dependency));
        }
    }

}
