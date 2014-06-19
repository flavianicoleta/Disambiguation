package com.nlp.disambiguation.service;

import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Dell on 6/15/14.
 */
public class ProcessText {
    private static String getResponse(String input){
        String result = "";
        try{
            String xmldata ="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "  <soap12:Body>\n" +
                    "    <Process xmlns=\"http://nlp.racai.ro/WebServices\">\n" +
                    "      <input>"+input+"</input>\n" +
                    "      <lang>ro</lang>\n" +
                    "    </Process>\n" +
                    "  </soap12:Body>\n" +
                    "</soap12:Envelope>";

            String hostname = "tutankhamon.racai.ro";
            int port = 80;
            InetAddress address = InetAddress.getByName(hostname);
            Socket socket = new Socket(address, port);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("POST /webservices/TextProcessing.asmx HTTP/1.0\r\n");
            bufferedWriter.write("Host: tutankhamon.racai.ro\r\n");
            bufferedWriter.write("Content-Type: application/soap+xml; charset=\"utf-8\"\r\n");
            bufferedWriter.write("Content-Length: " + xmldata.length() + "\r\n");
            bufferedWriter.write("\r\n");

            bufferedWriter.write(xmldata);
            bufferedWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            boolean ok = false;
            while ((line = bufferedReader.readLine()) != null){
                if(line.startsWith("<?xml") || ok) {
                    ok = true;
                    result += line;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static String getProcessedText(String xml){
        String result = null;
        try{
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            Node processResult = doc.getElementsByTagName("ProcessResponse").item(0);
            result = processResult.getTextContent();

        } catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println(result);
        return result;
    }

    public static String processInputText(String input){
        return getProcessedText(getResponse(input));
    }

    public static void main(String[] args){
        processInputText("Maria a plecat la mare cu sora ei ! Ele au mers cu trenul .");
    }

}
