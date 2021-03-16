package com.forest.insurance;

public class InsuranceRequest {
    private String text;
    private String nbest;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNbest() {
        return nbest;
    }

    public void setNbest(String nbest) {
        this.nbest = nbest;
    }

    @Override
    public String toString() {
        return "{" +
                "text='" + text + '\'' +
                ", nbest='" + nbest + '\'' +
                '}';
    }
}
