package org.model;

import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(20, 15);
        System.out.println(map.toString());
    }
}