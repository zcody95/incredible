package com.calpoly.incredible;;

/**
 * This class stores all of the article data.
 * @author roslynsierra
 */
public class Article {
   private double credibilityScore;
   private double percentError;
   private String title;
   private String url;
   private String commonWord1;
   private String commonWord2;
   private String commonWord3;

   /*
    * Constructor for an Article takes in the url of the article.
    * @param url the url of the article.
    */
   public Article(String url) {
      credibilityScore = 0.0;
      percentError = 0.0;
      title = "";
      this.url = url;
      commonWord1 = "";
      commonWord2 = "";
      commonWord3 = "";
   }

   /*
    * @return the credibility score of the article.
    */
   public double getCredibilityScore() {
      return credibilityScore;
   }

   /*
    * @return the percentage of grammatical errors and spelling errors in
    * the article. Calculated by (total errors) / (word count)
    */
   public double getPercentError() {
      return percentError;
   }

   /*
    * @return the title of the article.
    */
   public String getTitle() {
      return title;
   }

   /*
    * @return the url of the article.
    */
   public String getUrl() {
      return url;
   }

   /*
    * @return the most common used word in the article.
    */
   public String getCommonWord1() {
      return commonWord1;
   }

   /*
    * @return the second most common used word in the article.
    */
   public String getCommonWord2() {
      return commonWord2;
   }

   /*
    * @return the third most common used term in the article.
    */
   public String getCommonWord3() {
      return commonWord3;
   }

   public void setCredibilityScore(double newScore) {
      credibilityScore = newScore;
   }

   public void setPercentError(double error) {
      percentError = error;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setCommonWord1(String word1) {
      commonWord1 = word1;
   }

   public void setCommonWord2(String word2) {
      commonWord2 = word2;
   }

   public void setCommonWord3(String word3) {
      commonWord3 = word3;
   }
}
