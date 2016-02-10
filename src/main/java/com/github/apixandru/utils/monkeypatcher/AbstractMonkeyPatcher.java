package com.github.apixandru.utils.monkeypatcher;

import java.lang.instrument.ClassFileTransformer;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public abstract class AbstractMonkeyPatcher<C> implements ClassFileTransformer {

    public final C config;
    protected final Log log;

    public AbstractMonkeyPatcher(final Log log, final C config) {
        this.log = log;
        this.config = config;
    }

}
