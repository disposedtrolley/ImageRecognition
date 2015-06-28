/**
 * <h1>Pixel</h1>
 * Pixel objects store the required attributes for each pixel within an image.
 *
 * @author disposedtrolley
 * @since 25/06/2015
 */
public class Pixel {

    private final int ALPHA;
    private final int RED;
    private final int GREEN;
    private final int BLUE;
    private final int X_POS;
    private final int Y_POS;

    /**
     * Constructor for the Pixel class. Initialises instance variables only.
     *
     * @param i         x position of the pixel within the image.
     * @param j         y position of the pixel within the image.
     * @param alpha     alpha value of the pixel in ARGB colourspace.
     * @param red       red value of the pixel in ARGB colourspace.
     * @param green     green value of the pixel in ARGB colourspace.
     * @param blue      blue value of the pixel in ARGB colourspace.
     */
    public Pixel(int i, int j, int alpha, int red, int green, int blue) {
        this.X_POS = i;
        this.Y_POS = j;
        this.ALPHA = alpha;
        this.RED = red;
        this.GREEN = green;
        this.BLUE = blue;
    }

    /**
     * Returns the alpha value of the pixel object.
     *
     * @return  alpha value of the pixel in ARGB colourspace.
     */
    public int getAlpha() {
        return this.ALPHA;
    }

    /**
     * Returns the red value of the pixel object.
     *
     * @return  red value of the pixel in ARGB colourspace.
     */
    public int getRed() {
        return this.RED;
    }

    /**
     * Returns the green value of the pixel object.
     *
     * @return  green value of the pixel in ARGB colourspace.
     */
    public int getGreen() {
        return this.GREEN;
    }

    /**
     * Returns the blue value of the pixel object.
     *
     * @return  blue value of the pixel in ARGB colourspace.
     */
    public int getBlue() {
        return this.BLUE;
    }

    /**
     * Returns the x position of the pixel object.
     *
     * @return  x position of the pixel in the image.
     */
    public int getX() {
        return this.X_POS;
    }

    /**
     * Returns the y position of the pixel object.
     *
     * @return  y position of the pixel in the image.
     */
    public int getY() {
        return this.Y_POS;
    }

    /**
     * Calculates and returns the position of the pixel within the image according to the CentreBoundary object
     * corresponding to the image.
     *
     * @param b     a CentreBoundary object which defines the boundaries of what is considered the "centre"
     *              of the image.
     * @return      a String value of the Pixel's position of either:
     *                  above-left
     *                  above
     *                  above-right
     *                  centre
     *                  left
     *                  right
     *                  below-left
     *                  below
     *                  below-right
     */
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