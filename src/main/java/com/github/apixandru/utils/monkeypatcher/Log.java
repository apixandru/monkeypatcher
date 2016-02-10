package com.github.apixandru.utils.monkeypatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 10, 2016
 */
public final class Log implements AutoCloseable {

    private final FileWriter writer;

    public Log(final String file) throws IOException {
        this.writer = new FileWriter(file);
    }

    public void info(final String info) {
        writeString("INFO: " + info + "\n");
    }

    public void error(final String message, final Exception exception) {
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

    private synchronized void writeString(String string) {
        try {
            System.out.print(string);
            this.writer.write(string);
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.writer.close();
    }

}