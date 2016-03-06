package com.github.apixandru.utils;

import javassist.ClassPool;
import javassist.CtClass;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since March 06, 2016
 */
public class JavassistUtil {

    @SuppressWarnings("unchecked")
    public static void mockDependencies(final CtClass clasz, final ClassPool classPool, final List<CtClass> classes) {
        for (String dependency : (Collection<String>) clasz.getRefClasses()) {
            try {
                classPool.get(dependency);
            } catch (javassist.NotFoundException e) {
                classes.add(classPool.makeClass(dependency));
            }
        }
    }

}
