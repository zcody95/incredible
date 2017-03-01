package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * Created by Jenna on 3/1/17.
 * Textuality API
 * Textuality pulls out important information from the link provided.
 * It will provide the title, content, links on the page, and images.
 */
public final class Textuality {

    public static HttpResponse<JsonNode> post(String url) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post("https://extracttext.p.mashape.com/api/content_extract/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("dataurl", url)
                .asJson();

        return response;
    }

    public static void printData(HttpResponse<JsonNode> response) {
        //get title of article
        String title = response.getBody().getObject().get("title").toString();
        System.out.println("Title: " + title);
        System.out.println("");;
    }

    public static String getTitle(HttpResponse<JsonNode> response) {
        return response.getBody().getObject().get("title").toString();
    }

    public static String getBody(HttpResponse<JsonNode> response) {
        return response.getBody().getObject().get("content").toString();
    }

}
