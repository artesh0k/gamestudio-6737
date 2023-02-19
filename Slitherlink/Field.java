package Slitherlink;

import Slitherlink.Elements.Clue;
import Slitherlink.Elements.Dot;
import Slitherlink.Elements.Element;
import Slitherlink.Elements.Line;

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
            }
        }
        for(int row = 0; row < rowCount; row += 2){
            for(int column = 1; column < columnCount; column += 2){
                elements[row][column] = new Line();
            }
        }
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
