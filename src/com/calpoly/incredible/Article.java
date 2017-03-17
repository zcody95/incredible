package com.calpoly.incredible;

import java.util.Date;
/**
 * This class stores all of the article data.
 * @author roslynsierra
 */
public class Article {
   private float relatednessScore = 0.0f;
   private float sourceScore = -1.0f;
   private int total = 1;
   private String title;
   private String body;
   private String url;
   private String source;
   private Date date;
   private int numArticlesSameWeek;
   private int numArticlesSameDay;

   /*
    * Constructor for an Article takes in the url of the article.
    * @param url the url of the article.
    */
   public Article() {
      title = "";
      date = null;
      body = "";
      source = "";
      numArticlesSameWeek = 0;
      numArticlesSameDay = 0;
   }

   @Override
   public String toString() {
      String result = source + ";" +
              url + ";" +
              numArticlesSameWeek + ";" + numArticlesSameDay + ";" +
              relatednessScore;

      return result;
   }

   public String printAll() {
      return "" + source + "\n"
              + "" + total + "\n"
              + "" + sourceScore + "\n"
              + "" + numArticlesSameWeek + "\n"
              + "" + numArticlesSameDay + "\n"
              + "" + relatednessScore + "\n";
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
    * @return the date the article was published.
    */
   public Date getDate() {
      return date;
   }

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

   public void setDate(Date date) {
      this.date = date;
   }

   public void setNumArticlesSameWeek(int sameWeek) { numArticlesSameWeek = sameWeek; }

   public void  setNumArticlesSameDay(int sameDay) { numArticlesSameDay = sameDay; }

   public void setRelatednessScore(float score) {
      relatednessScore = score;
   }

   public float getRelatednesScores() {return relatednessScore;}

   public void setSourceScore(float src) {
      sourceScore = src;
   }

   public float getSourceScore() {
      return sourceScore;
   }

   public int getTotal() {
      return total;
   }

   public void setTotal(int tot) {
      total = tot;
   }

   public String getSource() {
       return source;
   }

   public boolean hasSource() {return sourceScore != -1.0f;}
}
