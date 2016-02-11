package com.github.apixandru.utils.monkeypatcher.patchall;

import com.github.apixandru.utils.monkeypatcher.AbstractMonkeyPatcher;
import com.github.apixandru.utils.monkeypatcher.ConfiguredBy;
import com.github.apixandru.utils.monkeypatcher.Log;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 11, 2016
 */
@ConfiguredBy(PatchAllConfigParser.class)
class PatchAllTransformer extends AbstractMonkeyPatcher<PatchAllConfig> {

    PatchAllTransformer(final PatchAllConfig config) {
        super(config);
    }

    public byte[] transform(final ClassLoader loader, final String className, final Class clazz,
                            final ProtectionDomain domain, final byte[] bytes) {

        if (!config.includes.isEmpty()) {
            for (String include : config.includes) {

                if (className.startsWith(include)) {
                    return patch(className, bytes);
                }
            }
            return bytes;
        }

        for (final String exclude : config.excludes) {
            if (className.startsWith(exclude)) {
                return bytes;
            }

        }

        return patch(className, bytes);
    }

    private byte[] patch(final String name, final byte[] bytes) {
        final ClassPool pool = ClassPool.getDefault();
        final List<CtClass> classes = new ArrayList<>();

        try {
            final CtClass ctClass = pool.makeClass(new ByteArrayInputStream(bytes));
            classes.add(ctClass);
            if (!ctClass.isInterface()) {
                for (final CtBehavior behavior : ctClass.getDeclaredBehaviors()) {
                    if (!behavior.isEmpty()) {
                        patch(behavior);
                    }
                }
                return ctClass.toBytecode();
            }
        } catch (Exception e) {
            Log.error("Failed to patch class", e);
        } finally {
            classes.forEach(CtClass::detach);
        }

        return bytes;
    }

    private void patch(final CtBehavior behavior) {

    }

}
