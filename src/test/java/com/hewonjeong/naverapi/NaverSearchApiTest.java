package com.hewonjeong.naverapi;

import com.hewonjeong.api.ApiErrorCode;
import com.hewonjeong.api.NaverSearchApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NaverSearchApiTest {
    final int EXPECTED_ITEMS_LENGTH = 1000;
    final String[] OBJECT_KEYS = {"link", "description", "title"};
    final String[] TARGET_DOMAINS = {"blog", "local", "kin", "news"};

    @Test
    public void testRequest_VaildParms_ShouldPass() {
        for (String s : TARGET_DOMAINS) {
            JSONObject res = NaverSearchApi.request(s, "춘천 여행", 100, 1, "sim");
            JSONArray items = (JSONArray) res.get("items");
            assertTrue(items.length() == EXPECTED_ITEMS_LENGTH);
            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = items.getJSONObject(i);
                assertTrue(hasKeys(obj, OBJECT_KEYS));
            }
        }
    }

    @Test
    public void testRequest_InvaildDomainValue_ShouldPass() {
        JSONObject res = NaverSearchApi.request("WRONG_PARM", "춘천 여행", 100, 1, "sim");
        assertEquals("Error", res.getString("result"));
        assertEquals(ApiErrorCode.NAVER_API_ERROR, res.getString("errorCode"));
    }

    @Test
    public void testRequest_NoResultQuery_ShouldPass() {
        for (String s : TARGET_DOMAINS) {
            final String NO_RESULT_QUERY = "Ffi22jp33#$#@d";
            JSONObject res = NaverSearchApi.request(s, NO_RESULT_QUERY, 100, 1, "sim");
            System.out.println(res);
            assertFalse(res.has("errorCode"));
            assertEquals("OK", res.getString("result"));
            JSONArray items = (JSONArray) res.get("items");
            assertTrue(items.length() == 0);
        }
    }

    @Test
    public void testRequest_InvalidDisplayValue_ShouldPass() {
        final int[] wrongDisplayVals = {-1, 0, 101};
        for (int val : wrongDisplayVals) {
            JSONObject res = NaverSearchApi.request("blog", "전주 여행", val, 1, "sim");
            assertEquals("Error", res.getString("result"));
            assertEquals(ApiErrorCode.NAVER_API_ERROR, res.getString("errorCode"));
        }
    }

    @Test
    public void testRequest_InvalidStartValue_ShouldPass() {
        final int[] wrongStartVals = {-1, 0, 1001};
        for (int val : wrongStartVals) {
            JSONObject res = NaverSearchApi.request("blog", "전주 여행", 100, 1001, "sim");
            System.out.println(res.toString());
            assertEquals(ApiErrorCode.NAVER_API_ERROR, res.getString("errorCode"));
        }
    }

    private boolean hasKeys(JSONObject obj, String[] keys) {
        for(String key : keys) {
            if (!obj.has(key)) return false;
        }
        return true;
    }
    /*@Test
    public void testResponeField() {
        JSONObject result = NaverSearchApi.request("local", "남이섬", 100, 1, "sim");
        assertTrue(result.has("items"));
        JSONArray arr = (JSONArray)result.get("items");
        JSONObject obj = ((JSONObject)arr.get(999));
        assertTrue(obj.has("address"));
        assertTrue(obj.has("roadAddress"));
        assertTrue(obj.has("link"));
        assertTrue(obj.has("description"));
        assertTrue(obj.has("telephone"));
        assertTrue(obj.has("title"));
        assertTrue(obj.has("category"));
        assertTrue(obj.has("mapy"));
        assertTrue(obj.has("mapx"));
    }*/
    /*@Test(expected = Exception.class)
    public void testWrongStart() {
        NaverSearchApi.request("blog", "abc", 100, -1, "sim");
    }*/

   /* @Test(expected = Exception.class)
    public void testWrongDisplay() {
        NaverSearchApi.request("blog", "abc", 0, 1, "sim");
    }*/
}