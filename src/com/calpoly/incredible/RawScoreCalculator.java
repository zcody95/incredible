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

        // Calculate the top three most common terms in article
        try {
            Skittle.setCommonWords(url, article);
        } catch (Exception e) {
            System.out.println("Skittle exception during post: ");
            e.printStackTrace();
        }

        // Check the grammer and spelling on the article
        try {
            LanguageTool.getPercentError(article);
        } catch (Exception e) {
            System.out.println("Language checking Exception during post: ");
            e.printStackTrace();
        }

        try {
            //find comparable articles
            Bing.search(article.getTitle());

            //Google sentiment
            article.setSentiment(GoogleSentiment.sentiment(article.getBody()));

            //find similar dates of comparable articles
            ArrayList<Integer> dates = Bing.getDates(article.getDate());
            article.setNumArticlesSameMonth(dates.get(0));
            article.setNumArticlesSameWeek(dates.get(1));
            article.setNumArticlesSameDay(dates.get(2));
            //sort the bing result by distance from original date. The first 5 are the closest to orignal date
            //which we will add semantic relatedness to.
            Bing.sortByDate(article.getDate());
            for (int ind = 0; ind < Math.min(5, Bing.results.size()); ind++) {
                article.setRelatednessScore(Bing.relatedness(article.getBody(),ind), ind);
            }
        } catch (Exception e) {
            System.out.println("Exception getting dates from related Bing articles. ");
            e.printStackTrace();
        }
    }

//    HttpResponse<JsonNode> response;
//    String body = null;
//
//    // News APIs - data source aggregators
//    ArrayList<String> queryStrings = new ArrayList<String>();
//      queryStrings.add("space");
//      queryStrings.add("life");
//      try {
//        NewsAPIExperiments.printNYTget(queryStrings);
//    } catch (Exception e) {
//        System.out.println("NYT exception during get: ");
//    }

    //Skittle 2.0 API




    //Semantic Relatedness API
    //Semantic Relatedness compares how related the two bodies of text are.
    //This could be used to compare titles and find similar articles


    //Grammar and Spell Checking
    //Most credible articles will have very good grammar and spelling.
    //Those that don't are likely not as credible of sources, because the
    //author or editor was not knowledgable enough to write correctly.

}
