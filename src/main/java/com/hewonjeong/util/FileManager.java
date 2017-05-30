package com.hewonjeong.util;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static void writeFile(JSONObject obj, String fileName) throws IOException{
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName, true));
        fileWriter.write(obj.toString(4));
        fileWriter.flush();
    }
}
