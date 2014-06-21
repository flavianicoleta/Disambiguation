package com.nlp.disambiguation.service;

import com.nlp.disambiguation.model.Word;

import java.util.*;

public class IdentifyVerbsAndNouns {

    public static List<Word> getProcessedWords(String input){
        String output = ProcessText.processInputText(input);
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
                        Word word = new Word();
                        if(firstPosition == 'N' && secondPosition != 'P' ) {
                            word.setName(currentWord[1]);
                            word.setPartOfSpeech("s.");
                        } else if (firstPosition == 'V' && secondPosition != 'A') {
                            word.setName(currentWord[1]);
                            word.setPartOfSpeech("vb.");
                        } else {
                            continue;
                        }
                        list.add(word);
                    }
                } else {
                    return null;
                }
            }
        }
        return list;
    }
}
