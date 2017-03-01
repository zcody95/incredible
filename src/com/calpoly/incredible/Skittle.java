package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jenna on 3/1/17.
 */
public final class Skittle {
    public static HttpResponse<JsonNode> post(String url, String body) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post("https://sentinelprojects-skyttle20.p.mashape.com/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("annotate", 1)
                .field("keywords", 1)
                .field("lang", "en")
                .field("sentiment", 1)
                .field("text", body)
                .asJson();

        return response;
    }

    public static ArrayList<String> getCommonWords(JSONObject json) {
        JSONArray terms = json.getJSONArray("terms");
        String term1 = "", term2 = "", term3 = "";
        int count1 = 0, count2 = 0, count3 = 0;
        ArrayList<String> commonWords = new ArrayList<String>();

        for (int i = 0; i < terms.length(); i++) {
            JSONObject term = (JSONObject) terms.get(i);
            int current = (Integer) term.get("count");
            if (current > count1 && current > count2 && current > count3) {
                count3 = count2;
                term3 = term2;
                count2 = count1;
                term2 = term1;
                count1 = current;
                term1 = (String) term.get("term");
            }
            else if (current > count2 && current > count3) {
                count3 = count2;
                term3 = term2;
                count2 = current;
                term2 = (String) term.getString("term");
            }
            else if (current > count3) {
                count3 = current;
                term3 = (String) term.getString("term");
            }
        }


        System.out.println("Most common term is " + term1);
        System.out.println("Second most common term is " + term2);
        System.out.println("Third most common term is " + term3);
        System.out.println("");

        commonWords.add(term1);
        commonWords.add(term2);
        commonWords.add(term3);
        return commonWords;
    }
}
