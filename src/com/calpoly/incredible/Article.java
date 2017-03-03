package com.calpoly.incredible;

import java.util.Date;
/**
 * This class stores all of the article data.
 * @author roslynsierra
 */
public class Article {
   private double credibilityScore;
   private double percentError;
   private float[] relatednessScore = new float[5];
   private String title;
   private String body;
   private String url;
   private String source;
   private String commonWord1;
   private String commonWord2;
   private String commonWord3;
   private String commonSentence1;
   private String commonSentence2;
   private String commonSentence3;
   private Date date;
   private double sentiment;
   private int numArticlesSameMonth;
   private int numArticlesSameWeek;
   private int numArticlesSameDay;

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
      commonSentence1 = "";
      commonSentence2 = "";
      commonSentence3 = "";
      date = null;
      body = "";
      source = "";
      numArticlesSameMonth = 0;
      numArticlesSameWeek = 0;
      numArticlesSameDay = 0;
   }

   @Override
   public String toString() {

      return  source + ";" +
              url + ";" + "[" +
              commonWord1 + "," + commonWord2 + "," + commonWord3 + "]" + ";" +
              percentError + ";" + sentiment + ";" +
              numArticlesSameMonth + ";" + numArticlesSameWeek + ";" + numArticlesSameDay + ";" +
              relatednessScore[0] + ";" + relatednessScore[1] + ";" +
              relatednessScore[2] + ";" + relatednessScore[3] + ";" +
              relatednessScore[4];
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
    * @return the sentence with the most common words used in it.
    */
   public String getCommonSentence1() {
      return commonSentence1;
   }

   /*
    * @return the sentence with the most common words used in it.
    */
   public String getCommonSentence2() {
      return commonSentence2;
   }

   /*
    * @return the sentence with the most common words used in it.
    */
   public String getCommonSentence3() {
      return commonSentence3;
   }

   /*
    * @return the date the article was published.
    */
   public Date getDate() {
      return date;
   }

   /*
    * @return the number of similar articles published in the same month.
    */
   public Integer getNumArticlesSameMonth() { return numArticlesSameMonth; }

   /*
    * @return the number of similar articles published in the same week.
    */
   public Integer getNumArticlesSameWeek() { return numArticlesSameWeek; }

   /*
    * @return the number of similar articles published in the same day.
    */
   public Integer getNumArticlesSameDay() { return numArticlesSameDay; }

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

   public void setCommonSentence1(String sentence1) {commonSentence1 = sentence1; };

   public void setCommonSentence2(String sentence2) {commonSentence2 = sentence2; };

   public void setCommonSentence3(String sentence3) {commonSentence3 = sentence3; };

   public void setDate(Date date) {
      this.date = date;
   }

   public void setNumArticlesSameMonth(int sameMonth) { numArticlesSameMonth = sameMonth; }

   public void setNumArticlesSameWeek(int sameWeek) { numArticlesSameWeek = sameWeek; }

   public void  setNumArticlesSameDay(int sameDay) { numArticlesSameDay = sameDay; }

   public void setRelatednessScore(float score, int index) {
      relatednessScore[index] = score;
   }

   public float getRelatednesScores(int index) {return relatednessScore[index];}

   public void setSentiment(double senti) {
      sentiment = senti;
   }

   public double getSentiment() {
      return sentiment;
   }
}
