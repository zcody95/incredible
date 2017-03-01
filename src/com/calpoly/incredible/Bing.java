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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Used for querying the Bing News Search API.
 */
public class Bing {
    // The api key provided by Microsoft
    private static final String apiKey = "841516eb16a04ed6b20ac9bfe7be6fdb";

    // The default number of results to get
    private static final int NUM_RESULTS = 5;

    // The list of results
    public static ArrayList<BingResult> results = new ArrayList();

    /**
     * Search news articles using the Bing search engine.
     *
     * @param search the search query
     * @param numResults the number of results to return
     */
    public static void search(String search, int numResults) {

        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api.cognitive.microsoft.com/bing/v5.0/news/search")
                    .queryString("q", search)
                    .queryString("count", numResults)
                    .header("Ocp-Apim-Subscription-Key", apiKey)
                    .asJson();
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
