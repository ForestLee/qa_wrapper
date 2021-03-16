package com.forest.insurance;

public class Insurance {
    private int index;
    private Double distance;
    private String question;

    public Insurance(int index, Double distance, String label) {
        this.index = index;
        this.distance = distance;
        this.question = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public static Insurance create(int index, Double distance) {
        Insurance userManual = new Insurance(index, distance, "");
        return userManual;
    }

    @Override
    public String toString() {
        return "{" +
                "index:" + index +
                ", distance:" + distance +
                ", label:'" + question + '\'' +
                '}';
    }
}
