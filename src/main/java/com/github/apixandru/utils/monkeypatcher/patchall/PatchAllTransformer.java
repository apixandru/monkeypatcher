package com.github.apixandru.utils.monkeypatcher.patchall;

import com.github.apixandru.utils.monkeypatcher.AbstractMonkeyPatcher;
import com.github.apixandru.utils.monkeypatcher.ConfiguredBy;
import com.github.apixandru.utils.monkeypatcher.Log;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import org.slf4j.instrumentation.JavassistHelper;

import java.io.ByteArrayInputStream;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static com.github.apixandru.utils.JavassistUtil.mockDependencies;

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
            final CtClass clasz = pool.makeClass(new ByteArrayInputStream(bytes));
            classes.add(clasz);
            mockDependencies(clasz, pool, classes);
            if (!clasz.isInterface()) {
                for (final CtBehavior behavior : clasz.getDeclaredBehaviors()) {
                    if (!behavior.isEmpty()) {
                        patch(behavior);
                    }
                }
                return clasz.toBytecode();
            }
        } catch (Exception e) {
            Log.error("Failed to patch " + name, e);
            System.exit(1);
        } finally {
            classes.forEach(CtClass::detach);
        }

        return bytes;
    }

    private void patch(final CtBehavior behavior) throws NotFoundException, CannotCompileException {

        final String className = behavior.getDeclaringClass().getName();
        final String signature = JavassistHelper.getSignature(behavior);
        final String returnValue = JavassistHelper.returnValue(behavior);

        behavior.insertBefore(Log.class.getName() + ".info(\">> " + className + " " + signature + "\");");
        behavior.insertAfter(Log.class.getName() + ".info(\"<< " + className + " " + signature + returnValue + "\");");
    }

}
