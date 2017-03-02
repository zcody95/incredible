package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Created by cube on 3/1/2017.
 */
public class GoogleSentiment {

    private static final String API_KEY = "AIzaSyB8_kAihoJ-kiBWwUyyfVsvDISEHaVehYw";

    public static double sentiment(String body) {
        JSONObject req =  new JSONObject();
        req.put("encodingType","UTF8");
        JSONObject text = new JSONObject();
        text.put("type", "PLAIN_TEXT");
        text.put("content", body);
        req.put("document", text);

        try {
            HttpResponse<JsonNode> res = Unirest.post("https://language.googleapis.com/v1/documents:analyzeSentiment?key=" + API_KEY)
                    .body(req)
                    .asJson();

            double score = res.getBody().getObject().getJSONObject("documentSentiment").getDouble("score");
            return res.getBody().getObject().getJSONObject("documentSentiment").getDouble("magnitude") * Math.signum(score);
        } catch (Exception e) {
            e.printStackTrace();
            return -1111.0;
        }
    }

}
