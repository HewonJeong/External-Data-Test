package com.hewonjeong.naverapi;

import com.hewonjeong.api.NaverSearchApi;
import net.javacrumbs.jsonunit.core.Option;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static net.javacrumbs.jsonunit.JsonAssert.when;
import static net.javacrumbs.jsonunit.core.Option.*;
import static org.junit.Assert.assertTrue;

public class NaverSearchApiTest {
    final int EXPECTED_ITEMS_LENGTH = 1000;

    @Test
    public void testRequest() {
        // Valid request
        JSONObject obj = NaverSearchApi.request("kin", "춘천 여행", 100, 1, "sim");
        assertTrue(((JSONArray)obj.get("items")).length() == EXPECTED_ITEMS_LENGTH);

        assertJsonEquals("[{\"address\":1}, {\"test\":2}]",
                "[{\n\"test\": 1\n}, {\"TEST\": 4}]", when(IGNORING_VALUES));

    }
    @Test
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
    }
    @Test(expected = Exception.class)
    public void testWrongStart() {
        NaverSearchApi.request("blog", "abc", 100, -1, "sim");
    }

    @Test(expected = Exception.class)
    public void testWrongDisplay() {
        NaverSearchApi.request("blog", "abc", 0, 1, "sim");
    }
}