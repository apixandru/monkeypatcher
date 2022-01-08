package com.apixandru.monkeypatcher.patchers;

import com.apixandru.monkeypatcher.util.Log;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

public abstract class AbstractObjectWebPatcher<C extends Config> implements ClassFileTransformer {

    protected final Log log = Log.forClass(getClass());
    protected final C config;

    public AbstractObjectWebPatcher(C config) {
        this.config = config;
    }

    @Override
    public final byte[] transform(ClassLoader loader, String className, Class clazz, ProtectionDomain domain, byte[] bytes) {
        if (config.shouldPatch(className)) {
            log.info("Patching class " + className);
            return patchClass(className, bytes);
        }
        return bytes;
    }

    private byte[] patchClass(String className, byte[] bytes) {
        ClassNode node = new ClassNode(ASM5);
        try {
            ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);
            patchMethods(node, className);
        } catch (Exception e) {
            log.error("Failed to patch class " + className, e);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private void patchMethods(ClassNode node, String className) {
        for (MethodNode methodNode : node.methods) {
            String methodName = methodNode.name + methodNode.desc;
            if (config.shouldPatch(className, methodName)) {
                try {
                    log.info("Patching class " + methodName);
                    doPatchMethod(methodNode, methodName, className);
                } catch (Exception ex) {
                    log.error("Failed to patch method " + methodName, ex);
                    throw ex;
                }
            }
        }
    }

    protected abstract void doPatchMethod(MethodNode methodNode, String methodName, String className);

}
