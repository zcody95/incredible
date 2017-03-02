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

        // Calculated the relatedness of the title to another article's
        try {
            SemanticRelatedness.setSemanticRelatedness(url, article, "Second Article goes here");
        } catch (Exception e) {
            System.out.println("Semantic relatedness Exception during post: ");
            e.printStackTrace();
        }

        // Check the grammer and spelling on the article
        try {
            LanguageTool.getPercentError(article);
        } catch (Exception e) {
            System.out.println("Language checking Exception during post: ");
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
