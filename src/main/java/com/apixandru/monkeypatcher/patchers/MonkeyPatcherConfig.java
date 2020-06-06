package com.apixandru.monkeypatcher.patchers;

import com.apixandru.monkeypatcher.patchers.alterexecution.AlterExecutionsConfig;
import com.apixandru.monkeypatcher.patchers.logexecution.LogExecutionsConfig;

import java.util.Objects;
import java.util.stream.Stream;

public final class MonkeyPatcherConfig {

    public boolean overrideSecurityManager;
    public boolean logToFile;

    public MultipleClassesConfig inspectClasses;
    public LogExecutionsConfig logExecutions;
    public AlterExecutionsConfig alterExecutions;

    public void markInitialized() {
        Stream.of(inspectClasses, logExecutions, alterExecutions)
                .filter(Objects::nonNull)
                .forEach(Config::afterPropertiesSet);
    }

}
