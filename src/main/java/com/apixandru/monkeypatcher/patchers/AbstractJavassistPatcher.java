package com.apixandru.monkeypatcher.patchers;

import com.apixandru.monkeypatcher.util.Log;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static com.apixandru.monkeypatcher.util.JavassistUtil.makeClass;
import static com.apixandru.monkeypatcher.util.JavassistUtil.mockDependencies;

public abstract class AbstractJavassistPatcher<C extends Config> implements ClassFileTransformer {

    protected final Log log = Log.forClass(getClass());
    protected final C config;

    public AbstractJavassistPatcher(C config) {
        this.config = config;
    }

    @Override
    public final byte[] transform(ClassLoader loader, String className, Class clazz, ProtectionDomain domain, byte[] bytes) {
        if (config.shouldPatch(className)) {
            if (config.logActions) {
                log.info("Patching class " + className);
            }
            return patchClass(className, bytes);
        }
        if (config.logActions) {
            log.info("Skipping class " + className);
        }
        return bytes;
    }

    private byte[] patchClass(String className, byte[] bytes) {
        List<CtClass> dependencies = new ArrayList<>();
        try {
            CtClass ctClass = makeClass(bytes);
            dependencies.add(ctClass);
            if (!ctClass.isInterface()) {
                return patchMethods(ctClass, className, dependencies);
            }
        } catch (Exception e) {
            log.error("Failed to patch class " + className, e);
        } finally {
            dependencies.forEach(CtClass::detach);
        }

        return bytes;
    }

    private byte[] patchMethods(CtClass ctClass, String className, List<CtClass> dependencies)
            throws NotFoundException, CannotCompileException, IOException {

        mockDependencies(ctClass, dependencies);
        for (final CtBehavior method : ctClass.getDeclaredBehaviors()) {
            String methodName = method.getLongName();
            if (!method.isEmpty() && config.shouldPatch(className, methodName)) {
                try {
                    doPatchMethod(method, methodName, className);
                } catch (Exception ex) {
                    log.error("Failed to patch method " + methodName, ex);
                    throw ex;
                }
            }
        }
        return ctClass.toBytecode();
    }

    protected abstract void doPatchMethod(CtBehavior method, String methodName, String className)
            throws NotFoundException, CannotCompileException;

}
