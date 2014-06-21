package com.nlp.disambiguation.model;

public class Sense {
    private int senseNumber;
    private String definition;

    public Sense(int senseNumber, String definition) {
        this.senseNumber = senseNumber;
        this.definition = definition;
    }

    public int getSenseNumber() {
        return senseNumber;
    }

    public void setSenseNumber(int senseNumber) {
        this.senseNumber = senseNumber;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return "Sense{" +
                "senseNumber='" + senseNumber + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
