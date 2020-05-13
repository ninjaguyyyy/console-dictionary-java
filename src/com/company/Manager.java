package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Manager {
    private HashMap<String, String> viData;
    private HashMap<String, String> enData;
    BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(System.in, "utf8"));
    private int mode = 1;
    public Manager() throws UnsupportedEncodingException {
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

    public void showMenu() {
        System.out.println("==================== DICTIONARY CONSOLE ===================");
        System.out.println("\t 1. Chuyển đổi ngôn ngữ");
        System.out.println("\t 2. Tra cứu từ");
        System.out.println("\t 0. Thoát chương trình");

    }

    public void runProgram() throws IOException {
        loadXmlData(0);
        loadXmlData(1);
        do {
            showMenu();
            String modeName = (this.mode == 1)? "en - vi": "vi - en";
            System.out.println("---------- " + modeName + " ------------");
            System.out.print("Nhập lựa chọn: ");
            String chosenStr = bufferedReader.readLine();
            int chosenInt = Integer.parseInt(chosenStr);

            switch (chosenInt) {
                case 1:
                    System.out.println("---------- " + modeName + " ------------");
                    System.out.println("1. Anh - Việt");
                    System.out.println("2. Việt - Anh");
                    System.out.print("Nhập lựa chọn: ");
                    String chooseModeStr = bufferedReader.readLine();
                    this.mode = Integer.parseInt(chooseModeStr);
                    break;
                case 2:
                    System.out.println("---------- Tra từ " + modeName + " ------------");
                    System.out.print("Nhập từ cần tra: ");
                    String valueInput = bufferedReader.readLine();
                    String result;
                    if(this.mode == 1) {
                         result = enData.get(valueInput);
                    } else {
                        result = viData.get(valueInput);
                    }
                    if(result == null) {
                        System.out.println("Kết quả: Không tìm thấy");
                    } else {
                        System.out.println("Kết quả: " + result);
                    }
                    System.out.println("Enter de ve menu");
                    String done2 = bufferedReader.readLine();
                    break;
                case 3:
                    System.out.println("Da xoa thanh cong. Enter de ve menu");
                    String done3 = bufferedReader.readLine();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    System.out.println("Da export file thanh cong. Enter de ve menu");
                    String done5 = bufferedReader.readLine();
                    break;
                default:
                    return;
            }
        } while (true);
    }
    public void displayData() {
//        System.out.println(viData);
        System.out.println(enData.get("1 to 1 relationship"));
    }
}
