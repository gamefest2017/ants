package com.ibm.sk.ff.gui.common.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class FileHelper {
    private FileHelper() {
    }

    public static void write(String json) {
        try {
            FileWriter writer = new FileWriter("MyFile.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Failed to write to the file!" + e);
        }

    }

    public static String read() {
        String fileContent = "";
        try {
            FileReader reader = new FileReader("MyFile.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent += line;
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Failed to read from the file!" + e);
        }
        return fileContent;
    }
}
