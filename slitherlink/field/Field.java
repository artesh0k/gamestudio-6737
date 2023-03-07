package slitherlink.field;
import slitherlink.elements.*;
import java.util.Random;


public class Field {
    private final int rowCount;
    private final int columnCount;
    private final double percentSizeOfLoop;
    private final double percentWithoutClues;
    private final Element[][] elements;
    private static GameState fieldState;

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
        generate();
        fieldState = GameState.PLAYING;
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
        if ((rowCount - 1) / 2 == 2 && (columnCount - 1) / 2 == 2 && counter == 3) {
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

        //printLoop(loop, rowCountOpt, columnCountOpt);

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
        //System.out.println(counter);
        gameCanBeSolved();
        while (counter > 0) {
            int row = random.nextInt(rowCountOpt) * 2 + 1;
            int column = random.nextInt(columnCountOpt) * 2 + 1;
            if (((Clue) elements[row][column]).getClueState() == ClueState.VISIBLE) {
                ((Clue) elements[row][column]).setClueState(ClueState.HIDDEN);
                if (!gameCanBeSolved()) {
                    ((Clue) elements[row][column]).setClueState(ClueState.VISIBLE);
                    if (!gameIsPossible(elements)) {
                        break;
                    }
                } else {
                    counter--;
                }
            }
        }
    }

    boolean gameCanBeSolved() {
        Element[][] check = copyElements(elements);
        Element[][] lastCheck = copyElements(check);

        noLinesAroundA0(check);
        adjacent0And3(check);
        diagonal0And3(check);
        twoAdjacent3s(check);
        twoDiagonals3s(check);
        anyNumberInACorner(check);
        //printGameSolver(check);

        while (!isTheSameField(check, lastCheck)) {
            lastCheck = copyElements(check);
            twoLinesAroundTheDot(check);
            crossesAroundTheDot(check);
            crossesShowLines(check);
            linesShowCrosses(check);
            noOtherWay(check);
            //printGameSolver(check);
            if (isSolved(check)) {
                //System.out.println("victory");
                return true;
            }
        }
        return false;
    }

    private boolean gameIsPossible(Element[][] check) {
        for (int y = 1; y < rowCount - 1; y += 2) {
            for (int x = 1; x < columnCount - 1; x += 2) {
                if (((Clue) check[y][x]).getClueState() == ClueState.VISIBLE) {
                    ((Clue) check[y][x]).setClueState(ClueState.HIDDEN);
                    if (gameCanBeSolved()) {
                        ((Clue) check[y][x]).setClueState(ClueState.VISIBLE);
                        return true;
                    }
                    ((Clue) check[y][x]).setClueState(ClueState.VISIBLE);
                }
            }
        }
        return false;
    }

