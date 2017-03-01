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

      // Textuality - gets title, content, links, and images
      try {
         response = Textuality.post(url);
         Textuality.printData(response);
         article.setTitle(Textuality.getTitle(response));
         body = Textuality.getBody(response);
      } catch (Exception e) {
         System.out.println("Textuality exception during post:");
         e.printStackTrace();
      }

      // News APIs - data source aggregators
      ArrayList<String> queryStrings = new ArrayList<String>();
      queryStrings.add("space");
      queryStrings.add("life");
      try {
         NewsAPIExperiments.printNYTget(queryStrings);
      } catch (Exception e) {
         System.out.println("NYT exception during get: ");
      }

      //Skittle 2.0 API
      //This pulls out the most opinionated sentences and determines if they have
      //positive or negative connotation. It also pulls out the most common terms.

      //find the top three most common terms in article
      try {
         response = Skittle.post(url, body);
         JSONArray docs = response.getBody().getObject().getJSONArray("docs");
         JSONObject json = (JSONObject) docs.getJSONObject(0);
         ArrayList<String> commonWords = Skittle.getCommonWords(json);
         article.setCommonWord1(commonWords.get(0));
         article.setCommonWord2(commonWords.get(1));
         article.setCommonWord3(commonWords.get(2));
      } catch (Exception e) {
         System.out.println("Skittle exception during post: ");
         e.printStackTrace();
      }


      //Semantic Relatedness API
      //Semantic Relatedness compares how related the two bodies of text are.
      //This could be used to compare titles and find similar articles
      float relatednessScore = -1;
      try {
         relatednessScore = SemanticRelatedness.getSemanticRelatedness(url, article.getTitle(), "Second Article goes here");
         System.out.println("Semantic relatedness score: " + relatednessScore);
      } catch (Exception e) {
         System.out.println("Semantic relatedness Exception during post: ");
         e.printStackTrace();
      }

      //Grammar and Spell Checking
      //Most credible articles will have very good grammar and spelling.
      //Those that don't are likely not as credible of sources, because the 
      //author or editor was not knowledgable enough to write correctly.

      System.out.println("");
      //Language Tool API
      // These code snippets use an open-source library. http://unirest.io/java

      try {
         article.setPercentError(LanguageTool.getPercentError(body));
      } catch (Exception e) {
         System.out.println("Language checking Exception during post: ");
         e.printStackTrace();
      }
   }
}
