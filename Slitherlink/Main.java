package Slitherlink;

import Slitherlink.Elements.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dot bodka = new Dot();
        Line ciara = new Line();
        ciara.setLineState(LineState.MARKED);
        Field pole = new Field(10,10);

        for(int row = 0; row < pole.getRowCount(); row++){
            for(int column = 0; column < pole.getColumnCount(); column++){
                if(pole.getElement(row, column) instanceof Dot){
                    System.out.print(" · ");
                }
                else if(pole.getElement(row, column) instanceof Clue){
                    System.out.print(" N ");
                }
                else if(pole.getElement(row, column) instanceof Line){
                    System.out.print(" | ");
                }
                else {
                    System.out.print(pole.getElement(row, column));
                }
            }
            System.out.println();
        }
    }
}
