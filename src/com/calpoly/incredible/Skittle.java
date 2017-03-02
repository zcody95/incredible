package com.calpoly.incredible;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jenna on 3/1/17.
 * This pulls out the most opinionated sentences and determines if they have
 * positive or negative connotation. It also pulls out the most common terms.
 */
public final class Skittle {

    public static void setCommonWords(String url, Article article) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post("https://sentinelprojects-skyttle20.p.mashape.com/")
                .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .field("annotate", 1)
                .field("keywords", 1)
                .field("lang", "en")
                .field("sentiment", 1)
                .field("text", article.getBody())
                .asJson();


        JSONArray docs = response.getBody().getObject().getJSONArray("docs");
        JSONObject json = (JSONObject) docs.getJSONObject(0);
        ArrayList<String> commonWords = Skittle.getCommonWords(json);

        article.setCommonWord1(commonWords.get(0));
        article.setCommonWord2(commonWords.get(1));
        article.setCommonWord3(commonWords.get(2));

        //Importance is weighted based on how many of the common words are in the sentence.
        ArrayList<String> importantSentences = getImportantSentences(article, commonWords);

        article.setCommonSentence1(importantSentences.get(0));
        article.setCommonSentence2(importantSentences.get(1));
        article.setCommonSentence3(importantSentences.get(2));
    }

    private static ArrayList<String> getCommonWords(JSONObject json) {
        JSONArray terms = json.getJSONArray("terms");
        String term1 = "", term2 = "", term3 = "";
        int count1 = 0, count2 = 0, count3 = 0;
        ArrayList<String> commonWords = new ArrayList<String>();

        for (int i = 0; i < terms.length(); i++) {
            JSONObject term = (JSONObject) terms.get(i);
            int current = (Integer) term.get("count");
            if (current > count1 && current > count2 && current > count3) {
                count3 = count2;
                term3 = term2;
                count2 = count1;
                term2 = term1;
                count1 = current;
                term1 = (String) term.get("term");
            }
            else if (current > count2 && current > count3) {
                count3 = count2;
                term3 = term2;
                count2 = current;
                term2 = (String) term.getString("term");
            }
            else if (current > count3) {
                count3 = current;
                term3 = (String) term.getString("term");
            }
        }

        commonWords.add(term1);
        commonWords.add(term2);
        commonWords.add(term3);
        return commonWords;
    }

    private static ArrayList<String> getImportantSentences(Article article, ArrayList<String> words) {
        // Must have set the body with Textuality call first.
        Scanner s = new Scanner(article.getBody()).useDelimiter("\\.");
        ArrayList<String> keySentences = new ArrayList<String>();
        ArrayList<String> sentences = new ArrayList<String>();
        while(s.hasNext()) {
            sentences.add(s.next());
        }

       for (String sentence : sentences) {
            //check if sentence contains all three of the key words
            if (sentence.contains(words.get(0)) && sentence.contains(words.get(1)) &&
                    sentence.contains(words.get(2))) {
                keySentences.add(sentence);
            }
        }

        sentences.removeAll(keySentences);
        //if not enough sentence have all three key words try pulling out sentence with 2 of the words
        if (keySentences.size() < 3) {
            for (String sentence : sentences) {
                //check if sentence contains two of the three key words
                if (!keySentences.contains(sentence) &&
                        sentence.contains(words.get(0)) && sentence.contains(words.get(1)) ||
                        sentence.contains(words.get(0)) && sentence.contains(words.get(2)) ||
                        sentence.contains(words.get(2)) && sentence.contains(words.get(1))) {
                    keySentences.add(sentence);
                }
                if (keySentences.size() > 3)
                    break;
            }
        }

        sentences.removeAll(keySentences);
        //if not enough sentence have all three key words, or two of them, try pulling out sentence with 1 of the words
        if (keySentences.size() < 3) {
            for (String sentence : sentences) {
                //check if sentence contains one of the three key words
                if (!keySentences.contains(sentence) &&
                        sentence.contains(words.get(0)) || sentence.contains(words.get(1)) ||
                        sentence.contains(words.get(2))) {
                    keySentences.add(sentence);
                }
                if (keySentences.size() > 3)
                    break;
            }
        }
        return keySentences;
    }
}
