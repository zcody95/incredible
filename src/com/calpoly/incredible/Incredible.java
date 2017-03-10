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

      System.out.println("Enter a url:");

      try {
         Scanner s = new Scanner(System.in);
         String next = s.next();

         // Calculate Raw Score
         article = new Article();
         article.setUrl(next);
         RawScoreCalculator.calculateRawScore(next, article);
         Backend.connectToBackend();
         Backend.getSource(article);

         float score;

         System.out.println("Got article info");
         System.out.println(article.printAll() + "\n\n");
         //System.out.println(article.toString());
         System.out.println("Is this article credible?");
         next = s.next();
         if (next.equals("y") || next.equals("Y")) {
            System.out.println("Calculating score. This may take a minute...");
            score = LearningAlgorithm.calculateLearn(article, true);
            System.out.println(score + " >= " + LearningAlgorithm.getCutoff());
         } else if (next.equals("n") || next.equals("N")) {
            System.out.println("Calculating score. This may take a minute...");
            score = LearningAlgorithm.calculateLearn(article, false);
            System.out.println(score + " >= " + LearningAlgorithm.getCutoff());
         } else {
            System.out.println("Calculating score. This may take a minute...");
            score = LearningAlgorithm.calculate(article);
         }
         System.out.println("Updating Backend...");
         if (article.getSourceScore() == -1) {
            Backend.insertNewSource(article.getSource(), score, 1);
         } else {
            Backend.insertNewSource(article.getSource(), (score + article.getSourceScore() * (article.getTotal() - 1)) / article.getTotal(), article.getTotal());
         }
         System.out.println(score + " >= " + LearningAlgorithm.getCutoff());
      }
      catch (Exception ex) {
         ex.printStackTrace();
         System.out.println("Could not compute score.");
      }
      // Compare to other articles

      // Display results

      // ask for user feedback

      // update database
   }
}
