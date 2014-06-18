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
            if(word.getDefinitions().size() == 1){
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

    public static void disambiguateTriplet(Word word1, Word word2, Word word3){
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
        ResultWord resultWord1 = new ResultWord(word1.getName(), word1.getDefinitions().get(maxSenses[0]));
        ResultWord resultWord2 = new ResultWord(word2.getName(), word2.getDefinitions().get(maxSenses[1]));
        ResultWord resultWord3 = new ResultWord(word3.getName(), word3.getDefinitions().get(maxSenses[2]));
        
        resultWordList.add(resultWord1);
        resultWordList.add(resultWord2);
        resultWordList.add(resultWord3);
    }

    public static double computeScore(Sense sense1, Sense sense2, Sense sense3){
        return 0.0;
    }


}
