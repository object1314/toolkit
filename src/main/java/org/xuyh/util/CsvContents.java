/*
 * Copyright (c) 2023-2023 XuYanhang.
 */

package org.xuyh.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.xuyh.io.UncloseProxyInputStream;
import org.xuyh.io.UncloseProxyOutputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Tools to read or write CSV content.
 *
 * @author XuYanhang
 * @since 2023-05-03
 */
public final class CsvContents {
    /**
     * Reads CSV from stream.
     *
     * @param consumer consumer to cost read content
     * @param in input stream contains CSV content
     * @param charset content charset
     * @throws IOException Exception on any IO error
     */
    public static void read(Consumer<String[]> consumer, InputStream in, Charset charset) throws IOException {
        try (InputStream pin = new UncloseProxyInputStream(in); // Do not close the input stream
             InputStreamReader isr = new InputStreamReader(pin, charset); // Reader for input stream
             BufferedReader br = new BufferedReader(isr); // Buffered reader
             CSVReader reader = new CSVReaderBuilder(br).build()) {
            for (String[] line : reader) {
                consumer.accept(line);
            }
        }
    }

    /**
     * Writes CSV to stream.
     *
     * @param content content supplier
     * @param out output stream to write CSV
     * @param charset content charset
     * @throws IOException Exception on any IO error
     */
    public static void write(Iterator<String[]> content, OutputStream out, Charset charset) throws IOException {
        try (OutputStream pout = new UncloseProxyOutputStream(out); // Do not close the output stream
             OutputStreamWriter osw = new OutputStreamWriter(pout, charset); // Writer for output stream
             BufferedWriter bw = new BufferedWriter(osw); // Buffered writer
             ICSVWriter writer = new CSVWriterBuilder(bw).build()) {
            while (content.hasNext()) {
                writer.writeNext(content.next());
            }
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private CsvContents() {
        super();
    }
}
