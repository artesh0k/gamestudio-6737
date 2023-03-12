package sk.tuke.gamestudio.game.slitherlink.field;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.slitherlink.elements.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private final Field field;
    private int rowCount;
    private int columnCount;

    public FieldTest(){
        Random randomGenerator = new Random();
        rowCount = randomGenerator.nextInt(9) + 1;
        columnCount = randomGenerator.nextInt(9) + 1;
        double percentSizeOfLoop = randomGenerator.nextDouble();
        double percentWithoutClues = randomGenerator.nextDouble();
        field = new Field(rowCount, columnCount, percentSizeOfLoop, percentWithoutClues);
        rowCount = field.getRowCount();
        columnCount = field.getColumnCount();
    }

    @Test
    public void fieldIsNotEmptyTest(){
        for(int row = 0; row < rowCount; row++){
            for(int column = 0; column < columnCount; column++){
                assertNotNull(field.getElement(row, column));
            }
        }
    }

    @Test
    public void fieldHasCorrectObjectsTest(){
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                assertInstanceOf(Dot.class, field.getElement(row, column));
            }
        }
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                assertInstanceOf(Clue.class, field.getElement(row, column));
            }
        }
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                assertInstanceOf(Line.class, field.getElement(row, column));
            }
        }
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                assertInstanceOf(Line.class, field.getElement(row, column));
            }
        }
    }

    @Test
    public void drawLineTest(){
        field.drawLine(0,1);
        assertEquals(((Line)field.getElement(0, 1)).getLineState(), LineState.DRAWN);
        field.drawLine(0,1);
        assertEquals(((Line)field.getElement(0, 1)).getLineState(), LineState.EMPTY);
    }

    @Test
    public void markLineTest(){
        field.markLine(0,1);
        assertEquals(((Line)field.getElement(0, 1)).getLineState(), LineState.MARKED);
        field.markLine(0,1);
        assertEquals(((Line)field.getElement(0, 1)).getLineState(), LineState.EMPTY);
    }

    @Test
    public void badValuesForFieldTest(){

        assertThrows(RuntimeException.class, () -> new Field(10,9,0.2, 0.3));
        assertThrows(RuntimeException.class, () -> new Field(0,9,0.2, 0.3));
        assertThrows(RuntimeException.class, () -> new Field(9,10,0.2, 0.3));
        assertThrows(RuntimeException.class, () -> new Field(9,0,0.2, 0.3));

        assertThrows(RuntimeException.class, () -> new Field(9,9,1.1, 0.3));
        assertThrows(RuntimeException.class, () -> new Field(9,9,-0.1, 0.3));
        assertThrows(RuntimeException.class, () -> new Field(9,9,0.2, 1.1));
        assertThrows(RuntimeException.class, () -> new Field(9,9,0.2, -0.1));
    }

   @Test
    public void generatedLoopAndCluesAreCorrectTest() {
        for (int y = 1; y < rowCount; y += 2) {
            for (int x = 1; x < columnCount; x += 2) {
                int value = 0;
                if (((Line) field.getElement(y - 1, x)).isBeing()) {
                    value++;
                }
                if (((Line) field.getElement(y + 1, x)).isBeing()) {
                    value++;
                }
                if (((Line) field.getElement(y, x - 1)).isBeing()) {
                    value++;
                }
                if (((Line) field.getElement(y, x + 1)).isBeing()) {
                    value++;
                }
                assertEquals(((Clue) field.getElement(y, x)).getValue(), value, "Count of lines does not equal " +
                        "to clue");
            }
        }
    }

    @Test
    public void generatedLoopIsCycle() {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                if (field.getElement(row, column) instanceof Dot) {
                    int countOfLines = 0;
                    if (row != 0 && ((Line) field.getElement(row-1, column)).isBeing()) {
                        countOfLines++;
                    }
                    if (column != 0 && ((Line) field.getElement(row, column-1)).isBeing()) {
                        countOfLines++;
                    }
                    if (row != rowCount - 1 && ((Line) field.getElement(row+1, column)).isBeing()) {
                        countOfLines++;
                    }
                    if (column != columnCount - 1 && ((Line) field.getElement(row, column+1)).isBeing()) {
                        countOfLines++;
                    }

                    if(countOfLines!=0) {
                        assertEquals(countOfLines, 2);
                    }
                }
            }
        }
    }

    @Test
    public void impossibleCase() {
        Field field1 = new Field(2,2,0.6, 1);
        int value = 0;
        for (int y = 1; y < 5; y += 2) {
            for (int x = 1; x < 5; x += 2) {

                if (((Line) field1.getElement(y - 1, x)).isBeing()) {
                    value++;
                }
                if (((Line) field1.getElement(y + 1, x)).isBeing()) {
                    value++;
                }
                if (((Line) field1.getElement(y, x - 1)).isBeing()) {
                    value++;
                }
                if (((Line) field1.getElement(y, x + 1)).isBeing()) {
                    value++;
                }
            }
        }
        assertNotEquals(value, 6);
    }
    @Test
    public void isSolvedTest() {
        int row=2;
        int column=2;
        Field field1 = new Field(row, column, 1, 1);
        cleanField(field1, row, column);
        ((Clue) field1.getElement(1,1)).setValue(3);
        ((Clue) field1.getElement(1,3)).setValue(1);
        ((Clue) field1.getElement(3,1)).setValue(3);
        ((Clue) field1.getElement(3,3)).setValue(1);

        ((Line) field1.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,2)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,2)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(4,1)).setLineState(LineState.DRAWN);

        assertTrue(field1.isSolved(field1.getElements()));

        cleanField(field1, row, column);
        ((Clue) field1.getElement(1,1)).setValue(3);
        ((Clue) field1.getElement(1,3)).setValue(1);
        ((Clue) field1.getElement(3,1)).setValue(3);
        ((Clue) field1.getElement(3,3)).setValue(1);

        ((Line) field1.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,2)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,2)).setLineState(LineState.DRAWN);

        assertFalse(field1.isSolved(field1.getElements()));

        cleanField(field1, row, column);
        ((Clue) field1.getElement(1,1)).setValue(3);
        ((Clue) field1.getElement(1,3)).setValue(1);
        ((Clue) field1.getElement(3,1)).setValue(3);
        ((Clue) field1.getElement(3,3)).setValue(1);

        ((Line) field1.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(1,4)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(2,1)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,4)).setLineState(LineState.DRAWN);
        ((Line) field1.getElement(3,4)).setLineState(LineState.DRAWN);

        assertFalse(field1.isSolved(field1.getElements()));
    }

    @Test
    public void gameCanBeSolvedTest() {
        int row=2;
        int column=2;
        Field field1 = new Field(row, column, 0.6, 1);// 0.6 * 4 ~ 2
        cleanField(field1, row, column);
        ((Clue) field1.getElement(1,1)).setValue(3);
        ((Clue) field1.getElement(1,3)).setValue(1);
        ((Clue) field1.getElement(1,3)).setClueState(ClueState.HIDDEN);
        ((Clue) field1.getElement(3,1)).setValue(3);
        ((Clue) field1.getElement(3,3)).setValue(1);
        ((Clue) field1.getElement(3,3)).setClueState(ClueState.HIDDEN);

        assertTrue(field1.gameCanBeSolved());

        cleanField(field1, row, column);
        ((Clue) field1.getElement(1,1)).setValue(3);
        ((Clue) field1.getElement(1,3)).setValue(1);
        ((Clue) field1.getElement(1,3)).setClueState(ClueState.HIDDEN);
        ((Clue) field1.getElement(3,1)).setValue(3);
        ((Clue) field1.getElement(3,1)).setClueState(ClueState.HIDDEN);
        ((Clue) field1.getElement(3,3)).setValue(1);
        ((Clue) field1.getElement(3,3)).setClueState(ClueState.HIDDEN);

        assertFalse(field1.gameCanBeSolved());

    }

    private void cleanField(Field field, int rowCount, int columnCount){
        rowCount=rowCount*2+1;
        columnCount=columnCount*2+1;
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                ((Clue) field.getElement(row, column)).setValue(0);
                ((Clue) field.getElement(row, column)).setClueState(ClueState.VISIBLE);
            }
        }
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                ((Line) field.getElement(row, column)).setLineState(LineState.EMPTY);
                ((Line) field.getElement(row, column)).setBeing(false);
            }
        }
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                ((Line) field.getElement(row, column)).setLineState(LineState.EMPTY);
                ((Line) field.getElement(row, column)).setBeing(false);
            }
        }
    }

}