package Slitherlink;

import Slitherlink.Elements.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        //Dot bodka = new Dot();
        //Line ciara = new Line();
        //ciara.setLineState(LineState.MARKED);
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

        System.out.println();
        pole.drawLine(1,0);
        pole.drawLine(2,0);
        pole.drawLine(2,1);
        pole.markLine(4,0);
        pole.markLine(5,0);
        for(int row = 0; row < pole.getRowCount(); row++){
            for(int column = 0; column < pole.getColumnCount(); column++){
                if(pole.getElement(row, column) instanceof Dot){
                    System.out.print(" · ");
                }
                else if(pole.getElement(row, column) instanceof Clue){
                    System.out.print(" N ");
                }
                else if(pole.getElement(row, column) instanceof Line){
                    if(((Line) pole.getElement(row, column)).getLineState()==LineState.DRAWN) {
                        System.out.print(" / ");
                    }
                    else if(((Line) pole.getElement(row, column)).getLineState()==LineState.MARKED){
                        System.out.print(" x ");
                    }
                    else{
                        System.out.print(" | ");
                    }
                }
                else {
                    System.out.print(pole.getElement(row, column));
                }
            }
            System.out.println();
        }
        System.out.println(pole.isSolved());

        System.out.println();
        for(int row = 0; row < pole.getRowCount(); row++){
            for(int column = 0; column < pole.getColumnCount(); column++){
                if(pole.getElement(row, column) instanceof Dot){
                    System.out.print(" · ");
                }
                else if(pole.getElement(row, column) instanceof Clue){
                    System.out.print(" N ");
                }
                else if(pole.getElement(row, column) instanceof Line){
                    if(((Line) pole.getElement(row, column)).isBeing()) {
                        System.out.print(" + ");
                    }
                    else {
                        System.out.print(" - ");
                    }
                }
                else {
                    System.out.print(pole.getElement(row, column));
                }
            }
            System.out.println();
        }

        System.out.println();
        pole.drawLine(3,0);
        pole.drawLine(3,4);
        pole.drawLine(3,8);
        for(int row = 0; row < pole.getRowCount(); row++){
            for(int column = 0; column < pole.getColumnCount(); column++){
                if(pole.getElement(row, column) instanceof Dot){
                    System.out.print(" · ");
                }
                else if(pole.getElement(row, column) instanceof Clue){
                    System.out.print(" N ");
                }
                else if(pole.getElement(row, column) instanceof Line){
                    if(((Line) pole.getElement(row, column)).getLineState()==LineState.DRAWN) {
                        System.out.print(" / ");
                    }
                    else if(((Line) pole.getElement(row, column)).getLineState()==LineState.MARKED){
                        System.out.print(" x ");
                    }
                    else{
                        System.out.print(" | ");
                    }
                }
                else {
                    System.out.print(pole.getElement(row, column));
                }
            }
            System.out.println();
        }
        System.out.println(pole.isSolved());
    }
}
