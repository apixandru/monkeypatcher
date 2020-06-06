package com.apixandru.monkeypatcher.patchers;

import java.util.HashSet;
import java.util.Set;

import static com.apixandru.monkeypatcher.util.Utils.convertClasses;

public class MultipleClassesConfig extends Config {

    public boolean includeAll;
    public Set<String> excludeNameEquals = new HashSet<>();
    public Set<String> excludeNameStartsWith = new HashSet<>();
    public Set<String> includeNameEquals = new HashSet<>();
    public Set<String> includeNameStartsWith = new HashSet<>();

    @Override
    public boolean isUseful() {
        return includeAll
                || !excludeNameEquals.isEmpty()
                || !excludeNameStartsWith.isEmpty()
                || !includeNameEquals.isEmpty()
                || !includeNameStartsWith.isEmpty();
    }

    @Override
    protected boolean shouldPatch(String className) {
        if (includeNameEquals.contains(className)) {
            return true;
        } else if (excludeNameEquals.contains(className)) {
            return false;
        }
        String includeCandidate = findMatchingCandidate(className, includeNameStartsWith);
        String excludeCandidate = findMatchingCandidate(className, excludeNameStartsWith);
        if (includeCandidate != null) {
            if (excludeCandidate == null) {
                return true;
            }
            return includeCandidate.length() > excludeCandidate.length();
        }
        return excludeCandidate == null && includeAll;
    }

    private String findMatchingCandidate(String className, Set<String> candidates) {
        return candidates.stream()
                .filter(className::startsWith)
                .findAny()
                .orElse(null);
    }

    @Override
    protected void afterPropertiesSet() {
        this.excludeNameEquals = convertClasses(this.excludeNameEquals);
        this.excludeNameStartsWith = convertClasses(this.excludeNameStartsWith);
        this.includeNameEquals = convertClasses(this.includeNameEquals);
        this.includeNameStartsWith = convertClasses(this.includeNameStartsWith);
    }

}
