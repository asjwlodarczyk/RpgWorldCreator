package com.aswlodarczyk.rpgworldcreator.Communication;

public class Printer implements Output {

    public void print(String s) {
        System.out.print(s);
    }

    public void println(String s) {
        System.out.println(s);
    }
}
