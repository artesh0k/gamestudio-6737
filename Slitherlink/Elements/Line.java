package Slitherlink.Elements;

public class Line extends Element{
    private LineState lineState;
    private boolean being;

    public LineState getLineState() {
        return lineState;
    }

    public void setLineState(LineState lineState) {
        this.lineState = lineState;
    }

    public void setBeing(boolean being) {
        this.being = being;
    }
}
