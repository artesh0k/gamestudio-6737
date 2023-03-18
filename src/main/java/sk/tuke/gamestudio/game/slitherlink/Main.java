package sk.tuke.gamestudio.game.slitherlink;

import sk.tuke.gamestudio.game.slitherlink.core.field.Field;
import sk.tuke.gamestudio.game.slitherlink.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field pole = new Field(4, 4, 1, 1);
        ConsoleUI consoleui = new ConsoleUI(pole);
        consoleui.play();
    }
}
