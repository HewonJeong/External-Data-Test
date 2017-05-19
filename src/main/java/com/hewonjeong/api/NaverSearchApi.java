package com.hewonjeong.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverSearchApi {
    public static final String BASE_URL = "https://openapi.naver.com/v1/search/";
    public static final int MAX_ITEM_LENGTH = 1000;

    public static JSONObject request(String domain, String query, int display, int start, String sort) {
        if (display < 10 || display > 100)
            throw new IllegalArgumentException("Display must be between 10 to 100.");
        if (start < 1 || start > 1000)
            throw new IllegalArgumentException("Start must be between 1 to 1000.");
        JSONObject res = new JSONObject();
        JSONArray arr = new JSONArray();
        int idx = start;
        while (idx <= MAX_ITEM_LENGTH) {
            try {
                URL apiUrl = getUrl(domain, query, display, idx, sort);
                HttpURLConnection con = getHttpURLConnection(apiUrl);
                JSONArray items = (JSONArray)getResponse(con).get("items");
                arr = concatArray(arr, items);
                idx += display;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.put("items", arr);
        return res;
    }

    private static URL getUrl(String domain, String query, int display, int start, String sort) {
        URL url = null;
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String params = String.format("%s?query=%s&display=%d&start=%d&sort=%s",
                    domain, encodedQuery, display, start, sort);
            url = new URL(BASE_URL + params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private static JSONObject getResponse(HttpURLConnection con) {
        StringBuffer response = new StringBuffer();
        try {
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject(response.toString());
    }
    private static HttpURLConnection getHttpURLConnection(URL url) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", NaverClientConfig.CLIENT_ID);
            con.setRequestProperty("X-Naver-Client-Secret", NaverClientConfig.CLIENT_SECRET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    private static JSONArray concatArray(JSONArray... arrs) throws JSONException {
        JSONArray res = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                res.put(arr.get(i));
            }
        }
        return res;
    }
}
