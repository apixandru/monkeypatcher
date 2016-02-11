package com.github.apixandru.utils.monkeypatcher.patchall;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 11, 2016
 */
class PatchAllConfig {

    final List<String> includes;
    final List<String> excludes;

    PatchAllConfig(final List<String> includes, final List<String> excludes) {
        this.includes = Collections.unmodifiableList(includes);
        this.excludes = Collections.unmodifiableList(excludes);
    }

}
