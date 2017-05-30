package com.hewonjeong.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NaverSearchApi {
    public static final String BASE_URL = "https://openapi.naver.com/v1/search/";
    public static final int MAX_ITEM_LENGTH = 1000;

    public static JSONObject request(String domain, String query, int display, int start, String sort) {
        JSONObject res = new JSONObject();
        JSONArray arr = new JSONArray();
        while (arr.length() < MAX_ITEM_LENGTH) {
            try {
                System.out.println(arr.length() + "  ");
                URL apiUrl = getUrl(domain, query, display, start, sort);
                HttpURLConnection con = getHttpURLConnection(apiUrl);
                JSONArray items = (JSONArray)getResponse(con).get("items");
                arr = concatArray(arr, items);
                // If there's no result or it's last page of result, It's useless to request next page.
                if (items.length() == 0 || items.length() < display) break;
                start += display;
            } catch (NaverApiException e) {
                return errorObject(ApiErrorCode.NAVER_API_ERROR, e.getMessage());
            } catch (IOException e) {
                return errorObject(ApiErrorCode.CONN_EXCEPTION, e.getMessage());
            } catch (JSONException e) {
                return errorObject(ApiErrorCode.JSON_EXCEPTION, e.getMessage());
            }
        }
        res.put("result", "OK");
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

    private static JSONObject getResponse(HttpURLConnection con)
            throws IOException, NaverApiException {
        JSONObject response;
        int responseCode = con.getResponseCode();
        if(responseCode != 200) {
            response = readStream(con.getErrorStream());
            String errorMessage = response.get("errorMessage").toString();
            throw new NaverApiException(errorMessage);
        }
        response = readStream(con.getInputStream());
        return new JSONObject(response.toString());
    }

    private static JSONObject readStream(InputStream is) throws IOException{
        StringBuffer res = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();
        return new JSONObject(res.toString());
    }
    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection con = null;
        con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id", NaverClientConfig.CLIENT_ID);
        con.setRequestProperty("X-Naver-Client-Secret", NaverClientConfig.CLIENT_SECRET);
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

    private static JSONObject errorObject(String errorCode, String errorMessage) {
        JSONObject res = new JSONObject();
        res.put("result", "Error");
        res.put("errorMessage", errorMessage);
        res.put("errorCode", errorCode);
        return res;
    }
}

class NaverApiException extends Exception{
    NaverApiException(String msg){
        super(msg);
    }
}
