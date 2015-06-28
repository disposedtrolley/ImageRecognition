/**
 * <h1>CentreBoundary</h1>
 * CentreBoundary objects store relevant attributes for defining the boundaries of what is considered the "centre"
 * of the image.
 *
 * @author disposedtrolley
 * @since 25/06/2015
 */
public class CentreBoundary {

    private final int TOP;
    private final int BOTTOM;
    private final int LEFT;
    private final int RIGHT;

    /**
     * Constructor for the CentreBoundary class. Responsible for initialising instance variables only.
     *
     * @param top       an integer corresponding to the top of the boundary.
     * @param bottom    an integer corresponding to the bottom of the boundary.
     * @param left      an integer corresponding to the left of the boundary.
     * @param right     an integer corresponding to the right of the boundary.
     */
    public CentreBoundary(int top, int bottom, int left, int right) {
        this.TOP = top;
        this.BOTTOM = bottom;
        this.LEFT = left;
        this.RIGHT = right;
    }

    /**
     * Returns an integer corresponding to the top of the boundary.
     *
     * @return  an integer corresponding to the top of the boundary.
     */
    public int getTop() {
        return this.TOP;
    }

    /**
     * Returns an integer corresponding to the bottom of the boundary.
     *
     * @return  an integer corresponding to the bottom of the boundary.
     */
    public int getBottom() {
        return this.BOTTOM;
    }

    /**
     * Returns an integer corresponding to the left of the boundary.
     *
     * @return  an integer corresponding to the left of the boundary.
     */
    public int getLeft() {
        return this.LEFT;
    }

    /**
     * Returns an integer corresponding to the right of the boundary.
     *
     * @return  an integer corresponding to the right of the boundary.
     */
    public int getRight() {
        return this.RIGHT;
    }
}