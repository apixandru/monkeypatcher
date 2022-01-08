package com.apixandru.monkeypatcher.patchers.quickreturn;

import com.apixandru.monkeypatcher.patchers.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apixandru.monkeypatcher.util.Utils.normalizeClassName;
import static java.util.Collections.emptyList;

public class QuickReturnConfig extends Config {

    private final Map<String, List<String>> overrides = new HashMap<>();

    @Override
    protected boolean shouldPatch(String className) {
        return overrides.containsKey(className);
    }

    @Override
    public boolean shouldPatch(String className, String methodName) {
        System.out.println(overrides);
        return overrides.get(className)
                .contains(methodName);
    }

    public void setOverrides(List<QuickReturnConfig.ClassOverride> overrides) {
        if (overrides == null) {
            overrides = emptyList();
        }
        this.overrides.clear();
        for (QuickReturnConfig.ClassOverride classOverride : overrides) {
            String className = classOverride.className;
            String normalizedName = normalizeClassName(className);
            List<String> methods = this.overrides.computeIfAbsent(normalizedName, key -> new ArrayList<>());
            for (String method : classOverride.methods) {
                methods.add(normalizeClassName(method));
            }
        }
    }

    public static class ClassOverride {
        public String className;
        public List<String> methods;
    }

}
