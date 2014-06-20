package com.nlp.disambiguation.service;

import com.nlp.disambiguation.model.ResultWord;
import com.nlp.disambiguation.model.Sense;
import com.nlp.disambiguation.model.Word;

import java.io.Console;
import java.io.IOException;
import java.util.*;

/**
 * Created by fgheorghe on 6/18/14.
 */
public class DisambiguateWords {

    static List<Word> wordList = new ArrayList<>();
    static List<ResultWord> resultWordList = new ArrayList<>();

    //TODO add a list of words to ignore
    static List<String> ignoredWords = new ArrayList<>();

    public static void popolateIgnoredWords(){

    }

    public static void getWords(String input) throws IOException{
        List<Word> verbsAndNouns = IdentifyVerbsAndNouns.getProcessedWords(input);
        Word processedWord;
        for(Word word:verbsAndNouns){
            processedWord = Search.search(word);
            wordList.add(processedWord);
        }
//        printList();
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
    }

    public static void disambiguateAllWords(){
        int size = wordList.size();
        for(int i = 0; i < size-2; i++){
            for(int j = i + 1; j < size-1; j++){
                for(int k = j + 1; k < size; k++){
                    if(j == i+1 && k == j+1){
                        disambiguateTriplet(wordList.get(i), wordList.get(j), wordList.get(k));
                    }

                }
            }
        }
    }

    public static void disambiguateTriplet(Word word1, Word word2, Word word3){
        if(!word1.hasOneSense() && !word2.hasOneSense() && !word3.hasOneSense()){
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
                if(word1.hasSense() && !word1.isDisambiguated()){
                    resultWord = new ResultWord(word1.getName(), word1.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                    word1.setDisambiguated(true);
                }
                if(word2.hasSense() && !word2.isDisambiguated()){
                    resultWord = new ResultWord(word2.getName(), word2.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                    word2.setDisambiguated(true);
                }
                if(word3.hasSense() && !word3.isDisambiguated()){
                    resultWord =  new ResultWord(word3.getName(), word3.getDefinitions().get(0));
                    resultWordList.add(resultWord);
                    word3.setDisambiguated(true);
                }
            } else {
                if(word1.hasSense() && !word1.isDisambiguated()){
                    resultWord = new ResultWord(word1.getName(), word1.getDefinitions().get(maxSenses[0]));
                    resultWordList.add(resultWord);
                    word1.setDisambiguated(true);
                }
                if(word2.hasSense() &&!word2.isDisambiguated()){
                    resultWord = new ResultWord(word2.getName(), word2.getDefinitions().get(maxSenses[1]));
                    resultWordList.add(resultWord);
                    word2.setDisambiguated(true);
                }
                if(word3.hasSense() &&!word3.isDisambiguated()){
                    resultWord =  new ResultWord(word3.getName(), word3.getDefinitions().get(maxSenses[2]));
                    resultWordList.add(resultWord);
                    word3.setDisambiguated(true);
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
        return listOfWords;
    }

    public static void printResult(){
        for(ResultWord resultWord : resultWordList){
            System.out.println(resultWord);
        }
    }

    public static void printList(){
        for(Word word : wordList){
            System.out.println(word);
        }
        System.out.println("---------------------------");
    }
    public static void main(String[] args) throws Exception{
        String input = "Maria a plecat pe litoral cu sora ei ! Ele au calatorit cu trenul . Drumul a fost lung . Drumul a fost usor . ";

        System.out.println("Give the text: ");
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        getWords(text);
        getWordsWithOneSense();
        disambiguateAllWords();
        printResult();
    }

}
