package com.calpoly.incredible;

import java.util.Date;
/**
 * This class stores all of the article data.
 * @author roslynsierra
 */
public class Article {
   private double credibilityScore;
   private double percentError;
   private float relatednesScore;
   private String title;
   private String body;
   private String url;
   private String source;
   private String commonWord1;
   private String commonWord2;
   private String commonWord3;
   private Date date;

   /*
    * Constructor for an Article takes in the url of the article.
    * @param url the url of the article.
    */
   public Article() {
      credibilityScore = 0.0;
      percentError = 0.0;
      title = "";
      commonWord1 = "";
      commonWord2 = "";
      commonWord3 = "";
      date = null;
      body = "";
      source = "";
   }

   @Override
   public String toString() {

      return  source + "|" +
              url + "|" + "[" +
              commonWord1 + "," + commonWord2 + "," + commonWord3 + "]" + "|" +
              percentError + "|" +
              relatednesScore + "|";
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
      return this.title;
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

   /*
    * @return the date the article was published.
    */
   public Date getDate() {
      return date;
   }

   public void setBody(String body) { this.body = body; }

   public String getBody() { return body; }

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
      setSource();
   }

   // captures the text between the 2nd and 3rd forward-slash
   private void setSource() {
      String tempSource = "";
      char character;
      int slashes = 0;

      // for each char in the url
      for (int i = 0; i < url.length(); i++) {
         character = url.charAt(i);
         if (character == '/') {
            slashes++;
            // skips adding the second slash to the source
            if (slashes == 2) {
               i++;
               character = url.charAt(i);
            }
         }
         // while you stil haven't encountered the 3rd slash
         if (slashes == 2) {
            tempSource += character;
         }
      }

      source = tempSource;
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

   public void setDate(Date date) {
      this.date = date;
   }

   public void setRelatednesScore(Float score) {this.relatednesScore = score;}

   public Float getRelatednesScore() {return relatednesScore;}
}
