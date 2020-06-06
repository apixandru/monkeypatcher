package com.apixandru.monkeypatcher;

import com.apixandru.monkeypatcher.patchers.MonkeyPatcherConfig;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static com.apixandru.monkeypatcher.util.Utils.loadConfig;
import static com.apixandru.monkeypatcher.util.Utils.loadConfigFile;

public final class Premain {

    public static void premain(String configFilePath, Instrumentation instrumentation) throws IOException {
        File configFile = loadConfigFile(configFilePath);
        if (null == configFile) {
            System.err.println("No valid javaagent argument detected!");
            System.exit(1);
            return;
        }
        MonkeyPatcherConfig config = loadConfig(configFile);
        new MonkeyPatcher(config, instrumentation)
                .setup();
    }


}
