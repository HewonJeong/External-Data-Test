package com.hewonjeong.launcher;


import com.hewonjeong.api.NaverSearchApi;
import com.hewonjeong.util.FileManager;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        final String[] TARGET_DOMAINS = {"blog", "local", "kin", "news"};
        for (String s : TARGET_DOMAINS) {
            JSONObject obj = NaverSearchApi.request(s, "제주 여행", 100, 1, "sim");
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String to = transFormat.format(new Date());
            FileManager.writeFile(obj, String.format("%s(%s).json", s, to));
        }
    }
}
