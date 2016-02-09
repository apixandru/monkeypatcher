package com.github.apixandru.utils.monkeypatcher;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
abstract class AbstractMonkeyPatcher<C> implements ClassFileTransformer {

    protected final C config;

    AbstractMonkeyPatcher(final C config) {
        this.config = config;
    }

}
