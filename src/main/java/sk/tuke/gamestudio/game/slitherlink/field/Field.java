package sk.tuke.gamestudio.game.slitherlink.field;
import sk.tuke.gamestudio.game.slitherlink.elements.*;

import java.util.Random;


public class Field {
    private final int rowCount;
    private final int columnCount;
    private final double percentSizeOfLoop;
    private final double percentWithoutClues;
    private final Element[][] elements;
    private GameState fieldState;
    private final GameSolver gameSolver;
    private final long startMillis;

    public Field(int rowCount, int columnCount, double percentSizeOfLoop, double percentWithoutClues) {
        if (rowCount > 9 || columnCount > 9 || columnCount < 1 || rowCount < 1) {
            throw new RuntimeException("Invalid size of field");
        }
        if (percentSizeOfLoop > 1 || percentSizeOfLoop < 0) {
            throw new RuntimeException("Invalid percent size of loop");
        }
        if (percentWithoutClues > 1 || percentWithoutClues < 0) {
            throw new RuntimeException("Invalid percent of clues");
        }
        this.rowCount = rowCount * 2 + 1;
        this.columnCount = columnCount * 2 + 1;
        this.percentSizeOfLoop = percentSizeOfLoop;
        this.percentWithoutClues = percentWithoutClues;
        elements = new Element[this.rowCount][this.columnCount];
        fieldState = GameState.PLAYING;
        gameSolver = new GameSolver(this.rowCount, this.columnCount, this.percentSizeOfLoop);
        generate();
        startMillis = System.currentTimeMillis();
    }

    private void generate() {
        elementsGeneration();
        loopGeneration();
        numbersGeneration();
    }

