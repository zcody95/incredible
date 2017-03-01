package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

/**
 * Created by Jenna on 3/1/17.
 */
public final class LanguageTool {

    public static double getPercentError(String body) throws Exception {
        HttpResponse<String> spelling = Unirest.post("https://dnaber-languagetool.p.mashape.com/v2/check")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "text/plain")
                .field("language", "en-US")
                .field("text", body)
                .asString();

        //count number of mistakes
        String str = spelling.getBody();
        String findStr = "\"message\":";
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1) {
            lastIndex = str.indexOf(findStr,lastIndex);
            if(lastIndex != -1) {
                count ++;
                lastIndex += findStr.length();
            }
        }
        System.out.println("There were " + count + " errors");

        //get number of words
        String wordcount = spelling.getHeaders().get("Content-Length").toString();
        System.out.println("Number of words is " + wordcount);
        wordcount = wordcount.substring(1, wordcount.length() - 1);
        int words = Integer.parseInt(wordcount);
        double errorPercentage = (double) count / (double) words;
        System.out.println("Percentage of error is " + errorPercentage);

        return errorPercentage;
    }
}
