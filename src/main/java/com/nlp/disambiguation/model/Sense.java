package com.nlp.disambiguation.model;

/**
 * Created by fgheorghe on 6/18/14.
 */
public class Sense {
    private int sense;
    private String definition;

    public Sense() {
    }

    public Sense(int sense, String definition) {
        this.sense = sense;
        this.definition = definition;
    }

    public int getSense() {
        return sense;
    }

    public void setSense(int sense) {
        this.sense = sense;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Sense{" +
                "sense='" + sense + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
