package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.monkeypatcher.MonkeyConfig.ClassToPatch;
import com.github.apixandru.utils.monkeypatcher.MonkeyConfig.MethodToPatch;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
final class Patcher implements ClassFileTransformer {

    private final MonkeyConfig config;

    /**
     * @param config
     */
    public Patcher(final MonkeyConfig config) {
        this.config = config;
    }

    @Override
    public byte[] transform(final ClassLoader loader, final String className, final Class clazz,
                            final ProtectionDomain domain, final byte[] bytes) {

        final ClassToPatch classToPatch = config.classes.get(className);
        if (null != classToPatch) {
            return patch(classToPatch, bytes);
        }
        return bytes;
    }

    private byte[] patch(final ClassToPatch clazz, final byte[] bytes) {
        final ClassPool pool = ClassPool.getDefault();
        final List<CtClass> classes = new ArrayList<>();
        try {
            final CtClass ctClass = pool.makeClass(new ByteArrayInputStream(bytes));
            classes.add(ctClass);
            final ClassPool classPool = ctClass.getClassPool();
            for (String stub : clazz.stubs) {
                classes.add(classPool.makeClass(stub));
            }

            for (final CtBehavior ctMethod : ctClass.getDeclaredBehaviors()) {
                final MethodToPatch methodToPatch = clazz.methods.get(ctMethod.getLongName());
                if (null != methodToPatch) {
                    ctMethod.setBody(String.format("{%s}", methodToPatch.body));
                }
            }
            return ctClass.toBytecode();

        } catch (final CannotCompileException | IOException e) {
            e.printStackTrace();
            return bytes;
        } finally {
            classes.forEach(CtClass::detach);
        }
    }

}
