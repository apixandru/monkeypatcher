package com.apixandru.monkeypatcher.util;

import com.apixandru.monkeypatcher.patchers.MonkeyPatcherConfig;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import static com.apixandru.monkeypatcher.util.Log.initLoggingToFile;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

public final class Utils {

    private Utils() {
    }

    public static Set<String> convertClasses(Collection<String> from) {
        if (from == null) {
            return emptySet();
        }
        return from.stream()
                .map(Utils::normalizeClassName)
                .collect(toSet());
    }

    public static String normalizeClassName(String className) {
        return className.replace(".", "/");
    }

    public static boolean containsText(String string) {
        return null != string && !string.isEmpty();
    }

    public static File loadConfigFile(String configFile) {
        if (configFile == null) {
            return null;
        }
        File file = new File(configFile);
        if (file.exists()) {
            return file;
        }
        System.err.println("Provided config file does not exist: " + configFile);
        return null;
    }

    public static MonkeyPatcherConfig loadConfig(File configFile) throws IOException {
        Yaml yaml = new Yaml(new Constructor(MonkeyPatcherConfig.class));
        try (InputStream inputStream = new FileInputStream(configFile)) {
            MonkeyPatcherConfig config = yaml.load(inputStream);
            if (config.logToFile) {
                initLoggingToFile(configFile);
            }
            config.markInitialized();
            return config;
        }
    }

    public static String classMethodName(String className, String methodName) {
        return className + "." + methodName;
    }

}
