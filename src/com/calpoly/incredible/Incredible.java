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
   public static void main(String[] args) {
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

      // Calculate Raw Score
      String url = args[0];
      article = new Article();
      article.setUrl(url);
      RawScoreCalculator.calculateRawScore(url, article);
      System.out.println(article.toString());

      // Compare to other articles

      // Display results

      // ask for user feedback

      // update database
   }
}
