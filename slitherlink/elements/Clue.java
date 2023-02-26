package slitherlink.elements;

public class Clue extends Element{
    private int value;
    private ClueState clueState;

    public ClueState getClueState() {
        return clueState;
    }

    public void setClueState(ClueState clueState) {
        this.clueState = clueState;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
