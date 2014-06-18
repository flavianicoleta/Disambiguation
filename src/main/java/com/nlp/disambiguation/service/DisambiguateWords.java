package com.nlp.disambiguation.service;

import com.nlp.disambiguation.model.ResultWord;
import com.nlp.disambiguation.model.Sense;
import com.nlp.disambiguation.model.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgheorghe on 6/18/14.
 */
public class DisambiguateWords {

    static List<Word> wordList = new ArrayList<>();
    static List<ResultWord> resultWordList = new ArrayList<>();

    //TODO add a list of words to ignore
    static List<String> ignoredWords = new ArrayList<>();

    public static void getWords(String input) throws IOException{
        List<Word> verbsAndNouns = IdentifyVerbsAndNouns.getProcessedWords(input);
        Word processedWord;
        for(Word word:verbsAndNouns){
            processedWord = Search.search(word);
            wordList.add(processedWord);
        }
    }

    public static void getWordsWithOneSense(){
        ResultWord resultWord;
        for(Word word:wordList){
            if(word.hasOneSense()){
                resultWord = new ResultWord(word.getName(), word.getDefinitions().get(0));
                resultWordList.add(resultWord);
                word.setDisambiguated(true);
            }
        }

        //disambiguate synonyms

//        for(Word disambiguatedWord:wordList){
//            if(disambiguatedWord.isDisambiguated()){
//                for(String synonym:disambiguatedWord.getSynonyms()){
//                    for(Word nondisambiguateddWord:wordList){
//                        if(!nondisambiguateddWord.isDisambiguated()){
//                            if(synonym.contains(nondisambiguateddWord.getName())){
//                                resultWord = new ResultWord(nondisambiguateddWord.getName(),disambiguatedWord.getDefinitions().get(0));
//                                resultWordList.add(resultWord);
//                                nondisambiguateddWord.setDisambiguated(true);
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    public static void disambiguateAllWords(){
        int size = wordList.size();
        for(int i = 0; i < size-2; i++){
            for(int j = i + 1; j < size-1; j++){
                for(int k = j + 1; k < size; k++){
                    disambiguateTriplet(wordList.get(i), wordList.get(j), wordList.get(k));
                }
            }
        }
    }

    public static void disambiguateTriplet(Word word1, Word word2, Word word3){
        if(!word1.hasOneSense() || word2.hasOneSense() || word3.hasOneSense()){
            int[] currentSenses;
            Map<int[], Double> scores = new HashMap<>();
            for(Sense sense1 : word1.getDefinitions()){
                for(Sense sense2 : word2.getDefinitions()){
                    for(Sense sense3 : word3.getDefinitions()){
                        double score = computeScore(sense1, sense2, sense3);
                        currentSenses = new int[3];
                        currentSenses[0] = sense1.getSense();
                        currentSenses[1] = sense2.getSense();
                        currentSenses[2] = sense3.getSense();
                        scores.put(currentSenses, score);
                    }
                }
            }
            int[] maxSenses = new int[3];
            double maxScore = 0.0;
            for(int[] senses : scores.keySet()){
                double currentScore = scores.get(senses);
                if(currentScore > maxScore){
                    maxScore = currentScore;
                    maxSenses = senses;
                }
            }
            ResultWord resultWord;
            if(maxScore == 0.0){
                if(!word1.hasOneSense()){
                    resultWord = new ResultWord(word1.getName(), word1.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                }
                if(!word2.hasOneSense()){
                    resultWord = new ResultWord(word2.getName(), word2.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                }
                if(!word3.hasOneSense()){
                    resultWord =  new ResultWord(word3.getName(), word3.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                }
            } else {
                if(!word1.hasOneSense()){
                    resultWord = new ResultWord(word1.getName(), word1.getDefinitions().get(maxSenses[0]));
                    resultWordList.add(resultWord);
                }
                if(!word2.hasOneSense()){
                    resultWord = new ResultWord(word2.getName(), word2.getDefinitions().get(maxSenses[1]));
                    resultWordList.add(resultWord);
                }
                if(!word3.hasOneSense()){
                    resultWord =  new ResultWord(word3.getName(), word3.getDefinitions().get(maxSenses[2]));
                    resultWordList.add(resultWord);
                }
            }

        }
    }

    public static double computeScore(Sense sense1, Sense sense2, Sense sense3){
        List<String> defWords1 = processDefinition(sense1.getDefinition());
        List<String> defWords2 = processDefinition(sense2.getDefinition());
        List<String> defWords3 = processDefinition(sense3.getDefinition());
        int totalNoOfWords = defWords1.size() + defWords2.size() + defWords3.size();
        int commonNoOfWords = 0;

        for(String defWord1 : defWords1){
            for(String defWord2 : defWords2){
                for(String defWord3 : defWords3){
                    if(defWord1.equals(defWord2) && defWord1.equals(defWord3) && defWord2.equals(defWord3)){
                        commonNoOfWords++;
                    }
                }
            }
        }
        double score = 3 * commonNoOfWords / totalNoOfWords;
        return score;
    }

    public static List<String> processDefinition(String definition){
        List<String> listOfWords = new ArrayList<>();
        String[] defWords = definition.split("\\s");
        for(String word : defWords){
            if(!ignoredWords.contains(word)){
                listOfWords.add(word);
            }
        }
        //TODO split words and exclude ignored words
        return listOfWords;
    }

}
