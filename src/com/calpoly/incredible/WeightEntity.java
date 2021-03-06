package com.calpoly.incredible;

/**
 * Created by Zack Cody on 3/8/2017.
 */
public class WeightEntity extends SourceEntity {
    private String name;
    private double value;

    public WeightEntity (String name) {
        this.partitionKey = name;
        this.rowKey = Backend.ROW_KEY;
    }

    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}
    public double getValue() {return this.value;}
    public void setValue(double value) {this.value = value;}
}
