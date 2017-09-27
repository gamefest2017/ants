package com.ibm.sk.engine;

import static com.ibm.sk.WorldConstans.FILE_NAME;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ibm.sk.dto.Step;

public final class SerializationUtil {

    private SerializationUtil() {
    }

    public static List<Step> deserialize() throws IOException, ClassNotFoundException {
        List<Step> steps = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_NAME); ObjectInputStream ois = new ObjectInputStream(fis)) {
            steps.addAll((List) ois.readObject());
            ois.close();
        }
        return steps;
    }

    public static void serialize(List<Step> step) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(step);
        }
    }
}
