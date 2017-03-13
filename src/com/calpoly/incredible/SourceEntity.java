package com.calpoly.incredible;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * Created by Zack Cody on 3/8/2017.
 */
public class SourceEntity extends TableServiceEntity {
    private String name;
    private double score;
    private int numArticles;

    public SourceEntity(String name) {
        this.partitionKey = name;
        this.rowKey = Backend.ROW_KEY;
    }

    public SourceEntity() {}

    public String getName() {return this.partitionKey;}
    public double getScore() {return this.score;}
    public int getNumArticles() {return this.numArticles;}
    public void setName(String name) {this.name = name;}
    public void setScore(double newScore) {this.score = newScore;}
    public void setNumArticles(int articles) {this.numArticles = articles;}

    @Override
    public String toString() {
        return "Name: " + this.getPartitionKey() + " Score: " + this.score + "\n";
    }
}
