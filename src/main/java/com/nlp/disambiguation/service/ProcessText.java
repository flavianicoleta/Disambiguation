package com.nlp.disambiguation.service;

import com.nlp.disambiguation.model.PartOfSpeech;

import java.util.*;

/**
 * Created by Dell on 6/1/14.
 */
public class ProcessText {

    public static Map<String, PartOfSpeech> getProcessedWords(String input, String output){
        Map<String, PartOfSpeech> map = new HashMap<>();
        String[] inputArr = input.split("\\s");
        String[] outputArr = output.split("\\s");

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
                        PartOfSpeech partOfSpeech = new PartOfSpeech();
                        if(firstPosition == 'N' && secondPosition != 'P' ) {
                            partOfSpeech.setSymbol('N');
                            partOfSpeech.setName("noun");
                        } else if (firstPosition == 'V' && secondPosition != 'A') {
                            partOfSpeech.setSymbol('V');
                            partOfSpeech.setName("verb");
                        } else {
                            continue;
                        }
                        map.put(currentWord[1], partOfSpeech);
                    }
                } else {
                    return null;
                }
            }
        }
        return map;
    }

    public static void main(String[] args){
        String input = "Maria va merge la mare !";
        String output = "Maria|Maria|NP|Np va|vrea|VA3S|Va--3s merge|merge|VN|Vmnp la|la|S|Spsa mare|mare|NSRN|Ncfsrn !|!|EXCL|EXCL";
        Map<String, PartOfSpeech> map = ProcessText.getProcessedWords(input, output);
        Set<String> keySet = map.keySet();
        for(String key:keySet){
            System.out.println("key: " + key + ";\t value: " + map.get(key));
        }
    }
}