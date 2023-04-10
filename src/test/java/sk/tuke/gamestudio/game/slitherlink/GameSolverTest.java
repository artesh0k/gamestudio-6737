package sk.tuke.gamestudio.game.slitherlink;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.slitherlink.elements.Clue;
import sk.tuke.gamestudio.game.slitherlink.elements.ClueState;
import sk.tuke.gamestudio.game.slitherlink.elements.Line;
import sk.tuke.gamestudio.game.slitherlink.elements.LineState;
import sk.tuke.gamestudio.game.slitherlink.field.Field;
import sk.tuke.gamestudio.game.slitherlink.field.GameSolver;


import static org.junit.jupiter.api.Assertions.*;

class GameSolverTest {

    @Test
    public void isSolvedTest() {
        int row=2;
        int column=2;
        Field field = new Field(row, column, 1, 1);
        GameSolver gameSolver = new GameSolver(field.getRowCount(), field.getColumnCount(), 1);
        cleanField(field, row, column);
        ((Clue) field.getElement(1,1)).setValue(3);
        ((Clue) field.getElement(1,3)).setValue(1);
        ((Clue) field.getElement(3,1)).setValue(3);
        ((Clue) field.getElement(3,3)).setValue(1);

        ((Line) field.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,2)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,2)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(4,1)).setLineState(LineState.DRAWN);

        assertTrue(gameSolver.isSolved(field.getElements()));

        cleanField(field, row, column);
        ((Clue) field.getElement(1,1)).setValue(3);
        ((Clue) field.getElement(1,3)).setValue(1);
        ((Clue) field.getElement(3,1)).setValue(3);
        ((Clue) field.getElement(3,3)).setValue(1);

        ((Line) field.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,2)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,2)).setLineState(LineState.DRAWN);

        assertFalse(gameSolver.isSolved(field.getElements()));

        cleanField(field, row, column);
        ((Clue) field.getElement(1,1)).setValue(3);
        ((Clue) field.getElement(1,3)).setValue(1);
        ((Clue) field.getElement(3,1)).setValue(3);
        ((Clue) field.getElement(3,3)).setValue(1);

        ((Line) field.getElement(0,1)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(1,4)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(2,1)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,0)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,4)).setLineState(LineState.DRAWN);
        ((Line) field.getElement(3,4)).setLineState(LineState.DRAWN);

        assertFalse(gameSolver.isSolved(field.getElements()));
    }

    @Test
    public void gameCanBeSolvedTest() {
        int row=2;
        int column=2;
        Field field = new Field(row, column, 0.6, 1);// 0.6 * 4 ~ 2
        GameSolver gameSolver = new GameSolver(field.getRowCount(), field.getColumnCount(), 0.6);
        cleanField(field, row, column);
        ((Clue) field.getElement(1,1)).setValue(3);
        ((Clue) field.getElement(1,3)).setValue(1);
        ((Clue) field.getElement(1,3)).setClueState(ClueState.HIDDEN);
        ((Clue) field.getElement(3,1)).setValue(3);
        ((Clue) field.getElement(3,3)).setValue(1);
        ((Clue) field.getElement(3,3)).setClueState(ClueState.HIDDEN);

        assertTrue(gameSolver.isGameCanBeSolved(field.getElements()));

        cleanField(field, row, column);
        ((Clue) field.getElement(1,1)).setValue(3);
        ((Clue) field.getElement(1,3)).setValue(1);
        ((Clue) field.getElement(1,3)).setClueState(ClueState.HIDDEN);
        ((Clue) field.getElement(3,1)).setValue(3);
        ((Clue) field.getElement(3,1)).setClueState(ClueState.HIDDEN);
        ((Clue) field.getElement(3,3)).setValue(1);
        ((Clue) field.getElement(3,3)).setClueState(ClueState.HIDDEN);

        assertFalse(gameSolver.isGameCanBeSolved(field.getElements()));

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