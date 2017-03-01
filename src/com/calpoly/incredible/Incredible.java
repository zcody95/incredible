package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 * This class runs all of our APIs on the article.
 * @author roslynsierra
 */
public class Incredible {
   private static String connectionUrl;     
   private static Connection con;
   private static Article article;

   /*
    * This is the main method that runs all the APIs
    * @param args the commandline arguments. args[0] must be a url passed
    * in by the user.
    **/
   public static void main(String[] args) throws UnirestException {
      // Connects to the Azure Backend using JDBC
//      try {
//         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//         connectionUrl = "jdbc:sqlserver://incredibleserver17.database.windows.net:1433;database=IncredibleStorage;" +
//            "user=aibackend@outlook.com@incredibleserver17;" +
//            "password={Backend123!};encrypt=true;" +
//            "trustServerCertificate=false;" +
//            "hostNameInCertificate=*.database.windows.net;loginTimeo    ut=30;";
//         con = DriverManager.getConnection(connectionUrl);
//         System.out.println("Successfully connected to SQL database");
//      }catch(Exception e) {
//         e.printStackTrace();
//      }

      //check that the user passed in a url for the APIs 
      if (!(args.length >= 1)) {
         System.out.println("ERROR: Must provide a url.");
         return;
      }
      String url = args[0];
      article = new Article(url);
      HttpResponse<JsonNode> response;
      String body = null;

      // These code snippets use an open-source library. http://unirest.io/java
      try {
         response = Textuality.post(url);
         Textuality.printData(response);
         article.setTitle(Textuality.getTitle(response));
         body = Textuality.getBody(response);
      } catch (Exception e) {
         System.out.println("Textuality exception during post + ");
         e.printStackTrace();
      }

//      News API - data source aggregator
      ArrayList<String> queryStrings = new ArrayList<String>();
      queryStrings.add("space");
      queryStrings.add("life");

      try {
         NewsAPIExperiments.printNYTget(queryStrings);
      } catch (Exception e) {
         System.out.println("NYT exception during get");
      }


      //Skittle 2.0 API
      //This pulls out the most opinionated sentences and determines if they have
      //positive or negative connotation. It also pulls out the most common terms.

      // These code snippets use an open-source library.
      response = Unirest.post("https://sentinelprojects-skyttle20.p.mashape.com/")
         .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
         .header("Content-Type", "application/x-www-form-urlencoded")
         .header("Accept", "application/json")
         .field("annotate", 1)
         .field("keywords", 1)
         .field("lang", "en")
         .field("sentiment", 1)
         .field("text", body)
         .asJson();

      //find the top three most common terms in article
      String term1 = "", term2 = "", term3 = "";
      int count1 = 0, count2 = 0, count3 = 0;

      JSONArray docs = response.getBody().getObject().getJSONArray("docs");
      JSONObject json = (JSONObject) docs.getJSONObject(0);
      JSONArray terms = json.getJSONArray("terms");

      for (int i = 0; i < terms.length(); i++) {
         JSONObject term = (JSONObject) terms.get(i);
         int current = (Integer) term.get("count");
         if (current > count1 && current > count2 && current > count3) {
            count3 = count2;
            term3 = term2;
            count2 = count1;
            term2 = term1;
            count1 = current;
            term1 = (String) term.get("term");
         }
         else if (current > count2 && current > count3) {
            count3 = count2;
            term3 = term2;
            count2 = current;
            term2 = (String) term.getString("term");
         }
         else if (current > count3) {
            count3 = current;
            term3 = (String) term.getString("term");
         }

      }
      System.out.println("Most common term is " + term1);
      System.out.println("Second most common term is " + term2);
      System.out.println("Third most common term is " + term3);
      System.out.println("");
      article.setCommonWord1(term1);
      article.setCommonWord2(term2);
      article.setCommonWord3(term3);

      //Semantic Relatedness API
      //Semantic Relatedness compares how related the two bodies of text are.
      //This could be used to compare titles and find similar articles

      // These code snippets use an open-source library. http://unirest.io/java
      response = Unirest.post("https://amtera.p.mashape.com/relatedness/en")
         .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
         .header("Content-Type", "application/json")
         .header("Accept", "application/json")
         .body("{\"t1\":\""+article.getTitle()+"\",\"t2\":\"Put other title here\"}")
         .asJson();

      System.out.println("Semantic Relatedness Score: " + response.getBody().getObject().get("v"));

      //Grammar and Spell Checking
      //Most credible articles will have very good grammar and spelling.
      //Those that don't are likely not as credible of sources, because the 
      //author or editor was not knowledgable enough to write correctly.

      System.out.println("");
      //Language Tool API
      // These code snippets use an open-source library. http://unirest.io/java
      HttpResponse<String> spelling = Unirest.post("https://dnaber-languagetool.p.mashape.com/v2/check")
         .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
         .header("Content-Type", "application/x-www-form-urlencoded")
         .header("Accept", "text/plain")
         .field("language", "en-US")
         .field("text", body)
         .asString();

      //count number of mistakes
      String str = spelling.getBody();
      String findStr = "\"message\":";
      int lastIndex = 0;
      int count = 0;
      while(lastIndex != -1) {
         lastIndex = str.indexOf(findStr,lastIndex);
         if(lastIndex != -1) {
            count ++;
            lastIndex += findStr.length();
         }
      }
      System.out.println("There were " + count + " errors");

      //get number of words
      String wordcount = spelling.getHeaders().get("Content-Length").toString();
      System.out.println("Number of words is " + wordcount);
      wordcount = wordcount.substring(1, wordcount.length() - 1);
      int words = Integer.parseInt(wordcount);
      double errorPercentage = (double) count / (double) words;
      System.out.println("Percentage of error is " + errorPercentage);
      article.setPercentError(errorPercentage);
   }
}
