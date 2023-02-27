package slitherlink;

import slitherlink.consoleui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Field pole = new Field(8, 1);
        ConsoleUI consoleui = new ConsoleUI(pole);
        consoleui.play();

    }
}
