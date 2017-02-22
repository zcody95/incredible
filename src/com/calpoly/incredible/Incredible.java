package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Incredible {
    private static String connectionUrl;
    private static Connection con;

    public static void main(String[] args) throws UnirestException {

        // Connects to the Azure Backend using JDBC
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connectionUrl = "jdbc:sqlserver://incredibleserver17.database.windows.net:1433;database=IncredibleStorage;" +
                    "user=aibackend@outlook.com@incredibleserver17;" +
                    "password={Backend123!};encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            con = DriverManager.getConnection(connectionUrl);
            System.out.println("Successfully connected to SQL database");
        }catch(Exception e) {
            e.printStackTrace();
        }



        //HttpResponse<String> request = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=You%20will%20learn%20how%20to%20speak%20like%20me%20someday.%20%20Oh%20wait.")
        //.header("X-Mashape-Authorization", "<Insert your Mashape key here>").asString();

        //System.out.println(request.getBody());

        if (!(args.length >= 1)) {
            System.out.println("ERROR: Must provide a url.");
            return;
        }

        String url = args[0];

        System.out.println("POST Textuality");
        //Textuality pulls out important information from the link provided.
        //It will provide the title, content, links on the page, and images.

        // These code snippets use an open-source library. http://unirest.io/java
        HttpResponse<JsonNode> response = Unirest.post("https://extracttext.p.mashape.com/api/content_extract/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("dataurl", url)
                .asJson();

        System.out.println("AFTER POST");
        System.out.println(response.getBody().toString());
        String text = response.getBody().toString();
        Scanner s = new Scanner(text).useDelimiter(",");

        while (s.hasNext()) {
            System.out.println(s.next());
        }

        System.out.println("");
        //get title of article
        String title = response.getBody().getObject().get("title").toString();
        System.out.println("Title: "+title);

        System.out.println("");
        System.out.println("GET Alchemy");
        //Alchemy pulls out the title. Not sure if we want to use this API. It
        //doesn't seem to be quite as useful or easy to use as Textuality

        // These code snippets use an open-source library. http://unirest.io/java
        response = Unirest.get("https://alchemy.p.mashape.com/url/URLGetFeedLinks?outputMode=json&url="+url)
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Accept", "application/json")
                .asJson();

        System.out.println("AFTER GET");
        System.out.println(response.getBody().toString());
        text = response.getBody().toString();
        s = new Scanner(text).useDelimiter(",");

        while (s.hasNext()) {
            System.out.println(s.next());
        }

        System.out.println("");
        System.out.println("POST Semantic Relatedness");
        //Semantic Relatedness compares how related the two bodies of text are.
        //I'm not yet sure how this will work will long bodies of text, but it
        //could be used to compare titles of articles and then find credible
        //articles about the same subject for fact checking purposes.

        // These code snippets use an open-source library. http://unirest.io/java
        response = Unirest.post("https://amtera.p.mashape.com/relatedness/en")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\"t1\":\""+title+"\",\"t2\":\"Put other title here\"}")
                .asJson();

        System.out.println("POST POST");
        System.out.println(response.getBody().toString());
        text = response.getBody().toString();
        s = new Scanner(text).useDelimiter(",");

        while (s.hasNext()) {
            System.out.println(s.next());
        }
    }
}
