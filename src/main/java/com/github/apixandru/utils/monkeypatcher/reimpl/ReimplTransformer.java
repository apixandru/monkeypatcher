package com.github.apixandru.utils.monkeypatcher.reimpl;

import com.github.apixandru.utils.StringUtil;
import com.github.apixandru.utils.monkeypatcher.AbstractMonkeyPatcher;
import com.github.apixandru.utils.monkeypatcher.ConfiguredBy;
import com.github.apixandru.utils.monkeypatcher.Log;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.IOException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static com.github.apixandru.utils.JavassistUtil.makeClass;
import static com.github.apixandru.utils.JavassistUtil.mockDependencies;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
@ConfiguredBy(ReimplConfigParser.class)
final class ReimplTransformer extends AbstractMonkeyPatcher<ReimplConfig> {

    /**
     * @param config
     */
    ReimplTransformer(final ReimplConfig config) {
        super(config);
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
        final List<CtClass> classes = new ArrayList<>();
        try {
            final CtClass clasz = makeClass(bytes);
            classes.add(clasz);
            mockDependencies(clasz, classes);
            for (final CtBehavior ctMethod : clasz.getDeclaredBehaviors()) {
                final MethodToPatch methodToPatch = clazz.methods.get(ctMethod.getLongName());
                if (null != methodToPatch) {
                    Log.info("Patching " + ctMethod.getLongName());
                    if (!StringUtil.isEmpty(methodToPatch.body)) {
                        ctMethod.setBody(methodToPatch.body);
                    }
                    if (!StringUtil.isEmpty(methodToPatch.before)) {
                        ctMethod.insertBefore(methodToPatch.before);
                    }
                    if (!StringUtil.isEmpty(methodToPatch.after)) {
                        ctMethod.insertAfter(methodToPatch.after);
                    }
                }
            }
            return clasz.toBytecode();

        } catch (final CannotCompileException | IOException e) {
            Log.error("Failed to patch class", e);
            return bytes;
        } finally {
            classes.forEach(CtClass::detach);
        }
    }

}
