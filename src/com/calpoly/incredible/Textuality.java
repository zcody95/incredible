package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Jenna on 3/1/17.
 * Textuality API
 * Textuality pulls out important information from the link provided.
 * It will provide the title, content, links on the page, and images.
 */
public final class Textuality {

// TODO: set up array list of links in article .get("links");
    public static void setTextElements(String url, Article article) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post("https://extracttext.p.mashape.com/api/content_extract/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("dataurl", url)
                .asJson();
        //get the title
        article.setTitle(response.getBody().getObject().get("title").toString());
        article.setBody(response.getBody().getObject().get("content").toString());

        //get the date of the article
        String date = response.getBody().getObject().get("stamp").toString();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day  = Integer.parseInt(date.substring(8, 10));
        article.setDate(new Date(year, month, day));
    }

}
