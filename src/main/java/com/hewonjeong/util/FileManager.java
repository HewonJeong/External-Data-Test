package com.hewonjeong.util;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class FileManager {
    public static void writeFile(JSONObject obj, String fileName) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, true));
            fileWriter.write(obj.toString(4));
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
