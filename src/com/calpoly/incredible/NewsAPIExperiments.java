package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.Collection;

/**
 * Created by Jenna on 3/1/17.
 * trying out NYT api and News api
 */
public final class NewsAPIExperiments {

//    public static void newsAPIget() {
//        HttpResponse<JsonNode> newsResponse = Unirest
//                .get("http://api.nytimes.com/svc/search/v2/articlesearch.json?")
//                .queryString("fq", queryStrings)
//                .header("start_date", "20170215")
//                .header("end_date", "20170301")
//                .header("api-key", "db5630d16fcb4b2183e910881c98a3d2")
//                .asJson();
//
//        System.out.println("News: " + newsResponse.getBody().toString() + "\n");
//    }


//       New York Times

    public static void printNYTget(Collection<String> queryStrings) throws Exception{
        HttpResponse<JsonNode> nytResponse = Unirest
                .get("http://api.nytimes.com/svc/search/v2/articlesearch.json?")
                .queryString("fq", queryStrings)
                .header("start_date", "20170215")
                .header("end_date", "20170301")
                .header("api-key", "db5630d16fcb4b2183e910881c98a3d2")
                .asJson();

        System.out.println("NYT" + nytResponse.getBody().toString() + "\n");
    }
}
