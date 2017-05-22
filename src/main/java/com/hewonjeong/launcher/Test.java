package com.hewonjeong.launcher;


import com.hewonjeong.api.NaverSearchApi;
import com.hewonjeong.util.FileManager;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        final String QUERY = "제주 여행";
        final int DISPLAY = 100;
        final int START = 1;
        final String SORT_SIM = "sim";
        final String[] TARGET_DOMAINS = {"blog", "local", "kin", "news"};
        for (String s : TARGET_DOMAINS) {
            JSONObject obj = NaverSearchApi.request(s, QUERY, DISPLAY , START, SORT_SIM);
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String to = transFormat.format(new Date());
            FileManager.writeFile(obj, String.format("%s(%s).json", s, to));
        }
    }
}
