package com.calpoly.incredible;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.org.apache.xml.internal.security.c14n.implementations.UtfHelpper;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Used for querying the Bing News Search API.
 */
public class Bing {
    // The api keys provided by Microsoft
    private static final String BING_API_KEY = "841516eb16a04ed6b20ac9bfe7be6fdb";
    private static final String ACADEMIC_KEY = "f4e25d6b63c4469b8806e87caa1c8489";

    // The default number of results to get
    private static final int NUM_RESULTS = 25;

    /**
     * The articles that are the result of running the search.
     *
     * @pre MUST CALL SEARCH BEFORE ACCESSING
     */
    public static ArrayList<BingResult> results = new ArrayList();

    /**
     * Search news articles using the Bing search engine.
     *
     * @param search the search query
     * @param numResults the number of results to return
     */
    public static void search(String search, int numResults) {

        try {
            // Construct the HTTP request
            HttpResponse<JsonNode> response = Unirest.get("https://api.cognitive.microsoft.com/bing/v5.0/news/search")
                    .queryString("q", search)
                    .queryString("count", numResults)
                    .header("Ocp-Apim-Subscription-Key", BING_API_KEY)
                    .asJson();

            // Populate the results list with a list of Bing Results converted from the response JSON
            Type listType = new TypeToken<ArrayList<BingResult>>(){}.getType();
            Gson gson = new Gson();
            results = gson.fromJson(response.getBody().getObject().get("value").toString(), listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search news articles using the Bing search engine.
     * Returns 5 results.
     *
     * @param search the search query
     */
    public static void search(String search) {
        search(search, NUM_RESULTS);
    }


    /**
     * Find the semantic relatedness between the two given bodies of text.
     *
     * @param body  the first body of text to compare
     * @param bodyTwo the second body of text to compare
     * @return a float between -1.0 and 1.0 indicating weakest to strongest relatedness respectively, or -1111.0 for an error
     */
    public static float relatedness(String body, String bodyTwo) {
        try {
            // Construct the request
            String request = "s1=" + body + "&s2=" + bodyTwo + "";
            HttpResponse<String> response = Unirest.post("https://westus.api.cognitive.microsoft.com/academic/v1.0/similarity")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Ocp-Apim-Subscription-Key", ACADEMIC_KEY)
                    .body(request)
                    .asString();
            // Return the float parsed from the response string
            return Float.parseFloat(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return -1111.0f;
        }
    }

    /**
     * Find the semantic relatedness between the given body of text and the description of the article at the given
     * index from the search results.
     *
     * @pre MUST CALL SEARCH FIRST BEFORE CALLING
     *
     * @param body the body of text to compare
     * @param index the index of the article to compare against from the results list
     *
     * @return a float between -1.0 and 1.0 indicating weakest to strongest relatedness respectively, or -1111.0 for an error
     */
    public static float relatedness(String body, int index) {
        return relatedness(body, results.get(index).description);
    }

    /**
     * The resulting object from a Bing API call.
     */
    public class BingResult {
        String datePublished;
        JsonObject image;
        ArrayList<Provider> provider;
        String name;
        ArrayList<Entity> about;
        String description;
        String category;
        String url;

        public class Provider {
            String _type;
            String name;

            @Override
            public String toString() {
                return name;
            }
        }

        public class Entity {
            String readLink;
            String name;

            @Override
            public String toString() {
                return name;
            }
        }
    }
}
