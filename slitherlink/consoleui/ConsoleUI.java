package slitherlink.consoleui;

import slitherlink.Field;
import slitherlink.elements.Clue;
import slitherlink.elements.Dot;
import slitherlink.elements.Line;
import slitherlink.elements.LineState;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([DM])([A-J])([1-9])([UDRL])");
    private final Scanner scanner = new Scanner(System.in);
    private static Field field;

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        System.out.println("The rules:");
        System.out.println("1) X - exit");
        System.out.println("2) correct input for the game: 'DF9U'");
        System.out.println();
        System.out.println("     coordinates of the tile");
        System.out.println("     ↑↑");
        System.out.println(" [D][B2][D]                  [M][C3][R]                      [D][D4][L]");
        System.out.println("  ↓                           ↓                                       ↘      ");
        System.out.println(" to [D]raw the line          to [M]ark the line                          up              ");
        System.out.println("                                                                      · [U] ·            ");
        System.out.println("                                                              left ← [L] 2 [R] → right   ");
        System.out.println("                                                                      · [D] ·            ");
        System.out.println("                                                                         dawn            ");
        while(!field.isSolved(field.getElements())){
            printField();
            //printUnderField();
            processInput();
        }
        printField();
        System.out.println("game is solved");

    }
    private void processInput() {
        System.out.println("=========================================================================================================================================================");
        System.out.println("Enter command (X - exit, DB2D - to  drawn the dawn line, MD3R - to mark the right line, DD4L - to drawn the left line, to do line empty use the same function(D or M)): ");
        String line = scanner.nextLine().toUpperCase();
        if ("X".equals(line)) {
            System.exit(0);
        }

        Matcher matcher = COMMAND_PATTERN.matcher(line);
        if (matcher.matches()) {
            int row = (line.charAt(1) - 'A') * 2 + 1;
            int column = (Integer.parseInt(matcher.group(3))-1) * 2 + 1;
            switch (line.charAt(3)) {
                case 'U' -> row--;
                case 'D' -> row++;
                case 'L' -> column--;
                case 'R' -> column++;
            }
            if(column<field.getColumnCount()-1 && row<field.getRowCount()-1) {
                switch (line.charAt(0)) {
                    case 'D' -> field.drawLine(row, column);
                    case 'M' -> field.markLine(row, column);
                }
            }
            else {
                System.err.println("Wrong input " + line);
            }
        } else {
            System.err.println("Wrong input " + line);
        }
    }

    public static void printField() {
        System.out.print("       ");
        for(int column = 1 ; column <= (field.getColumnCount()-1)/2; column++){
            System.out.print(column+"     ");
        }
        System.out.println();
        System.out.println();
        for (int row = 0; row < field.getRowCount(); row++) {
            int letter=row/2;
            if(row%2!=0){
                System.out.print((char) ('A' + letter)+"  ");
            }
            else{
                System.out.print("   ");
            }
            for (int column = 0; column < field.getColumnCount(); column++) {
                if (field.getElement(row, column) instanceof Dot) {
                    System.out.print(" · ");
                } else if (field.getElement(row, column) instanceof Clue) {
                    switch (((Clue) field.getElement(row, column)).getClueState()){
                        case VISIBLE -> System.out.print(" " + ((Clue) field.getElement(row, column)).getValue() + " ");
                        case HIDDEN -> System.out.print("   ");
                    }


                } else if (field.getElement(row, column) instanceof Line) {
                    if (((Line) field.getElement(row, column)).getLineState() == LineState.DRAWN) {
                        if (row == 0 || row == field.getRowCount() - 1) {
                            System.out.print(" — ");
                        } else if (column == 0 || column == field.getColumnCount() - 1) {
                            System.out.print(" | ");
                        } else if (column % 2 != 0) {
                            System.out.print(" — ");
                        } else {
                            System.out.print(" | ");
                        }
                    } else if (((Line) field.getElement(row, column)).getLineState() == LineState.MARKED) {
                        System.out.print(" x ");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();
        }
    }

    public static void printUnderField() {
        for (int row = 0; row < field.getRowCount(); row++) {
            for (int column = 0; column < field.getColumnCount(); column++) {
                if (field.getElement(row, column) instanceof Dot) {
                    System.out.print(" · ");
                } else if (field.getElement(row, column) instanceof Clue) {
                    //System.out.print(" N ");
                    System.out.print(" " + ((Clue) field.getElement(row, column)).getValue() + " ");
                } else if (field.getElement(row, column) instanceof Line) {
                    if ((row == 0 || row == field.getRowCount() - 1) && ((Line) field.getElement(row, column)).isBeing()) {
                        System.out.print(" — ");
                    } else if ((column == 0 || column == field.getColumnCount() - 1) && ((Line) field.getElement(row, column)).isBeing()) {
                        System.out.print(" | ");
                    } else if (((Line) field.getElement(row, column)).isBeing() && column % 2 != 0) {
                        System.out.print(" — ");
                    } else if (((Line) field.getElement(row, column)).isBeing() && column % 2 == 0) {
                        System.out.print(" | ");
                    } else {
                        System.out.print("   ");
                    }
                } else {
                    System.out.print(field.getElement(row, column));
                }
            }
            System.out.println();
        }
    }
}