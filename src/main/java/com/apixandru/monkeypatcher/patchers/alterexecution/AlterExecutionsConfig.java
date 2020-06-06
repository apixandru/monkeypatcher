package com.apixandru.monkeypatcher.patchers.alterexecution;

import com.apixandru.monkeypatcher.patchers.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apixandru.monkeypatcher.util.Utils.classMethodName;
import static com.apixandru.monkeypatcher.util.Utils.normalizeClassName;
import static java.util.Collections.emptyList;

public class AlterExecutionsConfig extends Config {

    private final Map<String, Map<String, MethodOverride>> overrides = new HashMap<>();

    @Override
    protected boolean shouldPatch(String className) {
        return overrides.containsKey(className);
    }

    @Override
    public boolean shouldPatch(String className, String methodName) {
        return overrides.get(className)
                .containsKey(methodName);
    }

    public MethodOverride getOverridesFor(String className, String methodName) {
        return overrides.get(className)
                .get(methodName);
    }

    public void setOverrides(List<ClassOverride> overrides) {
        if (overrides == null) {
            overrides = emptyList();
        }
        this.overrides.clear();
        for (ClassOverride classOverride : overrides) {
            String className = classOverride.className;
            String normalizedName = normalizeClassName(className);
            Map<String, MethodOverride> methods = this.overrides.computeIfAbsent(normalizedName, key -> new HashMap<>());
            for (MethodOverride method : classOverride.methods) {
                String fullInvocationName = classMethodName(className, method.name);
                methods.put(fullInvocationName, method);
            }
        }
    }

    public static class ClassOverride {
        public String className;
        public List<MethodOverride> methods;
    }

}
