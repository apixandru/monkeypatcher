package com.github.apixandru.utils.monkeypatcher;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import org.slf4j.instrumentation.JavassistHelper;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class Patcher implements ClassFileTransformer {

    private final MonkeyConfig config;

    /**
     * @param config
     */
    public Patcher(final MonkeyConfig config) {
        this.config = config;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return new byte[0];
    }
}
