import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>PixelBlob</h1>
 * PixelBlob objects contain an array of Pixel objects which satisfy the conditions for the target area; i.e. the
 * pixels have been determined to be green.
 *
 * @author disposedtrolley
 * @since 25/06/2015
 */
public class PixelBlob {

    private ArrayList<Pixel> pixelArray;
    private ArrayList<Integer> xArray;
    private ArrayList<Integer> yArray;

    /**
     * Constructor for the PixelBlob class. Initialises instance variables only.
     */
    public PixelBlob() {
        pixelArray = new ArrayList<>();
        xArray = new ArrayList<>();
        yArray = new ArrayList<>();
    }

    /**
     * Adds a new Pixel object to the Pixel array.
     *
     * @param newPixel  a Pixel object which satisfies colour tolerances (is green).
     */
    public void addPixel(Pixel newPixel) {
        this.pixelArray.add(newPixel);
    }

    /**
     * Returns a String value indicating z axis movement of the target area.
     *
     * @return  currently null as the method has not been implemented, possible values are:
     *              same
     *              closer
     *              farther
     *          from the current position (at time-1).
     */
    public String getDistance() {
        return null;
    }

    /**
     * Returns the approximate size of the target area by getting the values of the corner points and multiplying
     * the calculated length and width.
     * <p>
     * Two arrays are used to hold the x and y coordinates of each pixel respectively.
     * Both arrays are sorted, and the minimum+10 and maximum-1 values are taken from xArray (to produce the width),
     * and yArray (to produce the length). This was done as it was found that the value of the element at index 0 is
     * often incorrect.
     *
     * @return  the approximate size of the target area.
     */
    public int getSize() {
        ArrayList<Integer> xValues = new ArrayList<>();
        ArrayList<Integer> yValues = new ArrayList<>();
        for (Pixel pixel : this.pixelArray) {
            xValues.add(pixel.getX());
            yValues.add(pixel.getY());
        }
        Collections.sort(xValues);
        Collections.sort(yValues);

        int width;
        int length;

        if (xValues.size() > 10 && yValues.size() > 10) {
            width = xValues.get(xValues.size()-1) - xValues.get(10);
            length = yValues.get(yValues.size()-1) - yValues.get(10);

            /*
            System.out.println("xValues 10: " + xValues.get(10));
            System.out.println("xValues max: " + xValues.get(xValues.size()-1));

            System.out.println("yValues 10: " + yValues.get(10));
            System.out.println("yValues max: " + yValues.get(yValues.size()-1));
            */
        } else {
            width = 0;
            length = 0;
        }


        return width * length;
    }

    /**
     * Returns a String value of the relative position of the target area from the centre of the image, determined by
     * the attributes of a CentreBoundary object.
     *
     * @param b     a CentreBoundary object corresponding to the current image being processed.
     * @return      a String value of the PixelBlob's position of either:
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

        String position;

        if (this.pixelArray.size() > 0) {
            int x = calculateMedianX();
            int y = calculateMedianY();

            System.out.println("med x: " + x);
            System.out.println("med y: " + y);

            // get position of each pixel and calculate the average position
            // hmm should we get the mean or the median??? median would be less susceptible to erroneous pixels being added



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

        } else {
            position = "error";
        }

        return position;
    }

    /**
     * Calculates and returns the median x value of all the Pixels in the PixelBlob. The median was chosen instead
     * of the average to prevent stray green pixels in other regions of the image from influencing the result.
     *
     * @return  the median value of all of the x values.
     */
    private int calculateMedianX() {
        for (Pixel pixel : this.pixelArray) {
            this.xArray.add(pixel.getX());
        }
        Collections.sort(this.xArray);
        return this.xArray.get(this.xArray.size()/2);
    }

    /**
     * Calculates and returns the median y value of all the Pixels in the PixelBlob. The median was chosen instead
     * of the average to prevent stray green pixels in other regions of the image from influencing the result.
     *
     * @return  the median value of all of the y values.
     */
    private int calculateMedianY() {
        for (Pixel pixel : this.pixelArray) {
            this.yArray.add(pixel.getY());
        }
        Collections.sort(this.yArray);
        return this.yArray.get(this.yArray.size()/2);
    }

    /**
     * Returns the Pixels in the PixelBlob as an array.
     *
     * @return  an ArrayList of Pixels which were all determined to be green.
     */
    public ArrayList<Pixel> getPixelArray() {
        return this.pixelArray;
    }
}