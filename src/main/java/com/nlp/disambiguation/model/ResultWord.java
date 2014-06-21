package com.nlp.disambiguation.model;

public class ResultWord {
    private String word;
    private Sense sense;

    public ResultWord(String word, Sense sense) {
        this.word = word;
        this.sense = sense;
    }

    @Override
    public String toString() {
        return "word: "+ word + "\t - \t" + "definition: " + sense.getDefinition() + "\n";
    }
}
