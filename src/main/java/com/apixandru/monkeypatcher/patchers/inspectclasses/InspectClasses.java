package com.apixandru.monkeypatcher.patchers.inspectclasses;

import com.apixandru.monkeypatcher.patchers.AbstractJavassistPatcher;
import com.apixandru.monkeypatcher.patchers.MultipleClassesConfig;
import javassist.CtBehavior;

public class InspectClasses extends AbstractJavassistPatcher<MultipleClassesConfig> {

    public InspectClasses(MultipleClassesConfig config) {
        super(config);
    }

    @Override
    protected void doPatchMethod(CtBehavior method, String methodName, String className) {
        log.info("Method name " + methodName);
    }

}
