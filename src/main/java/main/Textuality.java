/*
 * The MIT License
 *
 * Copyright 2017 roslynsierra.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package main;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Scanner;


/**
 *
 * @author roslynsierra
 */
public class Textuality {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnirestException {
        
        //HttpResponse<String> request = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=You%20will%20learn%20how%20to%20speak%20like%20me%20someday.%20%20Oh%20wait.")
            //.header("X-Mashape-Authorization", "<Insert your Mashape key here>").asString();

        //System.out.println(request.getBody());
        
        if (!(args.length >= 1)) {
            System.out.println("ERROR: Must provide a url.");
            return;
        }
        
        String url = args[0];
        
        System.out.println("POST Textuality");
        //Textuality pulls out important information from the link provided.
        //It will provide the title, content, links on the page, and images.
        
        // These code snippets use an open-source library. http://unirest.io/java
        HttpResponse<JsonNode> response = Unirest.post("https://extracttext.p.mashape.com/api/content_extract/")
            .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Accept", "application/json")
            .field("dataurl", url)
            .asJson();
        
        System.out.println("AFTER POST");  
        System.out.println(response.getBody().toString());
        String text = response.getBody().toString();
        Scanner s = new Scanner(text).useDelimiter(",");
        
        while (s.hasNext()) {
            System.out.println(s.next());
        }
        
        System.out.println("");
        //get title of article
        String title = response.getBody().getObject().get("title").toString();
        System.out.println("Title: "+title);
        
        System.out.println("");
        System.out.println("GET Alchemy");
        //Alchemy pulls out the title. Not sure if we want to use this API. It
        //doesn't seem to be quite as useful or easy to use as Textuality
        
        // These code snippets use an open-source library. http://unirest.io/java
        response = Unirest.get("https://alchemy.p.mashape.com/url/URLGetFeedLinks?outputMode=json&url="+url)
            .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
            .header("Accept", "application/json")
            .asJson();
        
        System.out.println("AFTER GET");  
        System.out.println(response.getBody().toString());
        text = response.getBody().toString();
        s = new Scanner(text).useDelimiter(",");
        
        while (s.hasNext()) {
            System.out.println(s.next());
        }
        
        System.out.println("");
        System.out.println("POST Semantic Relatedness");
        //Semantic Relatedness compares how related the two bodies of text are.
        //I'm not yet sure how this will work will long bodies of text, but it
        //could be used to compare titles of articles and then find credible 
        //articles about the same subject for fact checking purposes.
        
        // These code snippets use an open-source library. http://unirest.io/java
        response = Unirest.post("https://amtera.p.mashape.com/relatedness/en")
            .header("X-Mashape-Key", "6mHWrpJfngmshbsZcedi1XmR2Urbp1kHUCgjsnctb0yJ4Ezskf")
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body("{\"t1\":\""+title+"\",\"t2\":\"Put other title here\"}")
            .asJson();
        
        System.out.println("AFTER POST");  
        System.out.println(response.getBody().toString());
        text = response.getBody().toString();
        s = new Scanner(text).useDelimiter(",");
        
        while (s.hasNext()) {
            System.out.println(s.next());
        }

    }
}
