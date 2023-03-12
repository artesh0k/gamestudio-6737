package sk.tuke.gamestudio.game.slitherlink;

import sk.tuke.gamestudio.game.slitherlink.field.Field;
import sk.tuke.gamestudio.game.slitherlink.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field pole = new Field(2, 2, 0.4, 0.5);
        ConsoleUI consoleui = new ConsoleUI(pole);
        consoleui.play();
    }
}
