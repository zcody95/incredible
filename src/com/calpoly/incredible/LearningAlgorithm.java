package com.calpoly.incredible;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.Calendar;

public class LearningAlgorithm {
    private static final String WEIGHT_PATH = "equation/weights.json";
    private static final String BASE_PATH = "equation/baseWeights.json";
    private static final String IDEAL_ARTICLE = "equation/ideal.json";
    private static final String INCREDIBLE_ARTICLE = "equation/incredible.json";
    private static File currentFile;
    private static Weights weights, baseWeights;
    private static Article ideal, incredible;
    private static Gson gson;

    public static float calculate(Article article) throws Exception {
        float score;

        initializeWeights();
        if (article.getSourceScore() == -1) {
            score = baseWeights.semWeight * article.getRelatednesScores()
                    + baseWeights.weekWeight * ((float)article.getNumArticlesSameWeek() / 25.0f)
                    + baseWeights.dayWeight * ((float)article.getNumArticlesSameDay() / 25.0f);
            System.out.println("Weights:\nSemWeights: " + baseWeights.semWeight
                + "\nWeek: " + baseWeights.weekWeight
                + "\nDay: " + baseWeights.dayWeight);
        }
        else {
            score = weights.semWeight * article.getRelatednesScores()
                    + weights.srcWeight * (article.getSourceScore() / 100.0f)
                    + weights.weekWeight * ((float)article.getNumArticlesSameWeek() / 25.0f)
                    + weights.dayWeight * ((float)article.getNumArticlesSameDay() / 25.0f);
            System.out.println("Weights:\nSemWeights: " + weights.semWeight
                    + "\nSrc: " + weights.srcWeight
                    + "\nWeek: " + weights.weekWeight
                    + "\nDay: " + weights.dayWeight);
        }

        return score;
    }

    public static float calculateLearn(Article article, boolean credible) throws Exception{
        float score = calculate(article);
        Field change;
        initializeIdeal();

        if (credible) {
            change = credibleLearn(article);
            if (score < weights.cutoff) {
                weight(change, article, score);
            }
        }
        else {
            change = incredibleLearn(article);
            if (score >= weights.cutoff) {
                weight(change, article, score);
            }
        }

        if (!article.hasSource()) {
            baseWeights.total++;
            currentFile = new File(BASE_PATH);
            writeFile(currentFile, baseWeights);
        }
        else {
            weights.total++;
            currentFile = new File(WEIGHT_PATH);
            writeFile(currentFile, weights);
        }

        return score;
    }

    private static Field credibleLearn(Article article) throws Exception{
        float maxDistance = (ideal.getNumArticlesSameWeek() / 25) - (article.getNumArticlesSameWeek() / 25);
        Field field = Field.WEEK;

        float distance = (ideal.getNumArticlesSameDay() / 25) - (article.getNumArticlesSameDay() / 25);
        if (Math.abs(distance) > Math.abs(maxDistance)) {
            maxDistance = distance;
            field = Field.DAY;
        }

        distance = ideal.getRelatednesScores() - article.getRelatednesScores();
        if (Math.abs(distance) > Math.abs(maxDistance)) {
            maxDistance = distance;
            field = Field.ARTICLE;
        }

        if (article.getSourceScore() != -1f) {
            distance = ideal.getSourceScore() - article.getSourceScore();
            if (Math.abs(distance) > Math.abs(maxDistance)) {
                field = Field.SOURCE;
            }
        }

        int tot = ideal.getTotal();
        ideal.setTotal(tot + 1);

        switch (field) {
            case ARTICLE:
                ideal.setRelatednessScore((ideal.getRelatednesScores() * tot + article.getRelatednesScores()) / ideal.getTotal());
                break;
            case WEEK:
                ideal.setNumArticlesSameWeek((ideal.getNumArticlesSameWeek() * tot + article.getNumArticlesSameWeek()) / ideal.getTotal());
                break;
            case DAY:
                ideal.setNumArticlesSameDay((ideal.getNumArticlesSameDay() * tot + article.getNumArticlesSameDay()) / ideal.getTotal());
                break;
            case SOURCE:
                ideal.setSourceScore((ideal.getSourceScore() * tot + article.getSourceScore()) / ideal.getTotal());
                break;
        }

        currentFile = new File(IDEAL_ARTICLE);
        writeFile(currentFile, ideal);
        return field;
    }

