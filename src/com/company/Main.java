package com.company;

public class Main {

    public static void main(String[] args) {
	    Manager manager = new Manager();
	    manager.loadXmlData(1);
	    manager.loadXmlData(0);
	    manager.displayData();
    }
}
