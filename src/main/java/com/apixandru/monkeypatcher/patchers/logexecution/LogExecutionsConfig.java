package com.apixandru.monkeypatcher.patchers.logexecution;

import com.apixandru.monkeypatcher.patchers.MultipleClassesConfig;

public class LogExecutionsConfig extends MultipleClassesConfig {

    public boolean logEntry = true;
    public boolean logExit = true;
    public boolean includeParams;
    public boolean includeReturnValues;

    @Override
    public boolean shouldPatch(String className, String methodName) {
        if (!onlyIncludeMethods.isEmpty()) {
            return onlyIncludeMethods.contains(methodName);
        }
        if (skipMethods.contains(methodName)) {
            return false;
        }
        System.out.println(methodName);
        return super.shouldPatch(className, methodName);
    }

}
