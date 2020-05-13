package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Manager {
    private HashMap<String, String> viData;
    private HashMap<String, String> enData;
    private Set<String> favoriteEnWord;
    private Set<String> favoriteViWord;
    BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(System.in, "utf8"));
    private int mode = 1;   // 1: en-vi

    public Manager() throws UnsupportedEncodingException {
        this.viData = new HashMap<>();
        this.enData = new HashMap<>();
        this.favoriteEnWord = new TreeSet<>();
        this.favoriteViWord = new TreeSet<>();
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

    public void loadFavData(int type) throws IOException {
        String fileNameInput = (type == 1)? "./src/data/en_vi_favorite.txt": "./src/data/vi_en_favorite.txt";
        BufferedReader inputFile = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileNameInput), StandardCharsets.UTF_8
                )
        );
        String line;
        if(type == 1) {
            favoriteEnWord.clear();
            while ((line = inputFile.readLine()) != null) {
                favoriteEnWord.add(line);
            }
        } else {
            favoriteViWord.clear();
            while ((line = inputFile.readLine()) != null) {
                favoriteViWord.add(line);
            }
        }
    }

    public void saveFavData(int type) throws IOException {
        String filename = (this.mode == 1)? "./src/data/en_vi_favorite.txt": "./src/data/vi_en_favorite.txt";
        PrintStream printStream = new PrintStream(new FileOutputStream(filename));
        if (type == 1) {
            for(String item : this.favoriteEnWord) {
                printStream.println(item);
            }

        } else {
            for(String item : this.favoriteViWord) {
                printStream.println(item);
            }
        }
        printStream.flush();
        printStream.close();
    }

    public void showFavData(TreeSet<String> data, int typeShow) {
        if(typeShow == 1) { // asc
            for(String word : data) {
                System.out.println(word);
            }
        } else {
            TreeSet<String> dataReserve = (TreeSet<String>) data.descendingSet();
            for(String word : dataReserve) {
                System.out.println(word);
            }
        }
    }

    public void showMenu() {
        System.out.println("==================== DICTIONARY CONSOLE ===================");
        System.out.println("\t 1. Chuyển đổi ngôn ngữ");
        System.out.println("\t 2. Tra cứu từ");
        System.out.println("\t 3. Thêm từ điển");
        System.out.println("\t 4. Xóa từ điển");
        System.out.println("\t 5. Thêm từ điển vào danh sách yêu thích");
        System.out.println("\t 6. Xem danh sách yêu thích");
        System.out.println("\t 6. Thống kê tần suất");
        System.out.println("\t 0. Thoát chương trình");
    }

    public void runProgram() throws IOException {
        loadXmlData(0);
        loadXmlData(1);
        loadFavData(0);
        loadFavData(1);
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
                    System.out.println("---------- Thêm từ " + modeName + " ------------");
                    System.out.print("Nhập từ muốn thêm: ");
                    String wordInput = bufferedReader.readLine();
                    System.out.print("Nhập nghĩa của từ: ");
                    String meaningInput = bufferedReader.readLine();
                    if(this.mode == 1) {
                        enData.put(wordInput, meaningInput);
                    } else {
                        viData.put(wordInput, meaningInput);
                    }
                    System.out.println("Đã thêm thành công. Enter de ve menu");
                    String done3 = bufferedReader.readLine();
                    break;
                case 4:
                    System.out.println("---------- Xóa từ " + modeName + " ------------");
                    System.out.print("Nhập từ muốn xóa: ");
                    String wordDelInput = bufferedReader.readLine();
                    if(this.mode == 1) {
                        enData.remove(wordDelInput);
                    } else {
                        viData.remove(wordDelInput);
                    }
                    System.out.println("Đã xóa thành công. Enter để về menu");
                    String done4 = bufferedReader.readLine();
                    break;
                case 5:
                    System.out.println("---------- Thêm từ vào danh sách yêu thích " + modeName + " ------------");
                    System.out.println("!!! Chú ý: có 2 danh sách yêu thích tương ứng 2 chế độ (vi - en và en - vi) nên hãy chú ý đến chế độ hiện tại để thêm vào danh sách hợp lý.");
                    System.out.print("Nhập từ yêu thích: ");
                    String wordFavInput = bufferedReader.readLine();
                    if(this.mode == 1) {
                        this.favoriteEnWord.add(wordFavInput);
                        saveFavData(1);
                    } else {
                        this.favoriteViWord.add(wordFavInput);
                        saveFavData(0);
                    }
                    System.out.println("Đã thêm vào danh sách yêu thích thành công. Enter để về menu");
                    String done5 = bufferedReader.readLine();
                    break;
                case 6:
                    System.out.println("---------- Xem danh sách yêu thích " + modeName + " ------------");
                    System.out.println("!!! Chú ý: có 2 danh sách yêu thích tương ứng 2 chế độ (vi - en và en - vi) nên hãy chú ý đến chế độ hiện tại để thêm vào danh sách hợp lý.");
                    System.out.println("1. Sắp xếp từ A-Z");
                    System.out.println("2. Sắp xếp từ Z-A");
                    System.out.print("Nhập lựa chọn: ");
                    String chooseModeSort = bufferedReader.readLine();
                    if(this.mode == 1) {
                        showFavData((TreeSet<String>)this.favoriteEnWord, Integer.parseInt(chooseModeSort));
                    }
                    else {
                        showFavData((TreeSet<String>)this.favoriteViWord, Integer.parseInt(chooseModeSort));
                    }
                    System.out.println("Da export file thanh cong. Enter de ve menu");
                    String done6 = bufferedReader.readLine();
                    break;
                case 7:

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
