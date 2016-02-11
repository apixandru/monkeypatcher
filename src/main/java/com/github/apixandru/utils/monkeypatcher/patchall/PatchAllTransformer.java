package com.github.apixandru.utils.monkeypatcher.patchall;

import com.github.apixandru.utils.monkeypatcher.AbstractMonkeyPatcher;
import com.github.apixandru.utils.monkeypatcher.ConfiguredBy;

import java.security.ProtectionDomain;

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
        return bytes;
    }

}
