package com.github.apixandru.utils.monkeypatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 10, 2016
 */
public final class Log {

    // safe because it's used in a synchronized manner
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss ");

    private static FileWriter writer;

    public static synchronized void setFile(final String file) throws IOException {
        if (null != writer) {
            writer.close();
        }
        writer = new FileWriter(file);
    }

    public static void info(final String info) {
        writeString("INFO: " + info + "\n");
    }

    public static void error(final String message, final Exception exception) {
        try (final StringWriter out = new StringWriter();
             final PrintWriter pw = new PrintWriter(out)) {
            pw.write("ERROR: " + message + "\n");
            exception.printStackTrace(pw);
            pw.write("\n");
            writeString(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void writeString(final String string) {
        final String output = sdf.format(new Date()) + string;
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

}
