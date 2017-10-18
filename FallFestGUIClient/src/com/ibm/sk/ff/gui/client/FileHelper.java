package com.ibm.sk.ff.gui.client;

import static com.ibm.sk.ff.gui.common.mapper.Mapper.INSTANCE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.ibm.sk.ff.gui.common.objects.operations.Step;

public final class FileHelper {
    private FileHelper() {
    }

    private static final String FILE_NAME= "ants.txt";

    public static void write(List<Step> steps) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(INSTANCE.pojoToJson(steps));
        } catch (IOException e) {
            System.out.println("Failed to write to the file!" + e);
        }
    }

    public static List<Step> read() {
        String fileContent = "";
        try(FileReader reader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent += line;
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Failed to read from the file!" + e);
        }
        return INSTANCE.jsonToPojo(fileContent, List.class);
    }
}