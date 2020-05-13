package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Manager {
    private HashMap<String, String> viData;
    private HashMap<String, String> enData;
    private Set<String> favoriteEnWord;
    private Set<String> favoriteViWord;
    private ArrayList<WordHistory> wordHistories;
    BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(System.in, "utf8"));
    private int mode = 1;   // 1: en-vi

    public Manager() throws UnsupportedEncodingException {
        this.viData = new HashMap<>();
        this.enData = new HashMap<>();
        this.favoriteEnWord = new TreeSet<>();
        this.favoriteViWord = new TreeSet<>();
        this.wordHistories = new ArrayList<>();
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

    public void loadHistoryData() throws IOException {
        BufferedReader inputFile = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("./src/data/history.txt"), StandardCharsets.UTF_8
                )
        );
        String line;
        wordHistories.clear();
        while ((line = inputFile.readLine()) != null) {
            String[] splitedLine = line.split(",");
            wordHistories.add(new WordHistory(splitedLine[0], Long.parseLong(splitedLine[1])));
        }
    }

    public void saveHistoryData() throws IOException {
        PrintStream printStream = new PrintStream(new FileOutputStream("./src/data/history.txt"));
        for(WordHistory item : this.wordHistories) {
            printStream.println(item.getWord() + "," + item.getAtDate());
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
        System.out.println("\t 7. Thống kê tần suất tra các từ");
        System.out.println("\t 0. Thoát chương trình");
    }

    private static void findDuplicatesAndRender(ArrayList<WordHistory> inputArray) {
        HashMap<String, Integer> map = new HashMap<>();
        for (WordHistory element : inputArray) {
            if(map.get(element.getWord()) == null) {
                map.put(element.getWord(), 1);
            }
            else {
                map.put(element.getWord(), map.get(element.getWord())+1);
            }
        }

        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        if(map.size() == 0) {
            System.out.println("Không có lịch sử tra từ vào lúc này");
        } else {
            for (Map.Entry<String, Integer> entry : entrySet) {
                System.out.println("Từ: "+entry.getKey()+" - đã tra "+entry.getValue()+" lần.");

            }
        }

    }

    private ArrayList<WordHistory> findHistoryByDate(String dateStartStr, String dateEndStr, ArrayList<WordHistory> list) throws ParseException {
        ArrayList<WordHistory> results = new ArrayList<>();
        Date dateStart = new SimpleDateFormat("dd-MM-yyyy").parse(dateStartStr);
        Date dateEnd = new SimpleDateFormat("dd-MM-yyyy").parse(dateEndStr);
        for (WordHistory recentWord : list) {
            Date wordDate = new Date(recentWord.getAtDate());
            if (wordDate.before(dateEnd) && wordDate.after(dateStart)) {
                results.add(recentWord);
            }
        }
        return results;
    }

    public void runProgram() throws IOException, ParseException {
        loadXmlData(0);
        loadXmlData(1);
        // load
        loadFavData(0);
        loadFavData(1);
        loadHistoryData();
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
                    this.wordHistories.add(new WordHistory(valueInput, (new Date()).getTime()));
                    saveHistoryData();
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
                    System.out.println("---------- Thống kê từ đã tra ------------");
                    System.out.println("Nhập ngày bắt đầu (dd-mm-yyyy): ");
                    String dateStartInput = bufferedReader.readLine();
                    System.out.println("Nhập ngày kết thúc (dd-mm-yyyy): ");
                    String dateEndInput = bufferedReader.readLine();
                    System.out.println("Lịch sử tra từ: " + dateStartInput + " đến " + dateEndInput);
                    findDuplicatesAndRender(findHistoryByDate(dateStartInput, dateEndInput, this.wordHistories));
                    System.out.println("Enter de ve menu");
                    String done7 = bufferedReader.readLine();
                    break;
                default:
                    saveHistoryData();
                    return;
            }
        } while (true);
    }
}
