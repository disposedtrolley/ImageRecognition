/*
stores the coordinates of the pixel and its ARGB values
 */

public class Pixel {

    private final int ALPHA;
    private final int RED;
    private final int GREEN;
    private final int BLUE;
    private final int X_POS;
    private final int Y_POS;

    public Pixel(int i, int j, int alpha, int red, int green, int blue) {
        this.X_POS = i;
        this.Y_POS = j;
        this.ALPHA = alpha;
        this.RED = red;
        this.GREEN = green;
        this.BLUE = blue;
    }

    public int getAlpha() {
        return this.ALPHA;
    }

    public int getRed() {
        return this.RED;
    }

    public int getGreen() {
        return this.GREEN;
    }

    public int getBlue() {
        return this.BLUE;
    }

    public int getX() {
        return this.X_POS;
    }

    public int getY() {
        return this.Y_POS;
    }

    public String getPosition(CentreBoundary b) {

        int x = this.X_POS;
        int y = this.Y_POS;

        String position;

        if (y <= b.getTop() && x <= b.getLeft()) {
            position = "above-left";
        } else if (y <= b.getTop() && x <= b.getRight() && x >= b.getLeft()) {
            position = "above";
        } else if (y <= b.getTop() && x >= b.getRight()) {
            position = "above-right";
        } else if (y <= b.getBottom() && y >= b.getTop() && x <= b.getRight() && x >= b.getLeft()) {
            position = "centre";
        } else if (y <= b.getBottom() && y >= b.getTop() && x <= b.getLeft()) {
            position = "left";
        } else if (y <= b.getBottom() && y >= b.getTop() && x >= b.getLeft()) {
            position = "right";
        } else if (y >= b.getBottom() && x <= b.getLeft()) {
            position = "below-left";
        } else if (y >= b.getBottom() && x <= b.getRight() && x >= b.getLeft()) {
            position = "below";
        } else if (y >= b.getBottom() && x >= b.getRight()) {
            position = "below-right";
        } else {
            position = "error";
        }
        return position;
    }

}