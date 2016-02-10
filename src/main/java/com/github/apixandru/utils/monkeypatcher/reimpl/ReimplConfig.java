package com.github.apixandru.utils.monkeypatcher.reimpl;

import java.util.Collections;
import java.util.Map;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class ReimplConfig {

    public final Map<String, ClassToPatch> classes;

    public ReimplConfig(final Map<String, ClassToPatch> classes) {
        this.classes = Collections.unmodifiableMap(classes);
    }

}
