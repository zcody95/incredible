package com.calpoly.incredible;

/**
 * This class stores all of the article data.
 * @author roslynsierra
 */
public class NewsSource {
   private String source;
   private double credibilityScore;
   private int numArticles;

   /*
    * Constructor for the news source class.
    * @param source is a string that is the name of the news source (example: "NewYork Times")
    */
   public NewsSource(String source) {
      this.source = source;
      credibilityScore = 0.0;
      numArticles = 0;
   }

   /*
    * @return the name of the news source.
    */
   public String getSource() {
      return source;
   }

   /*
    * @return the credibility score of the news source.
    */
   public double getCredibilityScore() {
      return score;
   }

   /*
    * @return the number of articles we have used to calculate this news sources credibility score.
    */
   public int getNumArticles() {
      return numArticles;
   }

   public void setSource(String newsSource) {
      source = newsSource;
   }

   public void setCredibilityScore(double newScore) {
      credibilityScore = newScore;
   }

   public void addArticle() {
      numArticles++;
   }
}
