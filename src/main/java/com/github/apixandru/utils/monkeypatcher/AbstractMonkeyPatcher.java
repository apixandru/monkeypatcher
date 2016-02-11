package com.github.apixandru.utils.monkeypatcher;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public abstract class AbstractMonkeyPatcher<C> implements ClassFileTransformer {

    public final C config;

    public AbstractMonkeyPatcher(final C config) {
        this.config = config;
    }

}