    private void elementsGeneration() {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                elements[row][column] = new Dot();
            }
        }
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                elements[row][column] = new Clue();
                ((Clue) elements[row][column]).setClueState(ClueState.VISIBLE);
            }
        }
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                elements[row][column] = new Line();
                ((Line) elements[row][column]).setBeing(false);
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            }
        }
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                elements[row][column] = new Line();
                ((Line) elements[row][column]).setBeing(false);
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            }
        }
    }

    private void loopGeneration() {
        int rowCountOpt = (rowCount - 1) / 2;
        int columnCountOpt = (columnCount - 1) / 2;
        int[][] loop = new int[rowCountOpt][columnCountOpt];

        int counter = (int) Math.round(rowCountOpt * columnCountOpt * percentSizeOfLoop);

        if ((rowCount - 1) / 2 == 2 && (columnCount - 1) / 2 == 2 && counter >=3 ) {
            //impossible case
            counter=2;
        }
        Random random = new Random();

        loop[random.nextInt(rowCountOpt)][random.nextInt(columnCountOpt)] = 1;
        counter--;
        while (counter > 0) {
            int row = random.nextInt(rowCountOpt);
            int column = random.nextInt(columnCountOpt);
            if (loop[row][column] != 1) {
                if ((row > 0 && loop[row - 1][column] == 1) || (row < rowCountOpt - 1 && loop[row + 1][column] == 1) ||
                        (column > 0 && loop[row][column - 1] == 1) || (column < columnCountOpt - 1 && loop[row][column + 1] == 1)) {
                    loop[row][column] = 1;
                    if (!loopIsCorrect(loop, rowCountOpt, columnCountOpt, row, column)) {
                        loop[row][column] = 0;
                        if (!loopIsPossible(loop, rowCountOpt, columnCountOpt)) {
                            break;
                        }
                    } else {
                        counter--;

                    }
                }
            }
        }

        for (int y = 0; y < rowCountOpt; y++) {
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

    private boolean loopIsCorrect(int[][] loop, int rowCountOpt, int columnCountOpt, int row, int column) {

        if (row > 0 && row < rowCountOpt - 1 && loop[row - 1][column] == 1 && loop[row + 1][column] == 1) {
            return false;
        }
        if (column > 0 && column < columnCountOpt - 1 && loop[row][column - 1] == 1 && loop[row][column + 1] == 1) {
            return false;
        }

        for (int y = 0; y < rowCountOpt; y++) {
            for (int x = 0; x < columnCountOpt; x++) {
                if (y < rowCountOpt - 1 && x < columnCountOpt - 1) {
                    if (loop[y][x] == 1 && loop[y + 1][x] == 1 && loop[y][x + 1] == 1 && loop[y + 1][x + 1] == 1) {
                        return false;
                    }
                    if (loop[y][x] == 1 && loop[y + 1][x] == 0 && loop[y][x + 1] == 0 && loop[y + 1][x + 1] == 1) {
                        return false;
                    }
                    if (loop[y][x] == 0 && loop[y + 1][x] == 1 && loop[y][x + 1] == 1 && loop[y + 1][x + 1] == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean loopIsPossible(int[][] loop, int rowCountOpt, int columnCountOpt) {
        for (int y = 0; y < rowCountOpt; y++) {
            for (int x = 0; x < columnCountOpt; x++) {
                if (loop[y][x] == 0 && ((y > 0 && loop[y - 1][x] == 1) || (y < rowCountOpt - 1 && loop[y + 1][x] == 1) ||
                        (x > 0 && loop[y][x - 1] == 1) || (x < columnCountOpt - 1 && loop[y][x + 1] == 1))) {
                    loop[y][x] = 1;
                    if (loopIsCorrect(loop, rowCountOpt, columnCountOpt, y, x)) {
                        loop[y][x] = 0;
                        return true;
                    }
                    loop[y][x] = 0;
                }
            }
        }
        return false;
    }

    private void numbersGeneration() {
        for (int y = 1; y < rowCount; y += 2) {
            for (int x = 1; x < columnCount; x += 2) {
                int value = 0;
                if (((Line) elements[y - 1][x]).isBeing()) {
                    value++;
                }
                if (((Line) elements[y + 1][x]).isBeing()) {
                    value++;
                }
                if (((Line) elements[y][x - 1]).isBeing()) {
                    value++;
                }
                if (((Line) elements[y][x + 1]).isBeing()) {
                    value++;
                }
                ((Clue) elements[y][x]).setValue(value);
            }
        }
        int rowCountOpt = (rowCount - 1) / 2;
        int columnCountOpt = (columnCount - 1) / 2;

        Random random = new Random();
        int counter = (int) Math.round(rowCountOpt * columnCountOpt * percentWithoutClues);

        while (counter > 0) {
            int row = random.nextInt(rowCountOpt) * 2 + 1;
            int column = random.nextInt(columnCountOpt) * 2 + 1;
            if (((Clue) elements[row][column]).getClueState() == ClueState.VISIBLE) {
                ((Clue) elements[row][column]).setClueState(ClueState.HIDDEN);
                if (!gameSolver.isGameCanBeSolved(elements)) {
                    ((Clue) elements[row][column]).setClueState(ClueState.VISIBLE);
                    if (!gameSolver.isGamePossibleToChange(elements)) {
                        break;
                    }
                } else {
                    counter--;
                }
            }
        }
    }

    public void drawLine(int row, int column) {
        if (elements[row][column] instanceof Line && fieldState == GameState.PLAYING) {
            if (((Line) elements[row][column]).getLineState() == LineState.DRAWN) {
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            } else {
                ((Line) elements[row][column]).setLineState(LineState.DRAWN);
            }
            if(gameSolver.isSolved(elements)) {
                fieldState = GameState.SOLVED;
            }
        }
    }

    public void markLine(int row, int column) {
        if (elements[row][column] instanceof Line && fieldState == GameState.PLAYING) {
            if (((Line) elements[row][column]).getLineState() == LineState.MARKED) {
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            } else {
                ((Line) elements[row][column]).setLineState(LineState.MARKED);
            }
        }
    }

    public int getRowCount() {
        //-> this.rowCount = rowCount*2+1;
        return rowCount;
    }

    public int getColumnCount() {
        //-> this.columnCount = columnCount*2+1;
        return columnCount;
    }

    public Element[][] getElements() {
        return elements;
    }

    public Element getElement(int row, int column) {
        return elements[row][column];
    }

    public GameState getFieldState() {
        return fieldState;
    }

    public int getScore() {
        int score = (int) (rowCount * columnCount - (System.currentTimeMillis() - startMillis) / 2000);
        if(fieldState == GameState.SOLVED && score > 0){
            return score;
        } else {
            return 0;
        }
    }

}
