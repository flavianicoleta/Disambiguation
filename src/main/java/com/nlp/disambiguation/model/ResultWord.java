package com.nlp.disambiguation.model;

/**
 * Created by fgheorghe on 6/18/14.
 */
public class ResultWord {
    private String word;
    private Sense sense;

    public ResultWord() {
    }

    public ResultWord(String word, Sense sense) {
        this.word = word;
        this.sense = sense;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    @Override
    public String toString() {
        return "ResultWord{" +
                "word='" + word + '\'' +
                ", sense=" + sense +
                '}';
    }
}
