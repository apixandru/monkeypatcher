package com.apixandru.monkeypatcher;

import com.apixandru.monkeypatcher.patchers.Config;
import com.apixandru.monkeypatcher.patchers.MonkeyPatcherConfig;
import com.apixandru.monkeypatcher.patchers.alterexecution.AlterExecutions;
import com.apixandru.monkeypatcher.patchers.inspectclasses.InspectClasses;
import com.apixandru.monkeypatcher.patchers.logexecution.LogExecutions;
import com.apixandru.monkeypatcher.patchers.quickreturn.QuickReturn;
import com.apixandru.monkeypatcher.util.Log;
import com.apixandru.monkeypatcher.util.NoSystemExitSecurityManager;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.function.Function;

public class MonkeyPatcher {

    private final Log log = Log.forClass(MonkeyPatcher.class);

    private final MonkeyPatcherConfig config;
    private final Instrumentation instrumentation;

    public MonkeyPatcher(MonkeyPatcherConfig config, Instrumentation instrumentation) {
        this.config = config;
        this.instrumentation = instrumentation;

        if (config.overrideSecurityManager) {
            System.setSecurityManager(new NoSystemExitSecurityManager());
        }
    }

    public void setup() {
        initInspectClasses(InspectClasses.class, config.inspectClasses, InspectClasses::new);
        initInspectClasses(AlterExecutions.class, config.alterExecutions, AlterExecutions::new);
        initInspectClasses(LogExecutions.class, config.logExecutions, LogExecutions::new);
        initInspectClasses(QuickReturn.class, config.quickReturn, QuickReturn::new);
    }

    private <C extends Config> void initInspectClasses(Class<?> what, C config, Function<C, ClassFileTransformer> initializer) {
        if (config == null || !config.isUseful()) {
            if (this.config.logActions) {
                log.info(what + " not configured.");
            }
            return;
        }
        if (!config.enabled) {
            if (this.config.logActions) {
                log.info(what + " not enabled.");
            }
            return;
        }
        instrumentation.addTransformer(initializer.apply(config));
    }

}
