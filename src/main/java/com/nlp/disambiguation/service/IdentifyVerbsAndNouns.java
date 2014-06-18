package com.nlp.disambiguation.service;

import com.nlp.disambiguation.model.PartOfSpeech;
import com.nlp.disambiguation.model.Word;

import java.util.*;

/**
 * Created by Dell on 6/1/14.
 */
public class IdentifyVerbsAndNouns {

    public static List<Word> getProcessedWords(String input){
        String output = ProcessText.processInputText(input);
        Map<String, PartOfSpeech> map = new HashMap<>();
        String[] inputArr = input.split("\\s");
        String[] outputArr = output.split("\\s");

        List<Word> list = new ArrayList<>();
        if(inputArr.length != outputArr.length){
            return null;
        } else {
            int len = inputArr.length;
            for(int i=0; i<len; i++){
                String inputStr = inputArr[i];
                String outputStr = outputArr[i];
                String[] currentWord = outputStr.split("\\|");
                if(inputStr.equals(currentWord[0])){
                    String partOfSpeechTag = currentWord[2];
                    if(partOfSpeechTag.length() >= 2){
                        char firstPosition = partOfSpeechTag.charAt(0);
                        char secondPosition = partOfSpeechTag.charAt(1);
//                        PartOfSpeech partOfSpeech = new PartOfSpeech();
                        Word word = new Word();
                        if(firstPosition == 'N' && secondPosition != 'P' ) {
//                            partOfSpeech.setSymbol('N');
//                            partOfSpeech.setName("noun");
                            word.setName(currentWord[1]);
                            word.setPartOfSpeech("s.");
                        } else if (firstPosition == 'V' && secondPosition != 'A') {
//                            partOfSpeech.setSymbol('V');
//                            partOfSpeech.setName("verb");
                            word.setName(currentWord[1]);
                            word.setPartOfSpeech("vb.");
                        } else {
                            continue;
                        }
                        list.add(word);
//                        map.put(currentWord[1], partOfSpeech);
                    }
                } else {
                    return null;
                }
            }
        }
        return list;
    }

    public static void main(String[] args){
        String input = "Maria va merge la munte !";
        List<Word> map = IdentifyVerbsAndNouns.getProcessedWords(input);

    }
}
