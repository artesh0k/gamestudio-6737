package sk.tuke.gamestudio.game.slitherlink.consoleui;

import com.sun.source.doctree.SystemPropertyTree;
import sk.tuke.gamestudio.game.slitherlink.elements.Clue;
import sk.tuke.gamestudio.game.slitherlink.elements.Line;
import sk.tuke.gamestudio.game.slitherlink.elements.LineState;
import sk.tuke.gamestudio.game.slitherlink.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.field.Field;
import sk.tuke.gamestudio.game.slitherlink.field.GameState;
import sk.tuke.gamestudio.game.slitherlink.elements.Dot;
import sk.tuke.gamestudio.game.slitherlink.service.ScoreService;
import sk.tuke.gamestudio.game.slitherlink.service.ScoreServiceJDBS;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    public static final String GAME_NAME = "slitherlink";
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([DM])([A-J])([1-9])([UDRL])");
    private final Scanner scanner = new Scanner(System.in);
    private static Field field;

    private ScoreService scoreService = new ScoreServiceJDBS();
    public ConsoleUI(Field pole) {
        field = pole;
    }

    public void play() {
        printTopScores();
        printRules();
        while (field.getFieldState() == GameState.PLAYING) {
            printField();
            processInput();
        }
        if(field.getFieldState() == GameState.SOLVED) {
            printField();
            System.out.println("game is solved");
            scoreService.addScore(new Score(System.getProperty("user.name"), "slitherlink", 1100, new Date()));
        }
    }

    private static void printRules() {
        System.out.println("The rules:");
        System.out.println("1) X - to exit");
        System.out.println("2) S - to show the solution");
        System.out.println("3) correct input for the game: 'DF9U'");
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
    }

    private void processInput() {
        System.out.println("=========================================================================================================================================================");
        System.out.println("Enter command (X - exit, DB2D - to  drawn the dawn line, MD3R - to mark the right line, DD4L - to drawn the left line, to do line empty use the same function(D or M)): ");
        String line = scanner.nextLine().toUpperCase();
        if ("X".equals(line)) {
            System.exit(0);
        }
        if("S".equals(line)){
            printSolvedField();
            System.exit(0);
        }

        Matcher matcher = COMMAND_PATTERN.matcher(line);
        if (matcher.matches()) {
            int row = (line.charAt(1) - 'A') * 2 + 1;
            int column = (Integer.parseInt(matcher.group(3)) - 1) * 2 + 1;
            if (column < field.getColumnCount() && row < field.getRowCount()) {
                switch (line.charAt(3)) {
                    case 'U' -> row--;
                    case 'D' -> row++;
                    case 'L' -> column--;
                    case 'R' -> column++;
                }
                switch (line.charAt(0)) {
                    case 'D' -> field.drawLine(row, column);
                    case 'M' -> field.markLine(row, column);
                }
            } else {
                System.err.println("Wrong input " + line);
            }
        } else {
            System.err.println("Wrong input " + line);
        }
    }

    public static void printField() {

        printColumnsNumbers();

        for (int row = 0; row < field.getRowCount(); row++) {
            printRowsLetters(row);
            for (int column = 0; column < field.getColumnCount(); column++) {
                if (field.getElement(row, column) instanceof Dot) {
                    System.out.print(" · ");

                } else if (field.getElement(row, column) instanceof Clue) {
                    printClue(row, column);

                } else if (field.getElement(row, column) instanceof Line) {
                    printLine(row, column);
                }
            }
            System.out.println();
        }
    }

    private static void printRowsLetters(int row) {
        int letter = row / 2;
        if (row % 2 != 0) {
            System.out.print((char) ('A' + letter) + "  ");
        } else {
            System.out.print("   ");
        }
    }

    private static void printColumnsNumbers() {
        System.out.print("       ");
        for (int column = 1; column <= (field.getColumnCount() - 1) / 2; column++) {
            System.out.print(column + "     ");
        }

        System.out.println();
        System.out.println();
    }

    private static void printClue(int row, int column) {
        switch (((Clue) field.getElement(row, column)).getClueState()) {
            case VISIBLE -> System.out.print(" " + ((Clue) field.getElement(row, column)).getValue() + " ");
            case HIDDEN -> System.out.print("   ");
        }
    }
    private static void printLine(int row, int column) {
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

    public void printSolvedField() {
        printColumnsNumbers();
        for (int row = 0; row < field.getRowCount(); row++) {
            printRowsLetters(row);
            for (int column = 0; column < field.getColumnCount(); column++) {
                if (field.getElement(row, column) instanceof Dot) {
                    System.out.print(" · ");
                } else if (field.getElement(row, column) instanceof Clue) {
                    printClue(row, column);
                } else if (field.getElement(row, column) instanceof Line) {
                    printSolvedLine(row, column);
                }
            }
            System.out.println();
        }
    }

    private static void printSolvedLine(int row, int column) {
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
    }

    private void printTopScores() {
        List<Score> scores = scoreService.getTopScores(GAME_NAME);
        System.out.println("-------------------------------------------------");
        for(Score score : scores) {
            System.out.printf("%s %d\n", score.getPlayer(), score.getPoints());
        }
        System.out.println("-------------------------------------------------");
    }

}