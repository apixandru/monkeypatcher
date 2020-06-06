package com.apixandru.monkeypatcher.patchers;

public abstract class Config {

    public boolean enabled = true;
    public boolean logActions = false;

    protected void afterPropertiesSet() {
    }

    public boolean isUseful() {
        return true;
    }

    protected abstract boolean shouldPatch(String className);

    public boolean shouldPatch(String className, String methodName) {
        return true;
    }

}
