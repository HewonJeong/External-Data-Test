package com.hewonjeong.util;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static boolean writeFile(JSONObject obj, String fileName) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, false));
            fileWriter.write(obj.toString());
            fileWriter.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
