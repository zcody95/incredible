package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jenna on 3/1/17.
 */
public final class RawScoreCalculator {

    public static void calculateRawScore(String url, Article article) {

        // Extract the Title and Body of text for the article
        try {
            Textuality.setTextElements(url, article);
        } catch (Exception e) {
            System.out.println("Textuality Exception during post: ");
            e.printStackTrace();
        }

        try {
            //find comparable articles
            Bing.search(article.getTitle());

            //find similar dates of comparable articles
            ArrayList<Integer> dates = Bing.getDates(article.getDate());
            article.setNumArticlesSameMonth(dates.get(0));
            article.setNumArticlesSameWeek(dates.get(1));
            article.setNumArticlesSameDay(dates.get(2));
            //sort the bing result by distance from original date. The first 5 are the closest to orignal date
            //which we will add semantic relatedness to.
            Bing.sortByDate(article.getDate());
            article.setRelatednessScore(Bing.relatedness(article.getBody(), Bing.results.get(0).description));
        } catch (Exception e) {
            System.out.println("Exception getting dates from related Bing articles. ");
            e.printStackTrace();
        }
    }

}
