package com.github.apixandru.utils.monkeypatcher.reimpl;

import java.util.Collections;
import java.util.Map;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
final class ReimplConfig {

    final Map<String, ClassToPatch> classes;

    ReimplConfig(final Map<String, ClassToPatch> classes) {
        this.classes = Collections.unmodifiableMap(classes);
    }

}
