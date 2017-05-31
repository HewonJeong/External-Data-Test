package com.hewonjeong.util;

import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileManagerTest {
    final String testObjStr = "{\"testKey\":[{\"k1\":\"v1\"},{\"k2\":\"v2\"}]}";
    final JSONObject testObj = new JSONObject(testObjStr);
    final String fileName = "test.json";

    @Test
    public void testWriteFile_ValidCase_ShouldPass() throws IOException{
        boolean res = FileManager.writeFile(testObj, fileName);
        assertTrue(res);
        StringBuilder sb = new StringBuilder();
        File f = new File(fileName);
        BufferedReader buf = new BufferedReader(new FileReader(f));
        String line;
        while ((line = buf.readLine()) != null) {
            sb.append(line);
        }
        assertEquals(testObjStr, sb.toString());
    }

    @Test
    public void testWriteFile_invalidDir_ShouldPass() {
        final String invalidDir = "INVALID_DIR/";
        boolean res = FileManager.writeFile(testObj, invalidDir + fileName);
        assertFalse(res);
    }
}
