package slitherlink;

import slitherlink.elements.*;

import java.util.Random;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final Element[][] elements;
    private static GameState fieldState;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount*2+1;
        this.columnCount = columnCount*2+1;
        elements = new Element[this.rowCount][this.columnCount];
        generate();
        fieldState = GameState.PLAYING;
    }

    private void generate() {
        elementsGeneration();
        double percentSizeOfLoop = 0.5;
        loopGeneration(percentSizeOfLoop);
        double percentOfClues = 0.3;
        numbersGeneration(percentOfClues);
    }

    private void elementsGeneration(){
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
            }
        }
        for(int row = 0; row < rowCount; row += 2){
            for(int column = 1; column < columnCount; column += 2){
                elements[row][column] = new Line();
                ((Line) elements[row][column]).setBeing(false);
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            }
        }
    }

    private void loopGeneration(double percentSizeOfLoop){
        int rowCountOpt = (rowCount-1)/2;
        int columnCountOpt = (columnCount-1)/2;
        int[][] loop = new int[rowCountOpt][columnCountOpt];
        if(percentSizeOfLoop>=1 || percentSizeOfLoop<=0){throw new RuntimeException("Invalid percent size of loop");}
        int counter = (int) Math.round(rowCountOpt*columnCountOpt*percentSizeOfLoop);
        Random random = new Random();

        loop[random.nextInt(rowCountOpt)][random.nextInt(columnCountOpt)]=1;
        while(counter>0){
            int row = random.nextInt(rowCountOpt);
            int column = random.nextInt(columnCountOpt);
            if(loop[row][column]!=1){
                if((row>0 && loop[row-1][column]==1) || (row<rowCountOpt-1 && loop[row+1][column]==1) ||
                        (column>0 && loop[row][column-1]==1) ||(column<columnCountOpt-1 && loop[row][column+1]==1)){
                    loop[row][column]=1;
                    if(loopIsCorrect(loop, rowCountOpt, columnCountOpt, row, column)){
                        counter--;
                        if(!loopIsPossible(loop, rowCountOpt, columnCountOpt)){
                            break;
                        }
                    }
                    else{
                        loop[row][column]=0;
                    }
                }
            }
        }

        //printLoop(loop, rowCountOpt, columnCountOpt);

        for(int y = 0; y<rowCountOpt; y++) {
            for (int x = 0; x < columnCountOpt; x++) {
                if (loop[y][x] == 1) {
                    if (x > 0) {
                        if (loop[y][x - 1] != loop[y][x]) {
                            ((Line) elements[y * 2 + 1][x * 2]).setBeing(true);
                        }
                    } else {
                        ((Line) elements[y * 2 + 1][x * 2]).setBeing(true);
                    }

                    if (x < columnCountOpt - 1) {
                        if (loop[y][x + 1] != loop[y][x]) {
                            ((Line) elements[y * 2 + 1][x * 2 + 2]).setBeing(true);
                        }
                    } else {
                        ((Line) elements[y * 2 + 1][x * 2 + 2]).setBeing(true);
                    }

                    if (y > 0) {
                        if (loop[y - 1][x] != loop[y][x]) {
                            ((Line) elements[y * 2][x * 2 + 1]).setBeing(true);
                        }
                    } else {
                        ((Line) elements[y * 2][x * 2 + 1]).setBeing(true);
                    }

                    if (y < rowCountOpt - 1) {
                        if (loop[y + 1][x] != loop[y][x]) {
                            ((Line) elements[y * 2 + 2][x * 2 + 1]).setBeing(true);
                        }
                    } else {
                        ((Line) elements[y * 2 + 2][x * 2 + 1]).setBeing(true);
                    }

                }
            }
        }
    }
    private boolean loopIsCorrect(int[][] loop, int rowCountOpt,int columnCountOpt, int row, int column){

        if(row>0 && row<rowCountOpt-1 && loop[row-1][column]==1 && loop[row+1][column]==1){
            return false;
        }
        if(column>0 && column<columnCountOpt-1 && loop[row][column-1]==1 && loop[row][column+1]==1){
            return false;
        }

        for(int y = 0; y < rowCountOpt; y++){
            for(int x = 0; x < columnCountOpt; x++){
                if(y<rowCountOpt-1 && x<columnCountOpt-1){
                    if(loop[y][x]==1 && loop[y+1][x]==1 && loop[y][x+1]==1 && loop[y+1][x+1]==1){
                        return false;
                    }
                    if(loop[y][x]==1 && loop[y+1][x]==0 && loop[y][x+1]==0 && loop[y+1][x+1]==1){
                        return false;
                    }
                    if(loop[y][x]==0 && loop[y+1][x]==1 && loop[y][x+1]==1 && loop[y+1][x+1]==0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean loopIsPossible(int[][] loop, int rowCountOpt,int columnCountOpt){
        for(int y = 0; y < rowCountOpt; y++) {
            for (int x = 0; x < columnCountOpt; x++) {
                if(loop[y][x]==0 && ((y>0 && loop[y-1][x]==1) || (y<rowCountOpt-1 && loop[y+1][x]==1) ||
                        (x>0 && loop[y][x-1]==1) || (x<columnCountOpt-1 && loop[y][x+1]==1))){
                    loop[y][x]=1;
                    if(loopIsCorrect(loop, rowCountOpt, columnCountOpt, y, x)){
                        loop[y][x]=0;
                        return true;
                    }
                    loop[y][x]=0;
                }
            }
        }
        return false;
    }
    /*private void printLoop(int[][] loop, int rowCountOpt,int columnCountOpt){
        for(int y = 0; y<rowCountOpt; y++){
            for(int x = 0; x<columnCountOpt; x++){
                System.out.print(loop[y][x]+" ");
            }
            System.out.println();
        }
    }*/
    private void numbersGeneration( double percentOfClues) {
        for(int y = 1; y<rowCount; y+=2) {
            for (int x = 1; x < columnCount; x+=2) {
                int value = 0;
                if(((Line) elements[y - 1][x]).isBeing()){
                    value++;
                }
                if(((Line) elements[y + 1][x]).isBeing()){
                    value++;
                }
                if(((Line) elements[y][x - 1]).isBeing()){
                    value++;
                }
                if(((Line) elements[y][x + 1]).isBeing()){
                    value++;
                }
                ((Clue) elements[y][x]).setValue(value);
            }
        }
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
        for(int y = 1; y < rowCount; y += 2){
            for(int x = 0; x < columnCount; x += 2){
                if(((Line) elements[y][x]).getLineState() != LineState.DRAWN && ((Line) elements[y][x]).isBeing()){
                    return false;
                }
                if(((Line) elements[x][y]).getLineState() != LineState.DRAWN && ((Line) elements[x][y]).isBeing()){
                    return false;
                }
            }
        }
        fieldState = GameState.SOLVED;
        return true;
    }

    public int getRowCount() {
        //-> this.rowCount = rowCount*2+1;
        return rowCount;
    }

    public int getColumnCount() {
        //-> this.columnCount = columnCount*2+1;
        return columnCount;
    }

    public Element getElement(int row, int column) {
        return elements[row][column];
    }
}
