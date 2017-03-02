package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jenna on 3/1/17.
 * This pulls out the most opinionated sentences and determines if they have
 * positive or negative connotation. It also pulls out the most common terms.
 */
public final class Skittle {

    public static void setCommonWords(String url, Article article) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post("https://sentinelprojects-skyttle20.p.mashape.com/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("annotate", 1)
                .field("keywords", 1)
                .field("lang", "en")
                .field("sentiment", 1)
                .field("text", article.getBody())
                .asJson();


        JSONArray docs = response.getBody().getObject().getJSONArray("docs");
        JSONObject json = (JSONObject) docs.getJSONObject(0);
        ArrayList<String> commonWords = Skittle.getCommonWords(json);

        article.setCommonWord1(commonWords.get(0));
        article.setCommonWord2(commonWords.get(1));
        article.setCommonWord3(commonWords.get(2));
    }

    private static ArrayList<String> getCommonWords(JSONObject json) {
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

        commonWords.add(term1);
        commonWords.add(term2);
        commonWords.add(term3);
        return commonWords;
    }
}
