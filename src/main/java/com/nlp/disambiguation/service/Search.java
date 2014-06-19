package com.nlp.disambiguation.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nlp.disambiguation.model.Sense;
import com.nlp.disambiguation.model.Word;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Search {

	//static Word cuvant = new Word("veselie","s.");

	private static String searchDEX(Word cuvant) throws IOException{
		URL url = new URL("http://m.dexonline.ro/definitie/"+cuvant.getName());
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Content-length", "0");
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.connect();
        int status = conn.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();
        }
		return null;
	  
	}
	
	public static boolean isNumeric(String str)  {  
		return str.matches("\\d+\\.+");
	} //check if number
	
	private static void findDefinition(Elements definitions, Word cuvant){
		
        //take the first definition 
        //and check if it is the right one
        Element definition = definitions.first();
        int index = 1;
        Elements parts = definition.getElementsByClass("abbrev");
        String part = null;
        boolean found = false;
        for (int i = 0; i < parts.size()-1; i++){
        	part = parts.get(i).text();
        	if (part.contains(cuvant.getPartOfSpeech())){
        		found = true;
        		break;
        	}
        }
        while (!found){
        	//search for the right set of definitions
        	definition = definitions.get(index);
        	parts = definition.getElementsByClass("abbrev");
        	for (int i = 0; i < parts.size()-1; i++){
        		part = parts.get(i).text();
        		if (part.contains(cuvant.getPartOfSpeech())){
        			found = true;
        			break;
        		}
        	}
			index++;
		}

		// when the right set of definitions is selected, use them
		Elements set_definitions = definition.getElementsByTag("b");
		if (set_definitions.first().nextElementSibling().text().contains(cuvant.getPartOfSpeech())){
			//System.out.println(set_definitions.first().nextElementSibling().nextSibling().toString());
            Sense def = new Sense(0, set_definitions.first().nextElementSibling().nextSibling().toString());

			cuvant.addDefinition(def);
		}
		boolean ok = true;
		for (int i = 0; i < set_definitions.size() - 2; i++) {
			
			if (set_definitions.get(i+1 ).nextSibling().toString().equals(" ")) {
				if (!set_definitions.get(i+1 ).nextElementSibling().nextSibling().toString().equals(" ")) {
                    Sense def = new Sense(0, set_definitions.get(i+1 ).nextElementSibling().nextSibling().toString());
					cuvant.addDefinition(def);
				}
				else
				if (set_definitions.get(i + 1).nextElementSibling().text().compareToIgnoreCase(cuvant.getPartOfSpeech())
						== 0) 
					ok = true;
				else
					ok = false;
			}
			if ((ok == true) && 
					(!set_definitions.get(i + 1).nextSibling().toString().equals(" ")) && 
					(set_definitions.get(i + 1).nextSibling().toString().length() > 5) &&
					(isNumeric(set_definitions.get(i+1).text()))){
                Sense def = new Sense(0, set_definitions.get(i + 1).nextSibling().toString());
				cuvant.addDefinition(def);
			}
				
		}

	}
	
	private static void findSynonymsAndAntonyms(Elements sources, Elements definitions, Word cuvant){
		
        int count = 0;
        for (Element source: sources){
        	//check for each definition if it is from "Sinonime"
        	if (source.text().contains("Sinonime")){
        		Element definition = definitions.get(count);
        		Elements set_definitions = definition.getElementsByTag("b");
        		String part = set_definitions.first().nextSibling().toString();
        		//if the part of speech is the right one
        		if (part.contains(cuvant.getPartOfSpeech()))
        		for (int i = 0; i < set_definitions.size()-1; i++){
        			cuvant.addSynonym(set_definitions.get(i+1).nextSibling() 
                			+set_definitions.get(i+1).nextElementSibling().text()
                			+set_definitions.get(i+1).nextElementSibling().nextSibling());
                }
        	}
        	if (source.text().contains("Antonime")){
        		Element definition = definitions.get(count);
        		Elements set_definitions = definition.getElementsByTag("b");
        		cuvant.setAntonyms(Arrays.asList(set_definitions.first().nextSibling().toString().substring(3).
						split(", ")));
         	}
        	count++;
        }
	}

	public static Element getResults(String HTML){
		Document doc = Jsoup.parse(HTML);
//        System.out.println("Title: " + doc.getElementsByTag("title").text());
        //set of all definitions
        Element results = doc.getElementById("resultsWrapper");
        return results;
	}


	public static Word search(Word cuvant) throws IOException{
		String jsonReply = searchDEX(cuvant);
	    Element results = getResults(jsonReply);
	    Elements sources = results.getElementsByClass("defDetails"); //all sources
	    Elements definitions = results.getElementsByClass("def"); //all definitions
	    
	    
	    findDefinition(definitions, cuvant);
//	    System.out.println("Definitions:");
	    for (int i = 0; i < cuvant.getDefinitions().size(); i++){
            cuvant.getDefinitions().get(i).setSense(i+1);
//	    	System.out.println(i+": "+cuvant.getDefinitions().get(i));
	    }
	    
	    findSynonymsAndAntonyms(sources, definitions, cuvant);
//	    System.out.println("Synonyms:");
	    for (int i = 0; i < cuvant.getSynonyms().size(); i++){
	    	System.out.println(i+": "+cuvant.getSynonyms().get(i));
	    }
//	    System.out.println("Antonyms:");
//	    for (int i = 0; i < cuvant.getAntonyms().size(); i++){
//	    	System.out.println(i+": "+cuvant.getAntonyms().get(i));
//	    }
//        System.out.println(cuvant);
        return cuvant;
	}
	
	public static void main(String[] args) throws IOException {
        Word cuvant = new Word("conduce","vb.");
		Word newWord = search(cuvant);
        System.out.println(newWord);
//
	}
}
