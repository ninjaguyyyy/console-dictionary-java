package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

public class Manager {
    private HashMap<String, String> viData;
    private HashMap<String, String> enData;

    public Manager() {
        this.viData = new HashMap<>();
        this.enData = new HashMap<>();
    }

    public void loadXmlData(int type) {
        String fileName = (type == 1)? "Anh_Viet.xml": "Viet_Anh.xml";
        try {
            File xmlFile = new File("./src/data/" + fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("record");
            if(type == 1) {
                for(int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    Element element = (Element) nNode;
                    enData.put(
                            element.getElementsByTagName("word").item(0).getTextContent(),
                            element.getElementsByTagName("meaning").item(0).getTextContent()
                    );
                }
            } else {
                for(int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    Element element = (Element) nNode;
                    viData.put(
                            element.getElementsByTagName("word").item(0).getTextContent(),
                            element.getElementsByTagName("meaning").item(0).getTextContent()
                    );

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayData() {
//        System.out.println(viData);
        System.out.println(enData.get("1 to 1 relationship"));
    }
}
