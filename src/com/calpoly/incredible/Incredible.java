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
      String sourceTable = "SrcTable";
      String link, next = "?";
      if (args.length == 2) {
          if (args[1].toLowerCase().equals("y")) {
              next = "y";
          }
          else if (args[1].toLowerCase().equals("n")) {
              next = "n";
          }
      }
      if (args.length < 1 || args.length > 2) {
          System.out.println("Invalid arguments.");
          return;
      }
      link = args[0];
      try {
         // Calculate Raw Score
         article = new Article();
         article.setUrl(link);
         RawScoreCalculator.calculateRawScore(link, article);
         Backend.connectToBackend();
         Backend.getSource(sourceTable, article);

         float score;
         System.out.println(article.printAll());
         //System.out.println(article.toString());
         if (next.equals("y")) {
            score = LearningAlgorithm.calculateLearn(article, true);
         } else if (next.equals("n")) {
            score = LearningAlgorithm.calculateLearn(article, false);
         } else {
            score = LearningAlgorithm.calculate(article);
         }
         float newScore = score;
         float cutoff = LearningAlgorithm.getCutoff();
         if (next.equals("n") || next.equals("y")) {

            if (score < cutoff && next.equals("y")) {
               newScore = cutoff + (cutoff - score) / article.getTotal();
            }
            else if (score >= cutoff && next.equals("n")) {
               newScore = cutoff - (score - cutoff) / article.getTotal();
            }
            else if (article.hasSource())
            {
               if ((score >= cutoff && score < article.getSourceScore())
                       || (score < cutoff && score > article.getSourceScore())) {
                  newScore = article.getSourceScore();
               }
            }

            newScore = Math.min(Math.max(newScore, 0), 100);

            if (!article.hasSource()) {
               Backend.insertNewSource(sourceTable, article.getSource(), newScore, 1);
            } else {
               Backend.insertNewSource(sourceTable, article.getSource(), (newScore + article.getSourceScore() * (article.getTotal() - 1)) / article.getTotal(), article.getTotal());
            }
         }

         if ( score >= cutoff) {
            System.out.print("Credible\nScore: ");
         }
         else {
            System.out.print("Not Credible\nScore: ");
         }
         if (score >= cutoff) {
            System.out.println(100f * Math.min(1.0f, 0.5f + (score / (50.0f - cutoff)) * 0.5f) + "%");
         }
         else {
            System.out.println(100f * (score / cutoff) * 0.5f + "%");
         }
      }
      catch (Exception ex) {
         System.out.println("Could not compute score.");
         ex.printStackTrace();
      }
      // Compare to other articles

      // Display results

      // ask for user feedback

      // update database
   }
}
