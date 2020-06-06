package com.apixandru.monkeypatcher.patchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MultipleClassesConfigBuilder {
    private boolean includeAll;
    private Boolean enabled;
    private Boolean logActions;
    private Set<String> excludeNameEquals;
    private Set<String> excludeNameStartsWith;
    private Set<String> includeNameEquals;
    private Set<String> includeNameStartsWith;

    public MultipleClassesConfigBuilder setIncludeAll(boolean includeAll) {
        this.includeAll = includeAll;
        return this;
    }

    public MultipleClassesConfigBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public MultipleClassesConfigBuilder setLogActions(boolean logActions) {
        this.logActions = logActions;
        return this;
    }

    public MultipleClassesConfigBuilder setExcludeNameEquals(Set<String> excludeNameEquals) {
        this.excludeNameEquals = excludeNameEquals;
        return this;
    }

    public MultipleClassesConfigBuilder setExcludeNameStartsWith(String... excludeNameStartsWith) {
        this.excludeNameStartsWith = new HashSet<>(Arrays.asList(excludeNameStartsWith));
        return this;
    }

    public MultipleClassesConfigBuilder setIncludeNameEquals(Set<String> includeNameEquals) {
        this.includeNameEquals = includeNameEquals;
        return this;
    }

    public MultipleClassesConfigBuilder setIncludeNameStartsWith(Set<String> includeNameStartsWith) {
        this.includeNameStartsWith = includeNameStartsWith;
        return this;
    }

    public MultipleClassesConfig build() {
        MultipleClassesConfig config = new MultipleClassesConfig();
        setValue(enabled, value -> config.enabled = value);
        setValue(logActions, value -> config.logActions = value);
        setValue(includeAll, value -> config.includeAll = value);
        config.excludeNameEquals = excludeNameEquals;
        config.excludeNameStartsWith = excludeNameStartsWith;
        config.includeNameEquals = includeNameEquals;
        config.includeNameStartsWith = includeNameStartsWith;
        config.afterPropertiesSet();
        return config;
    }

    private void setValue(Boolean value, Consumer<Boolean> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
