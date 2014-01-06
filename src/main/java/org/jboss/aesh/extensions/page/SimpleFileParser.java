/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.aesh.extensions.page;

import org.jboss.aesh.console.man.FileParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Util method that tries to read a file
 * and prepare it to be displayed in a terminal
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class SimpleFileParser implements FileParser {

    private String pageAsString;
    private String fileName;
    private InputStreamReader reader;

    public SimpleFileParser() {
    }

    /**
     * Read from a specified filename. Also supports gzipped files.
     *
     * @param filename File
     * @throws IOException
     */
    public void setFile(String filename) throws IOException {
        setFile(new File(filename));
    }

    /**
     * Read from a specified file. Also supports gzipped files.
     *
     * @param file File
     * @throws IOException
     */
    public void setFile(File file) throws IOException {
        if(!file.isFile())
            throw new IllegalArgumentException(file+" must be a file.");
        else {
            if(file.getName().endsWith("gz"))
                initGzReader(file);
            else
                initReader(file);
        }
    }

    public void setFile(InputStream inputStream) {
        reader = new InputStreamReader(inputStream);
    }

    /**
     * Read a file resouce located in a jar
     *
     * @param fileName name
     */
    public void setFileFromAJar(String fileName) {
        InputStream is = this.getClass().getResourceAsStream(fileName);
        if(is != null) {
            this.fileName = fileName;
            reader = new InputStreamReader(is);
        }
    }

    private void initReader(File file) throws FileNotFoundException {
        fileName = file.getAbsolutePath();
        reader = new FileReader(file);
    }

    public void readPageAsString(String pageAsString) {
        this.pageAsString = pageAsString;
    }

    @Override
    public String getName() {
        if(fileName != null)
            return fileName;
        else
            return "STREAM";
    }

    private void initGzReader(File file) throws IOException {
        fileName = file.getAbsolutePath();
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file));
        reader = new InputStreamReader(gzip);
    }

    @Override
    public List<String> loadPage(int columns) throws IOException {
        List<String> lines = new ArrayList<String>();
        //read file and save each line in a list
        if(reader != null) {
            BufferedReader br = new BufferedReader(reader);
            try {
                String line = br.readLine();

                while (line != null) {
                    if(line.length() > columns) {
                        //split the line in the size of column
                        for(String s : line.split("(?<=\\G.{"+columns+"})"))
                            lines.add(s);
                    }
                    else
                        lines.add(line);
                    line = br.readLine();
                }
            }
            finally {
                br.close();
            }
        }
        else if(pageAsString != null) {
            for(String s : pageAsString.split("\n")) {
                for(String s2 : s.split("(?<=\\G.{" + columns + "})"))
                    lines.add(s2);
            }
        }

        return lines;
    }

}
