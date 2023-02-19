package Slitherlink;

import Slitherlink.Elements.*;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final Element[][] elements;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount*2+1;
        this.columnCount = columnCount*2+1;
        elements = new Element[this.rowCount][this.columnCount];
        System.out.println(this.rowCount);
        System.out.println(this.columnCount);
        generate();
    }

    private void generate() {
        simpleGeneration();
        // generation of a loop...
        loopGeneration();
    }
    private void simpleGeneration(){
        for(int row = 0; row < rowCount; row += 2){
            for(int column = 0; column < columnCount; column += 2){
                elements[row][column] = new Dot();
            }
        }
        for(int row = 1; row < rowCount; row += 2){
            for(int column = 1; column < columnCount; column += 2){
                elements[row][column] = new Clue();
            }
        }
        for(int row = 1; row < rowCount; row += 2){
            for(int column = 0; column < columnCount; column += 2){
                elements[row][column] = new Line();
                ((Line) elements[row][column]).setBeing(false);
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
                elements[column][row] = new Line();
                ((Line) elements[column][row]).setBeing(false);
                ((Line) elements[column][row]).setLineState(LineState.EMPTY);
            }
        }
    }

    private void loopGeneration(){
        ((Line) elements[3][0]).setBeing(true);
        ((Line) elements[3][4]).setBeing(true);
        ((Line) elements[3][8]).setBeing(true);
    }

    public void drawLine(int row, int column){
        if(elements[row][column] instanceof Line){
            if(((Line) elements[row][column]).getLineState() == LineState.DRAWN){
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            }
            else{
                ((Line) elements[row][column]).setLineState(LineState.DRAWN);
            }
        }
    }

    public void markLine(int row, int column){
        if(elements[row][column] instanceof Line){
            if(((Line) elements[row][column]).getLineState() == LineState.MARKED){
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            }
            else{
                ((Line) elements[row][column]).setLineState(LineState.MARKED);
            }
        }
    }

    public boolean isSolved(){
        for(int row = 1; row < rowCount; row += 2){
            for(int column = 0; column < columnCount; column += 2){
                if(((Line) elements[row][column]).getLineState() != LineState.DRAWN
                        && ((Line) elements[row][column]).isBeing()){
                    return false;
                }
                if(((Line) elements[column][row]).getLineState() != LineState.DRAWN
                        && ((Line) elements[column][row]).isBeing()){
                    return false;
                }
            }
        }
        return true;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Element getElement(int row, int column) {
        return elements[row][column];
    }
}