    private void noLinesAroundA0(Element[][] check) {
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                if (((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {
                    if (((Clue) check[row][column]).getValue() == 0) {
                        ((Line) check[row - 1][column]).setLineState(LineState.MARKED);
                        ((Line) check[row + 1][column]).setLineState(LineState.MARKED);
                        ((Line) check[row][column - 1]).setLineState(LineState.MARKED);
                        ((Line) check[row][column + 1]).setLineState(LineState.MARKED);
                        if (!((row == 1 && column == 1) || (row == 1 && column == columnCount - 2) || (row == rowCount - 2 && column == 1) || (column == columnCount - 2 && row == rowCount - 2))) {
                            if (row == 1) {
                                ((Line) check[0][column + 2]).setLineState(LineState.MARKED);
                                ((Line) check[0][column - 2]).setLineState(LineState.MARKED);
                            } else if (row == rowCount - 2) {
                                ((Line) check[rowCount - 1][column + 2]).setLineState(LineState.MARKED);
                                ((Line) check[rowCount - 1][column - 2]).setLineState(LineState.MARKED);
                            } else if (column == 1) {
                                ((Line) check[row + 2][0]).setLineState(LineState.MARKED);
                                ((Line) check[row - 2][0]).setLineState(LineState.MARKED);
                            } else if (column == columnCount - 2) {
                                ((Line) check[row + 2][columnCount - 1]).setLineState(LineState.MARKED);
                                ((Line) check[row - 2][columnCount - 1]).setLineState(LineState.MARKED);
                            }
                        }
                    }
                }
            }
        }

    }

    private void adjacent0And3(Element[][] check) {
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                if (((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {
                    if (((Clue) check[row][column]).getValue() == 3) {
                        if (row != 1 && ((Clue) check[row - 2][column]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row - 2][column]).getValue() == 0) {
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column - 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column + 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column - 2]).setLineState(LineState.MARKED);
                                ((Line) check[row + 1][column + 2]).setLineState(LineState.MARKED);
                                if (row != rowCount - 2) {
                                    ((Line) check[row + 2][column - 1]).setLineState(LineState.MARKED);
                                    ((Line) check[row + 2][column + 1]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                        if (row != rowCount - 2 && ((Clue) check[row + 2][column]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row + 2][column]).getValue() == 0) {
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column - 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column + 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column - 2]).setLineState(LineState.MARKED);
                                ((Line) check[row - 1][column + 2]).setLineState(LineState.MARKED);
                                if (row != 1) {
                                    ((Line) check[row - 2][column - 1]).setLineState(LineState.MARKED);
                                    ((Line) check[row - 2][column + 1]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                        if (column != 1 && ((Clue) check[row][column - 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row][column - 2]).getValue() == 0) {
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 2][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column + 1]).setLineState(LineState.MARKED);
                                ((Line) check[row + 2][column + 1]).setLineState(LineState.MARKED);
                                if (column != columnCount - 2) {
                                    ((Line) check[row - 1][column + 2]).setLineState(LineState.MARKED);
                                    ((Line) check[row + 1][column + 2]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                        if (column != columnCount - 2 && ((Clue) check[row][column + 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row][column + 2]).getValue() == 0) {
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 2][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column - 1]).setLineState(LineState.MARKED);
                                ((Line) check[row + 2][column - 1]).setLineState(LineState.MARKED);
                                if (column != 1) {
                                    ((Line) check[row - 1][column - 2]).setLineState(LineState.MARKED);
                                    ((Line) check[row + 1][column - 2]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void diagonal0And3(Element[][] check) {
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                if (((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {
                    if (((Clue) check[row][column]).getValue() == 3) {
                        if (row != 1 && column != 1 && ((Clue) check[row - 2][column - 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row - 2][column - 2]).getValue() == 0) {
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                            }
                        }
                        if (row != 1 && column != columnCount - 2 && ((Clue) check[row - 2][column + 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row - 2][column + 2]).getValue() == 0) {
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                            }
                        }
                        if (row != rowCount - 2 && column != 1 && ((Clue) check[row + 2][column - 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row + 2][column - 2]).getValue() == 0) {
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                            }
                        }
                        if (row != rowCount - 2 && column != columnCount - 2 && ((Clue) check[row + 2][column + 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row + 2][column + 2]).getValue() == 0) {
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                            }
                        }

                    }
                }
            }
        }
    }

    private void twoAdjacent3s(Element[][] check) {
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                if (((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {
                    if (((Clue) check[row][column]).getValue() == 3) {
                        if (row != rowCount - 2 && ((Clue) check[row + 2][column]).getClueState() == ClueState.VISIBLE && rowCount!=5) {
                            if (((Clue) check[row + 2][column]).getValue() == 3) {
                                ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 3][column]).setLineState(LineState.DRAWN);
                                if (column != 1) {
                                    ((Line) check[row + 1][column - 2]).setLineState(LineState.MARKED);
                                }
                                if (column != columnCount - 2) {
                                    ((Line) check[row + 1][column + 2]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                        if (column != columnCount - 2 && ((Clue) check[row][column + 2]).getClueState() == ClueState.VISIBLE && columnCount!=5) {
                            if (((Clue) check[row][column + 2]).getValue() == 3) {
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column + 3]).setLineState(LineState.DRAWN);
                                if (row != 1) {
                                    ((Line) check[row - 2][column + 1]).setLineState(LineState.MARKED);
                                }
                                if (row != rowCount - 2) {
                                    ((Line) check[row + 2][column + 1]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private void twoDiagonals3s(Element[][] check) {
        for (int row = 1; row < rowCount; row += 2) {
            for (int column = 1; column < columnCount; column += 2) {
                if (((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {
                    if (((Clue) check[row][column]).getValue() == 3) {
                        if (row != 1 && column != columnCount - 2 && ((Clue) check[row - 2][column + 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row - 2][column + 2]).getValue() == 3) {
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                                if (column != 1) {
                                    ((Line) check[row + 1][column - 2]).setLineState(LineState.MARKED);
                                }
                                if (row != rowCount - 2) {
                                    ((Line) check[row + 2][column - 1]).setLineState(LineState.MARKED);
                                }
                                ((Line) check[row - 3][column + 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column + 3]).setLineState(LineState.DRAWN);
                                if (column + 2 != columnCount - 2) {
                                    ((Line) check[row - 3][column + 4]).setLineState(LineState.MARKED);
                                }
                                if (row - 2 != 1) {
                                    ((Line) check[row - 4][column + 3]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                        if (row != 1 && column != 1 && ((Clue) check[row - 2][column - 2]).getClueState() == ClueState.VISIBLE) {
                            if (((Clue) check[row - 2][column - 2]).getValue() == 3) {
                                ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                                ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                                if (column != columnCount - 2) {
                                    ((Line) check[row + 1][column + 2]).setLineState(LineState.MARKED);
                                }
                                if (row != rowCount - 2) {
                                    ((Line) check[row + 2][column + 1]).setLineState(LineState.MARKED);
                                }
                                ((Line) check[row - 3][column - 2]).setLineState(LineState.DRAWN);
                                ((Line) check[row - 2][column - 3]).setLineState(LineState.DRAWN);
                                if (column - 2 != 1) {
                                    ((Line) check[row - 3][column - 4]).setLineState(LineState.MARKED);
                                }
                                if (row - 2 != 1) {
                                    ((Line) check[row - 4][column - 3]).setLineState(LineState.MARKED);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void anyNumberInACorner(Element[][] check) {
        if (((Clue) check[1][1]).getClueState() == ClueState.VISIBLE) {
            switch (((Clue) check[1][1]).getValue()) {
                case 0 -> {
                    if (columnCount - 1 > 3) {
                        ((Line) check[0][3]).setLineState(LineState.MARKED);
                    }
                    if (rowCount - 1 > 3) {
                        ((Line) check[3][0]).setLineState(LineState.MARKED);
                    }
                }
                case 1 -> {
                    ((Line) check[0][1]).setLineState(LineState.MARKED);
                    ((Line) check[1][0]).setLineState(LineState.MARKED);
                }
                case 2 -> {
                    if (columnCount - 1 > 3) {
                        ((Line) check[0][3]).setLineState(LineState.DRAWN);
                    }
                    if (rowCount - 1 > 3) {
                        ((Line) check[3][0]).setLineState(LineState.DRAWN);
                    }
                }
                case 3 -> {
                    ((Line) check[0][1]).setLineState(LineState.DRAWN);
                    ((Line) check[1][0]).setLineState(LineState.DRAWN);
                }
            }
        }
        if (((Clue) check[1][columnCount - 2]).getClueState() == ClueState.VISIBLE) {

            switch (((Clue) check[1][columnCount - 2]).getValue()) {
                case 0 -> {
                    if (columnCount - 4 > 0) {
                        ((Line) check[0][columnCount - 4]).setLineState(LineState.MARKED);
                    }
                    if (rowCount > 3) {
                        ((Line) check[3][columnCount - 1]).setLineState(LineState.MARKED);
                    }
                }
                case 1 -> {
                    ((Line) check[0][columnCount - 2]).setLineState(LineState.MARKED);
                    ((Line) check[1][columnCount - 1]).setLineState(LineState.MARKED);
                }
                case 2 -> {
                    if (columnCount - 4 > 0) {
                        ((Line) check[0][columnCount - 4]).setLineState(LineState.DRAWN);
                    }
                    if (rowCount > 3) {
                        ((Line) check[3][columnCount - 1]).setLineState(LineState.DRAWN);
                    }
                }
                case 3 -> {
                    ((Line) check[0][columnCount - 2]).setLineState(LineState.DRAWN);
                    ((Line) check[1][columnCount - 1]).setLineState(LineState.DRAWN);
                }
            }
        }
        if (((Clue) check[rowCount - 2][1]).getClueState() == ClueState.VISIBLE) {
            switch (((Clue) check[rowCount - 2][1]).getValue()) {
                case 0 -> {
                    if (rowCount - 4 > 0) {
                        ((Line) check[rowCount - 4][0]).setLineState(LineState.MARKED);
                    }
                    if (columnCount > 3) {
                        ((Line) check[rowCount - 1][3]).setLineState(LineState.MARKED);
                    }
                }
                case 1 -> {
                    ((Line) check[rowCount - 2][0]).setLineState(LineState.MARKED);
                    ((Line) check[rowCount - 1][1]).setLineState(LineState.MARKED);
                }
                case 2 -> {
                    if (rowCount - 4 > 0) {
                        ((Line) check[rowCount - 4][0]).setLineState(LineState.DRAWN);
                    }
                    if (columnCount > 3) {
                        ((Line) check[rowCount - 1][3]).setLineState(LineState.DRAWN);
                    }
                }
                case 3 -> {
                    ((Line) check[rowCount - 2][0]).setLineState(LineState.DRAWN);
                    ((Line) check[rowCount - 1][1]).setLineState(LineState.DRAWN);
                }
            }
        }
        if (((Clue) check[rowCount - 2][columnCount - 2]).getClueState() == ClueState.VISIBLE) {
            switch (((Clue) check[rowCount - 2][columnCount - 2]).getValue()) {
                case 0 -> {
                    if (columnCount - 4 > 0) {
                        ((Line) check[rowCount - 1][columnCount - 4]).setLineState(LineState.MARKED);
                    }
                    if (rowCount - 4 > 0) {
                        ((Line) check[rowCount - 4][columnCount - 1]).setLineState(LineState.MARKED);
                    }
                }
                case 1 -> {
                    ((Line) check[rowCount - 2][columnCount - 1]).setLineState(LineState.MARKED);
                    ((Line) check[rowCount - 1][columnCount - 2]).setLineState(LineState.MARKED);
                }
                case 2 -> {
                    if (columnCount - 4 > 0) {
                        ((Line) check[rowCount - 1][columnCount - 4]).setLineState(LineState.DRAWN);
                    }
                    if (rowCount - 4 > 0) {
                        ((Line) check[rowCount - 4][columnCount - 1]).setLineState(LineState.DRAWN);
                    }
                }
                case 3 -> {
                    ((Line) check[rowCount - 2][columnCount - 1]).setLineState(LineState.DRAWN);
                    ((Line) check[rowCount - 1][columnCount - 2]).setLineState(LineState.DRAWN);
                }
            }
        }
    }

    private void twoLinesAroundTheDot(Element[][] check) {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                if (check[row][column] instanceof Dot) {
                    int countOfDrawnLines = 0;
                    if (row != 0 && ((Line) check[row - 1][column]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (column != 0 && ((Line) check[row][column - 1]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (row != rowCount - 1 && ((Line) check[row + 1][column]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (column != columnCount - 1 && ((Line) check[row][column + 1]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (countOfDrawnLines == 2) {
                        if (row != 0 && ((Line) check[row - 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row - 1][column]).setLineState(LineState.MARKED);
                        }
                        if (column != 0 && ((Line) check[row][column - 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column - 1]).setLineState(LineState.MARKED);
                        }
                        if (row != rowCount - 1 && ((Line) check[row + 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row + 1][column]).setLineState(LineState.MARKED);
                        }
                        if (column != columnCount - 1 && ((Line) check[row][column + 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column + 1]).setLineState(LineState.MARKED);
                        }
                    }
                }
            }
        }
    }

    private void crossesAroundTheDot(Element[][] check) {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                if (check[row][column] instanceof Dot) {

                    int countOfLines = 0;
                    int countOfMarkedLines = 0;
                    if (row != 0) {
                        countOfLines++;
                        if (((Line) check[row - 1][column]).getLineState() == LineState.MARKED) {
                            countOfMarkedLines++;
                        }
                    }
                    if (column != 0) {
                        countOfLines++;
                        if (((Line) check[row][column - 1]).getLineState() == LineState.MARKED) {
                            countOfMarkedLines++;
                        }
                    }
                    if (row != rowCount - 1) {
                        countOfLines++;
                        if (((Line) check[row + 1][column]).getLineState() == LineState.MARKED) {
                            countOfMarkedLines++;
                        }
                    }
                    if (column != columnCount - 1) {
                        countOfLines++;
                        if (((Line) check[row][column + 1]).getLineState() == LineState.MARKED) {
                            countOfMarkedLines++;
                        }
                    }

                    if (countOfMarkedLines == countOfLines - 1) {
                        if (row != 0 && ((Line) check[row - 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row - 1][column]).setLineState(LineState.MARKED);
                        }
                        if (column != 0 && ((Line) check[row][column - 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column - 1]).setLineState(LineState.MARKED);
                        }
                        if (row != rowCount - 1 && ((Line) check[row + 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row + 1][column]).setLineState(LineState.MARKED);
                        }
                        if (column != columnCount - 1 && ((Line) check[row][column + 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column + 1]).setLineState(LineState.MARKED);
                        }
                    }
                }
            }
        }
    }

    private void crossesShowLines(Element[][] check) {
        for (int row = 1; row < rowCount - 1; row += 2) {
            for (int column = 1; column < columnCount - 1; column += 2) {
                if (check[row][column] instanceof Clue && ((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {

                    int countOfCrosses = 0;
                    if (((Line) check[row - 1][column]).getLineState() == LineState.MARKED) {
                        countOfCrosses++;
                    }
                    if (((Line) check[row][column - 1]).getLineState() == LineState.MARKED) {
                        countOfCrosses++;
                    }
                    if (((Line) check[row + 1][column]).getLineState() == LineState.MARKED) {
                        countOfCrosses++;
                    }
                    if (((Line) check[row][column + 1]).getLineState() == LineState.MARKED) {
                        countOfCrosses++;
                    }

                    if (4 - countOfCrosses == ((Clue) check[row][column]).getValue()) {
                        if (((Line) check[row - 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                        }
                        if (((Line) check[row][column - 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                        }
                        if (((Line) check[row + 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                        }
                        if (((Line) check[row][column + 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                        }
                    }
                }
            }
        }
    }

    private void linesShowCrosses(Element[][] check) {
        for (int row = 1; row < rowCount - 1; row += 2) {
            for (int column = 1; column < columnCount - 1; column += 2) {
                if (check[row][column] instanceof Clue && ((Clue) check[row][column]).getClueState() == ClueState.VISIBLE) {

                    int countOfDrawnLines = 0;
                    if (((Line) check[row - 1][column]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (((Line) check[row][column - 1]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (((Line) check[row + 1][column]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }
                    if (((Line) check[row][column + 1]).getLineState() == LineState.DRAWN) {
                        countOfDrawnLines++;
                    }

                    if (countOfDrawnLines == ((Clue) check[row][column]).getValue()) {
                        if (((Line) check[row - 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row - 1][column]).setLineState(LineState.MARKED);
                        }
                        if (((Line) check[row][column - 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column - 1]).setLineState(LineState.MARKED);
                        }
                        if (((Line) check[row + 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row + 1][column]).setLineState(LineState.MARKED);
                        }
                        if (((Line) check[row][column + 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column + 1]).setLineState(LineState.MARKED);
                        }
                    }
                }
            }
        }
    }

    private void noOtherWay(Element[][] check) {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                if (check[row][column] instanceof Dot) {

                    int countOfLines = 0;
                    int countOfDrawnLines = 0;
                    int countOfMarkedLines = 0;
                    if (row != 0) {
                        countOfLines++;
                        switch (((Line) check[row - 1][column]).getLineState()) {
                            case MARKED -> countOfMarkedLines++;
                            case DRAWN -> countOfDrawnLines++;
                        }
                    }
                    if (column != 0) {
                        countOfLines++;
                        switch (((Line) check[row][column - 1]).getLineState()) {
                            case MARKED -> countOfMarkedLines++;
                            case DRAWN -> countOfDrawnLines++;
                        }
                    }
                    if (row != rowCount - 1) {
                        countOfLines++;
                        switch (((Line) check[row + 1][column]).getLineState()) {
                            case MARKED -> countOfMarkedLines++;
                            case DRAWN -> countOfDrawnLines++;
                        }
                    }
                    if (column != columnCount - 1) {
                        countOfLines++;
                        switch (((Line) check[row][column + 1]).getLineState()) {
                            case MARKED -> countOfMarkedLines++;
                            case DRAWN -> countOfDrawnLines++;
                        }
                    }

                    if (countOfDrawnLines == 1 && countOfLines - countOfMarkedLines - countOfDrawnLines == 1) {
                        if (row != 0 && ((Line) check[row - 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row - 1][column]).setLineState(LineState.DRAWN);
                        }
                        if (column != 0 && ((Line) check[row][column - 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column - 1]).setLineState(LineState.DRAWN);
                        }
                        if (row != rowCount - 1 && ((Line) check[row + 1][column]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row + 1][column]).setLineState(LineState.DRAWN);
                        }
                        if (column != columnCount - 1 && ((Line) check[row][column + 1]).getLineState() == LineState.EMPTY) {
                            ((Line) check[row][column + 1]).setLineState(LineState.DRAWN);
                        }
                    }
                }
            }
        }
    }

    private boolean isTheSameField(Element[][] check, Element[][] lastCheck) {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (check[row][column] instanceof Line) {
                    if (((Line) check[row][column]).getLineState() != ((Line) lastCheck[row][column]).getLineState()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Element[][] copyElements(Element[][] elements) {
        Element[][] copyOfElements = new Element[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (elements[row][column] instanceof Dot) {
                    copyOfElements[row][column] = new Dot();
                } else if (elements[row][column] instanceof Clue) {
                    copyOfElements[row][column] = new Clue();
                    ((Clue) copyOfElements[row][column]).setValue(((Clue) elements[row][column]).getValue());
                    ((Clue) copyOfElements[row][column]).setClueState(((Clue) elements[row][column]).getClueState());
                } else if (elements[row][column] instanceof Line) {
                    copyOfElements[row][column] = new Line();
                    ((Line) copyOfElements[row][column]).setBeing(((Line) elements[row][column]).isBeing());
                    ((Line) copyOfElements[row][column]).setLineState(((Line) elements[row][column]).getLineState());
                }
            }
        }
        return copyOfElements;
    }

    public void drawLine(int row, int column) {
        if (elements[row][column] instanceof Line) {
            if (((Line) elements[row][column]).getLineState() == LineState.DRAWN) {
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            } else {
                ((Line) elements[row][column]).setLineState(LineState.DRAWN);
            }
            if(isSolved(elements)) {
                fieldState = GameState.SOLVED;
            }
        }
    }

    public void markLine(int row, int column) {
        if (elements[row][column] instanceof Line) {
            if (((Line) elements[row][column]).getLineState() == LineState.MARKED) {
                ((Line) elements[row][column]).setLineState(LineState.EMPTY);
            } else {
                ((Line) elements[row][column]).setLineState(LineState.MARKED);
            }
        }
    }

    boolean isSolved(Element[][] elements) {
        return isLinesAroundNumbersCorrect(elements) && isLineCycle(elements);
    }

    private boolean isLinesAroundNumbersCorrect(Element[][] elements) {
        for (int y = 1; y < rowCount; y += 2) {
            for (int x = 1; x < columnCount; x += 2) {
                int value = 0;
                if (((Line) elements[y - 1][x]).getLineState() == LineState.DRAWN) {
                    value++;
                }
                if (((Line) elements[y + 1][x]).getLineState() == LineState.DRAWN) {
                    value++;
                }
                if (((Line) elements[y][x - 1]).getLineState() == LineState.DRAWN) {
                    value++;
                }
                if (((Line) elements[y][x + 1]).getLineState() == LineState.DRAWN) {
                    value++;
                }
                if (((Clue) elements[y][x]).getValue() != value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLineCycle(Element[][] elements) {
        for (int row = 0; row < rowCount; row += 2) {
            for (int column = 0; column < columnCount; column += 2) {
                if (elements[row][column] instanceof Dot) {
                    int countOfLines = 0;
                    if (row != 0 && ((Line) elements[row-1][column]).getLineState() == LineState.DRAWN) {
                        countOfLines++;
                    }
                    if (column != 0 && ((Line) elements[row][column-1]).getLineState() == LineState.DRAWN) {
                        countOfLines++;
                    }
                    if (row != rowCount - 1 && ((Line) elements[row+1][column]).getLineState() == LineState.DRAWN) {
                        countOfLines++;
                    }
                    if (column != columnCount - 1 && ((Line) elements[row][column+1]).getLineState() == LineState.DRAWN) {
                        countOfLines++;
                    }

                    if(!(countOfLines == 0 || countOfLines == 2)) {
                        return false;
                    }
                }
            }
        }
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

    Element[][] getElements() {
        return elements;
    }

    public GameState getFieldState() {
        return fieldState;
    }

    /*private void printLoop(int[][] loop, int rowCountOpt, int columnCountOpt) {
        for (int y = 0; y < rowCountOpt; y++) {
            for (int x = 0; x < columnCountOpt; x++) {
                System.out.print(loop[y][x] + " ");
            }
            System.out.println();
        }
    }
*/
    /*private void printGameSolver(Element[][] elements) {
        System.out.print("       ");
        for (int column = 1; column <= (columnCount - 1) / 2; column++) {
            System.out.print(column + "     ");
        }
        System.out.println();
        System.out.println();
        for (int row = 0; row < rowCount; row++) {
            int letter = row / 2;
            if (row % 2 != 0) {
                System.out.print((char) ('A' + letter) + "  ");
            } else {
                System.out.print("   ");
            }
            for (int column = 0; column < columnCount; column++) {
                if (elements[row][column] instanceof Dot) {
                    System.out.print(" · ");
                } else if (elements[row][column] instanceof Clue) {
                    switch (((Clue) elements[row][column]).getClueState()) {
                        case VISIBLE -> System.out.print(" " + ((Clue) elements[row][column]).getValue() + " ");
                        case HIDDEN -> System.out.print("   ");
                    }


                } else if (elements[row][column] instanceof Line) {
                    if (((Line) elements[row][column]).getLineState() == LineState.DRAWN) {
                        if (row == 0 || row == rowCount - 1) {
                            System.out.print(" — ");
                        } else if (column == 0 || column == columnCount - 1) {
                            System.out.print(" | ");
                        } else if (column % 2 != 0) {
                            System.out.print(" — ");
                        } else {
                            System.out.print(" | ");
                        }
                    } else if (((Line) elements[row][column]).getLineState() == LineState.MARKED) {
                        System.out.print(" x ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();
        }
    }
*/
}
