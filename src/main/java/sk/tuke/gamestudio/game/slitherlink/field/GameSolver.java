package sk.tuke.gamestudio.game.slitherlink.field;

import sk.tuke.gamestudio.game.slitherlink.elements.*;

public class GameSolver {
    private final int rowCount;
    private final int columnCount;
    private final double percentSizeOfLoop;

    public GameSolver(int rowCount, int columnCount, double percentSizeOfLoop) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.percentSizeOfLoop = percentSizeOfLoop;
    }

    public boolean isGameCanBeSolved(Element[][] elementsFromOutside) {
        Element[][] check = copyElements(elementsFromOutside);
        Element[][] lastCheck = copyElements(check);

        noLinesAroundA0(check);
        adjacent0And3(check);
        diagonal0And3(check);
        twoAdjacent3s(check);
        twoDiagonals3s(check);
        anyNumberInACorner(check);

        while (!isTheSameField(check, lastCheck)) {
            lastCheck = copyElements(check);
            twoLinesAroundTheDot(check);
            crossesAroundTheDot(check);
            crossesShowLines(check);
            linesShowCrosses(check);
            noOtherWay(check);
            if (isSolved(check)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isGamePossibleToChange(Element[][] check) {
        for (int y = 1; y < rowCount - 1; y += 2) {
            for (int x = 1; x < columnCount - 1; x += 2) {
                if (((Clue) check[y][x]).getClueState() == ClueState.VISIBLE) {
                    ((Clue) check[y][x]).setClueState(ClueState.HIDDEN);
                    if (isGameCanBeSolved(check)) {
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
                        if (row != rowCount - 2 && ((Clue) check[row + 2][column]).getClueState() == ClueState.VISIBLE &&
                                ((int) Math.round((double) (((rowCount - 1) / 2) * ((columnCount - 1) / 2)) * percentSizeOfLoop))!=2) {
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
                        if (column != columnCount - 2 && ((Clue) check[row][column + 2]).getClueState() == ClueState.VISIBLE &&
                                ((int) Math.round((double) (((rowCount - 1) / 2) * ((columnCount - 1) / 2)) * percentSizeOfLoop))!=2) {
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

    public boolean isSolved(Element[][] elements) {
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

}
