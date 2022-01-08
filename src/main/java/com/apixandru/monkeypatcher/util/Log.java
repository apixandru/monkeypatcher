package com.apixandru.monkeypatcher.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.System.currentTimeMillis;

public final class Log {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");

    private static final StringBuilder buffer = new StringBuilder();

    private static FileWriter writer;
    private final String tag;

    Log(String tag) {
        this.tag = tag;
    }

    public static void initLoggingToFile(File configFile) throws IOException {
        String configFileName = configFile.getName();
        File absoluteFile = configFile.getAbsoluteFile();
        File parentFile = absoluteFile.getParentFile();
        File logs = new File(parentFile, "logs");
        logs.mkdirs();
        String logExtension = "." + currentTimeMillis() + ".log";
        File logFile = new File(logs, configFileName.replace(".yml", logExtension));
        writer = new FileWriter(logFile);
    }

    public static Log forClass(Class<?> logClass) {
        return new Log(logClass.getSimpleName());
    }

    @Deprecated // do not use, for runtime logs only!
    public static void info(String tag, String info) {
        writeString(tag, "INFO", info + "\n");
    }

    private static void error(String tag, String message, final Exception exception) {
        try (StringWriter out = new StringWriter();
             PrintWriter pw = new PrintWriter(out)) {
            pw.write(message + "\n");
            exception.printStackTrace(pw);
            pw.write("\n");
            writeString(tag, "ERROR", out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void writeString(String tag, String logType, String string) {
        buffer.setLength(0);
        buffer.append(dtf.format(LocalDateTime.now()))
                .append(" [")
                .append(logType)
                .append("] ")
                .append(tag)
                .append(": ")
                .append(string);

        String output = buffer.toString();
        System.out.print(output);
        try {
            if (null != writer) {
                writer.write(output);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(String info) {
        info(tag, info);
    }

    public void error(String message, final Exception exception) {
        error(tag, message, exception);
    }

    public String logInfoAtRuntime(String info) {
        return "System.out.println(\"TAGGED: " + tag + " <->" + info + "\");";
//        return Log.class.getName() + ".info(\"" + tag + "\",\" " + info + "\");";
    }
}
