package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * Created by Jenna on 3/1/17.
 */
public final class SemanticRelatedness {

    public static float getSemanticRelatedness(String url, String article1Title, String article2Title) throws Exception {

        HttpResponse<JsonNode>response = Unirest.post("https://amtera.p.mashape.com/relatedness/en")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\"t1\":\""+article1Title+"\",\"t2\":\""+article2Title+"\"}")
                .asJson();

        return Float.parseFloat(response.getBody().getObject().get("v").toString());
    }
}
