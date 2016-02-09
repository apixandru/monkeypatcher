package com.github.apixandru.utils.monkeypatcher.reimpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public class ClassToPatch {

    public final String name;
    public final Map<String, MethodToPatch> methods;
    public final List<String> stubs;

    public ClassToPatch(final String name, final Map<String, MethodToPatch> methods, final List<String> stubs) {
        this.name = name;
        this.methods = Collections.unmodifiableMap(methods);
        this.stubs = Collections.unmodifiableList(stubs);
    }

}
