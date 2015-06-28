/*
stores the values of the centre boundary
 */

public class CentreBoundary {

    private final int TOP;
    private final int BOTTOM;
    private final int LEFT;
    private final int RIGHT;

    public CentreBoundary(int top, int bottom, int left, int right) {
        this.TOP = top;
        this.BOTTOM = bottom;
        this.LEFT = left;
        this.RIGHT = right;
    }

    public int getTop() {
        return this.TOP;
    }

    public int getBottom() {
        return this.BOTTOM;
    }

    public int getLeft() {
        return this.LEFT;
    }

    public int getRight() {
        return this.RIGHT;
    }
}