package slitherlink;

import slitherlink.field.Field;
import slitherlink.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field pole = new Field(2, 2, 0.4, 0.5);
        ConsoleUI consoleui = new ConsoleUI(pole);
        consoleui.play();

    }
}