    private static Field incredibleLearn(Article article) throws Exception{
        float maxDistance = (incredible.getNumArticlesSameWeek() / 25) - (article.getNumArticlesSameWeek() / 25);
        Field field = Field.WEEK;

        float distance = (incredible.getNumArticlesSameDay() / 25) - (article.getNumArticlesSameDay() / 25);
        if (Math.abs(distance) > Math.abs(maxDistance)) {
            maxDistance = distance;
            field = Field.DAY;
        }

        distance = incredible.getRelatednesScores() - article.getRelatednesScores();
        if (Math.abs(distance) > Math.abs(maxDistance)) {
            maxDistance = distance;
            field = Field.ARTICLE;
        }

        if (article.hasSource()) {
            distance = incredible.getSourceScore() - article.getSourceScore();
            if (Math.abs(distance) > Math.abs(maxDistance)) {
                field = Field.SOURCE;
            }
        }

        int tot = incredible.getTotal();
        incredible.setTotal(tot + 1);

        switch (field) {
            case ARTICLE:
                incredible.setRelatednessScore((incredible.getRelatednesScores() * tot + article.getRelatednesScores()) / incredible.getTotal());
                break;
            case WEEK:
                incredible.setNumArticlesSameWeek((incredible.getNumArticlesSameWeek() * tot + article.getNumArticlesSameWeek()) / incredible.getTotal());
                break;
            case DAY:
                incredible.setNumArticlesSameDay((incredible.getNumArticlesSameDay() * tot + article.getNumArticlesSameDay()) / incredible.getTotal());
                break;
            case SOURCE:
                incredible.setSourceScore((incredible.getSourceScore() * tot + article.getSourceScore()) / incredible.getTotal());
                break;
        }

        currentFile = new File(INCREDIBLE_ARTICLE);
        writeFile(currentFile, incredible);

        return field;
    }

    private static void weight(Field field, Article art, float score) throws Exception{
        float newWeight;
        float oldWeight;
        Weights wght;

        if (art.hasSource()) {
            wght = weights;
        }
        else {
            wght = baseWeights;
        }

        switch (field) {
            case ARTICLE:
                oldWeight = wght.semWeight;
                newWeight = ((art.getRelatednesScores() * oldWeight) + (wght.cutoff - score)) / art.getRelatednesScores();
                wght.semWeight = (newWeight + wght.semWeight * wght.total) / (wght.total + 1);
                if (art.hasSource()) {
                    newWeight = (wght.semWeight - oldWeight) / 3;
                    wght.srcWeight -= newWeight;
                }
                else {
                    newWeight = (wght.semWeight - oldWeight) / 2;
                }
                wght.weekWeight -= newWeight;
                wght.dayWeight -= newWeight;
                break;
            case WEEK:
                oldWeight = wght.weekWeight;
                newWeight = (((art.getNumArticlesSameWeek() / 25) * oldWeight) + (wght.cutoff - score)) / (art.getNumArticlesSameWeek() / 25);
                wght.weekWeight = (newWeight + wght.weekWeight * wght.total) / (wght.total + 1);

                if (art.hasSource()) {
                    newWeight = (wght.weekWeight - oldWeight) / 3;
                    wght.srcWeight -= newWeight;
                }
                else {
                    newWeight = (wght.weekWeight - oldWeight) / 2;
                }
                wght.semWeight -= newWeight;
                wght.dayWeight -= newWeight;
                break;
            case DAY:
                oldWeight = wght.dayWeight;
                newWeight = (((art.getNumArticlesSameDay() / 25) * oldWeight) + (wght.cutoff - score)) / (art.getNumArticlesSameDay() / 25);
                wght.dayWeight = (newWeight + wght.dayWeight * wght.total) / (wght.total + 1);

                if (art.hasSource()) {
                    newWeight = (wght.dayWeight - oldWeight) / 3;
                    wght.srcWeight -= newWeight;
                }
                else {
                    newWeight = (wght.dayWeight - oldWeight) / 2;
                }
                wght.semWeight -= newWeight;
                wght.weekWeight -= newWeight;
                break;
            case SOURCE:
                oldWeight = wght.srcWeight;
                newWeight = ((art.getSourceScore() * oldWeight) + (wght.cutoff - score)) / art.getSourceScore();
                wght.srcWeight = (newWeight + wght.srcWeight * wght.total) / (wght.total + 1);
                newWeight = (wght.srcWeight - oldWeight) / 3;
                wght.weekWeight -= newWeight;
                wght.dayWeight -= newWeight;
                wght.semWeight -= newWeight;
                break;
        }
    }

    private static void initializeWeights() throws Exception{
        if (gson == null) {
            gson = new GsonBuilder().create();
            currentFile = new File(WEIGHT_PATH);
            weights = readFile(currentFile, Weights.class);
            currentFile = new File(BASE_PATH);
            baseWeights = readFile(currentFile, Weights.class);
        }
    }

    private static void initializeIdeal() throws Exception{
        currentFile = new File(IDEAL_ARTICLE);
        ideal = readFile(currentFile, Article.class);
        currentFile = new File(INCREDIBLE_ARTICLE);
        incredible = readFile(currentFile, Article.class);
    }

    private static <T> T readFile(File file, Class<T> clazz) throws Exception{
        T obj = null;
        try {
            FileReader reader = new FileReader(file);
            obj = gson.fromJson(reader, clazz);
            reader.close();
        }
        catch (Exception ex) {
            System.out.println("Could not read file: " + file.getPath());
            throw ex;
        }
        return obj;
    }

    private static <T> void writeFile(File file, T object) throws Exception {
        try{
            FileWriter writer = new FileWriter(file, false);
            writer.write(gson.toJson(object));
            writer.close();
        }
        catch (Exception ex) {
            System.out.println("Could not write file: " + file.getPath());
            throw ex;
        }
    }

    public static float getCutoff() {
        return weights.cutoff;
    }

    private static class Weights{
        public float weekWeight;
        public float dayWeight;
        public float semWeight;
        public float srcWeight;
        public float cutoff;
        public int total;
    }

    private enum Field {
        WEEK,
        DAY,
        ARTICLE,
        SOURCE    }
}
