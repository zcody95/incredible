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
      System.out.println("Enter a url:");

      try {
         Scanner s = new Scanner(System.in);
         String next = s.next();

         // Calculate Raw Score
         article = new Article();
         article.setUrl(next);
         RawScoreCalculator.calculateRawScore(next, article);
         Backend.connectToBackend();
         Backend.getSource(sourceTable, article);

         float score;

         System.out.println("Got article info");
         System.out.println(article.printAll() + "\n\n");
         //System.out.println(article.toString());
         System.out.println("Credible?");
         next = s.next().toLowerCase();
         if (next.equals("y")) {
            score = LearningAlgorithm.calculateLearn(article, true);
            System.out.println(score + " >= " + LearningAlgorithm.getCutoff());
         } else if (next.equals("n")) {
            score = LearningAlgorithm.calculateLearn(article, false);
            System.out.println(score + " >= " + LearningAlgorithm.getCutoff());
         } else {
            score = LearningAlgorithm.calculate(article);
         }
         float newScore = score;
         float cutoff = LearningAlgorithm.getCutoff();
         if (next.equals("n") || next.equals("y")) {
            System.out.println("...");
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
            System.out.println("y");
         }
         else {
            System.out.println("n");
         }
         System.out.println(next + "\n" + score + "\n" + LearningAlgorithm.getCutoff());
         if (score >= cutoff) {
            System.out.println(Math.min(1.0f, 0.5f + (score / (50.0f - cutoff)) * 0.5f));
         }
         else {
            System.out.println((score / cutoff) * 0.5f);
         }
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
