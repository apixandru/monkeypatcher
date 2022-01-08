package com.apixandru.monkeypatcher.patchers.alterexecution;

import com.apixandru.monkeypatcher.patchers.AbstractJavassistPatcher;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import static com.apixandru.monkeypatcher.util.Utils.containsText;

public final class AlterExecutions extends AbstractJavassistPatcher<AlterExecutionsConfig> {

    public AlterExecutions(final AlterExecutionsConfig config) {
        super(config);
    }

    @Override
    protected void doPatchMethod(CtBehavior method, String methodName, String className) throws CannotCompileException {
        log.info("Patching " + methodName);

        MethodOverride overrides = config.getOverridesFor(className, methodName);
        if (containsText(overrides.body)) {
            method.setBody(overrides.body);
        }
        if (containsText(overrides.before)) {
            method.insertBefore(overrides.before);
        }
        if (containsText(overrides.after)) {
            method.insertAfter(overrides.after);
        }

    }

}
