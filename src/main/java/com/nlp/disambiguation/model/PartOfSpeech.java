package com.nlp.disambiguation.model;

/**
 * Created by Dell on 6/1/14.
 */
public class PartOfSpeech {

    private char symbol;
    private String name;

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PartOfSpeech{" +
                "symbol=" + symbol +
                ", name='" + name + '\'' +
                '}';
    }
}
