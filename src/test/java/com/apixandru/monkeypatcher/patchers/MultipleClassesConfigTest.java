package com.apixandru.monkeypatcher.patchers;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultipleClassesConfigTest {

    @Test
    public void test1() {
        MultipleClassesConfig config = new MultipleClassesConfigBuilder()
                .setIncludeAll(true)
                .setExcludeNameStartsWith("java", "javax", "sun", "jdk", "com.sun")
                .build();

        assertThat(config.shouldPatch("java/util/IdentityHashMap"))
                .isFalse();
    }

}
