package com.github.apixandru.utils.monkeypatcher;

import java.lang.instrument.Instrumentation;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class Premain {

    public static void premain(final String agentArgument, final Instrumentation instrumentation) {
        if (null == agentArgument) {
            System.err.println("Missing javaagent argument!");
            System.exit(1);
        }
        final MonkeyConfig parse = MonkeyConfig.parse(agentArgument);
        System.setProperty("java.util.logging.SimpleFormatter.format", parse.loggingPattern);
        instrumentation.addTransformer(new SimpleMethodBodyReplacer(parse));
    }

}
