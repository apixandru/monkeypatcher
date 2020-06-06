package com.apixandru.monkeypatcher.patchers.logexecution;

import com.apixandru.monkeypatcher.patchers.AbstractPatcher;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

import static org.slf4j.instrumentation.JavassistHelper.getSignature;
import static org.slf4j.instrumentation.JavassistHelper.returnValue;

public class LogExecutions extends AbstractPatcher<LogExecutionsConfig> {

    public LogExecutions(LogExecutionsConfig config) {
        super(config);
    }

    @Override
    protected void doPatchMethod(CtBehavior method, String methodName, String normalizedClassName)
            throws NotFoundException, CannotCompileException {

        String executingMethodName = getExecutingMethodName(method, methodName);
        if (config.logEntry) {
            method.insertBefore(log.logInfoAtRuntime(">> " + executingMethodName));
        }
        if (config.logExit) {
            String returnValue = extractReturnValue(method);
            method.insertAfter(log.logInfoAtRuntime("<< " + executingMethodName + returnValue));
        }
    }

    private String extractReturnValue(CtBehavior method) throws NotFoundException {
        if (config.includeReturnValues) {
            return returnValue(method);
        }
        return "";
    }

    protected String getExecutingMethodName(CtBehavior method, String methodName) throws NotFoundException {
        if (!config.includeParams) {
            return methodName;
        }
        CtClass declaringClass = method.getDeclaringClass();
        String className = declaringClass.getName();
        return className + " " + getSignature(method);
    }


}
