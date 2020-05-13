package com.company;

public class WordHistory {
    private String word;
    private long atDate;

    public WordHistory(String word, long atDate) {
        this.word = word;
        this.atDate = atDate;
    }

    public String getWord() {
        return word;
    }

    public long getAtDate() {
        return atDate;
    }

}
