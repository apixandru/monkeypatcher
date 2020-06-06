package com.apixandru.monkeypatcher.patchers.logexecution;

import com.apixandru.monkeypatcher.patchers.MultipleClassesConfig;

public class LogExecutionsConfig extends MultipleClassesConfig {

    public boolean logEntry = true;
    public boolean logExit = true;
    public boolean includeParams;
    public boolean includeReturnValues;

}
