package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

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
}
