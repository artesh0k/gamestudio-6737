package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.slitherlink.field.Field;
import sk.tuke.gamestudio.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field pole = new Field(8, 8, 1, 1);
        ConsoleUI consoleui = new ConsoleUI(pole);
        consoleui.play();
    }
}
