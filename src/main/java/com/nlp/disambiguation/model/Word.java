package com.nlp.disambiguation.model;

import java.util.ArrayList;
import java.util.List;


public class Word {

	String name;
	String partOfSpeech;
	List<Sense> definitions;
	List<String> synonyms;
	List<String> antonyms;
    boolean disambiguated;

    public Word(){

    }

	public Word(String name, String partOfSpeech) {
		super();
		this.name = name;
		this.partOfSpeech = partOfSpeech;
		definitions = new ArrayList<>();
		synonyms = new ArrayList<>();
		antonyms = new ArrayList<>();
        disambiguated = false;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPartOfSpeech() {
		return partOfSpeech;
	}
	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	public List<Sense> getDefinitions() {
		return definitions;
	}

    public boolean isDisambiguated() {
        return disambiguated;
    }

    public void setDisambiguated(boolean disambiguated) {
        this.disambiguated = disambiguated;
    }

    public void addDefinition(Sense definition) {
		this.definitions.add(definition);
	}
	public List<String> getSynonyms() {
		return synonyms;
	}
	public void addSynonym(String synonym) {
		this.synonyms.add(synonym);
	}
	public List<String> getAntonyms() {
		return antonyms;
	}
	public void setAntonyms(List<String> antonyms) {
		this.antonyms = antonyms;
	}

    public boolean hasOneSense(){
        return (definitions.size() == 1);
    }

	@Override
	public String toString() {
		return "Word [name=" + name + ", partOfSpeech=" + partOfSpeech
				+ ", definitions=" + definitions + ", synonyms=" + synonyms
				+ ", antonyms=" + antonyms + "]";
	}
	

}
