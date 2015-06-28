import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <h1>ImageTools</h1>
 * ImageTools objects are responsible for handling the processing of a single image frame.
 *
 * @author disposedtrolley
 * @since 27/06/2015
 */
public class ImageTools {

    private BufferedImage image;
    private PixelBlob targetPixels;

    /**
     * Constructor of the ImageTools class. Responsible for initialising the BufferedImage instance variable.
     *
     * @param image     the target image in a BufferedImage object.
     */
    public ImageTools(BufferedImage image) {
        this.image = image;
    }

    /**
     * Returns a String value of the position of the target area in the image.
     * <p>
     * Firstly, a 2D array of Pixel objects is created which contains the position and colour attributes of every
     * pixel in the image.
     * <p>
     * Secondly, the boundaries of the centre of the image are determined, and the values stored in a new
     * CentreBoundary object.
     * <p>
     * Thirdly, a 2D array of integers is created which holds values of 0 (if the pixel at the coordinates is not
     * green) and 1 (if the pixel at the coordinates is green). The same 2D integer array is "cleaned", which is an
     * attempt at removing stray green pixels so calculations regarding the size and position of the target area are
     * more accurate.
     * <p>
     * Finally, the PixelBlob instance variable is initialised to contain the final set of green pixels in the image.
     *
     * @return  a String value of the PixelBlob's position of either:
     *              above-left
     *              above
     *              above-right
     *              centre
     *              left
     *              right
     *              below-left
     *              below
     *              below-right
     */
    public String getPositionOfTarget() {

        Pixel[][] allPixels = getPixelArray(this.image);
        CentreBoundary boundary = getCentreBoundary(this.image);
        int[][] intArray = getIntArray(this.image);
        int[][] cleanedArray = cleanUpIntArray(intArray);
        this.targetPixels = getTargetPixelsClean(allPixels, cleanedArray);
        return this.targetPixels.getPosition(boundary);
    }

    /**
     * Returns the approximate size of the target area using the getSize() method of the PixelBlob object.
     *
     * @return  the approximate size of the target area.
     */
    public int getSizeOfTarget() {

        return this.targetPixels.getSize();
    }

    /**
     * Returns a 2D array of Pixel objects where each element corresponds to a pixel in the original image. The
     * dimensions of the array are identical to the dimensions of the image.
     *
     * The getRGB() method of the BufferedImage class is used to get the alpha, red, green, and blue values of the
     * image in ARGB colourspace. The method returns an integer from which the individual values are extracted. For
     * each pixel encountered in the image, a new Pixel object to store these attributes is added to the 2D array.
     *
     * @param image     the target image represented with a BufferedImage object.
     * @return          a 2D array of Pixel objects holding the attributes of each pixel in the image.
     */
    private Pixel[][] getPixelArray(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        Pixel[][] allPixels = new Pixel[w][h];

        // adds every Pixel object required for the image into the array
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int currPixel = image.getRGB(j, i);
                int alpha = (currPixel >> 24) & 0xff;
                int red = (currPixel >> 16) & 0xff;
                int green = (currPixel >> 8) & 0xff;
                int blue = (currPixel) & 0xff;
                allPixels[j][i] = new Pixel(j, i, alpha, red, green, blue);
            }
        }

        return allPixels;
    }

    /**
     * Returns a CentreBoundary object which stores the determined boundary of the centre of the object. The boundary
     * is currently set to a 20% deviation in all directions from the centre of the image.
     *
     * @param image     the target image represented with a BufferedImage object.
     * @return          a CentreBoundary object containing values which define the centre boundaries of the image.
     */
    private CentreBoundary getCentreBoundary(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        // calculating the bounds
        // calculations produce a double, which is rounded and casted to an int
        int top = (int) ((h/2) - (h*0.2) + 0.5);
        int bottom = (int) ((h/2) + (h*0.2) + 0.5);
        int left = (int) ((w/2) - (w*0.2) + 0.5);
        int right = (int) ((w/2) + (w*0.2) + 0.5);

        return new CentreBoundary(top, bottom, left, right);
    }

    @Deprecated
    private PixelBlob getTargetPixelsHSB(Pixel[][] allPixels) {

        PixelBlob targetPixels = new PixelBlob();

        // traverses through every pixel and adds green ones to targetPixels
        for (int i = 0; i < allPixels.length; i++) {
            for (int j = 0; j < allPixels[0].length; j++) {
                Pixel currPixel = allPixels[i][j];
                if (isGreen(currPixel)) {
                    targetPixels.addPixel(currPixel);
                }
            }
        }
        return targetPixels;
    }

    /**
     * Calculates whether a given Pixel object lies within the accepted tolerances to be regarded as green.
     * <p>
     * The red, green, and blue values of the Pixel object are extracted and converted into HSB colourspace. HSB is
     * used as it provides a linear representation of colour (a colour can only lie within a specified boundary,
     * e.g. 90-150 for every shade of green).
     *
     * @param pixel     the target Pixel object.
     * @return          a boolean value indicating whether the given Pixel object is green or not.
     */
    private boolean isGreen(Pixel pixel) {

        boolean green = false;
        float hsb[] = new float[3];

        // converting the image's RGB colourspace to HSB, which provides a linear range of colour values
        Color.RGBtoHSB(pixel.getRed(), pixel.getGreen(), pixel.getBlue(), hsb);
        float deg = hsb[0]*360;

        // @todo
        // determine appropriate tolerance (90-150 did not work for green-test-light.png below-right)
        if (deg >=  110 && deg < 160) {
            green = true;
        }

        return green;
    }

    /**
     * Returns a 2D array of integers containing values of either 0 (if the pixel at the element's coordinates is not
     * green) or 1 (if the pixel at the element's coordinates is green).
     * <p>
     * A 2D array of Pixel objects is created firstly, and is iterated over to produce the parallel integer array. The
     * isGreen() method is employed to determine whether the Pixel object is green or not.
     *
     * @param image     the target image represented as BufferedImage.
     * @return          a 2D array of integers with values or either 0 or 1 indicating if the pixel at a given
     *                  coordinate is green or not.
     */
    private int[][] getIntArray(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        int[][] intArray = new int[w][h];

        Pixel[][] allPixels = getPixelArray(image);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Pixel currPixel = allPixels[j][i];
                if (isGreen(currPixel)) {
                    intArray[j][i] = 1;
                } else {
                    intArray[j][i] = 0;
                }
            }
        }
        return intArray;
    }

    /**
     * Returns a "cleaned" 2D array of integers where an attempt has been made to zero out stray green pixels.
     * <p>
     * A very inefficient but simple algorithm is used to find elements with a value of 1, and set it to 0 if its
     * neighbouring element has a value of 0. This has the effect of removing stray 1's, which leads to more accurate
     * position and distance calculations (distance is still a bit sketchy).
     *
     * @param array     a 2D array of integers with stray 1's to be removed.
     * @return          a 2D array of integers with stray 1's removed.
     */
    private int[][] cleanUpIntArray(int[][] array) {

        // traverse the 2D array twice
        for (int pass = 0; pass < 4; pass++) {
            for (int i = 1; i < array.length - 1; i++) {
                for (int j = 1; j < array[0].length - 1; j++) {
                    if (array[i][j] == 1 && array[i][j+1] == 0) {
                        array[i][j] = 0;
                    }
                }
            }
        }
        return array;
    }

    /**
     * Prints a 2D array of integers to the console.
     * @param array     a 2D array of integers.
     */
    private void printIntArray(int[][] array) {
        for (int i = 0; i < array[0].length; i++) {
            System.out.println();
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[j][i] + " ");
            }
        }
    }

    /**
     * Returns a PixelBlob object with the final set of green Pixel objects added to its array.
     * <p>
     * This is also done quite inefficiently by iterating through the entire 2D array of Pixels, and determining
     * whether the corresponding coordinates in the cleaned 2D integer array has the value of 0 (in which case the
     * Pixel in the 2D Pixel array will not be added) or 1 (it will be added).
     *
     * @param allPixels         a 2D array of every Pixel object extracted from the image.
     * @param cleanedArray      a 2D array of integers which has been cleansed of stray 1's.
     * @return                  a PixelBlob object with the final set of green Pixel objects in its array.
     */
    private PixelBlob getTargetPixelsClean(Pixel[][] allPixels, int[][] cleanedArray) {
        PixelBlob targetPixels = new PixelBlob();

        // traverses through every pixel and adds green ones to targetPixels
        for (int i = 0; i < allPixels.length; i++) {
            for (int j = 0; j < allPixels[0].length; j++) {
                Pixel currPixel = allPixels[i][j];
                if (cleanedArray[i][j] == 1) {
                    targetPixels.addPixel(currPixel);
                }
            }
        }
        return targetPixels;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage testImage = ImageIO.read(new File("test-images/green-test-dark.png"));
        ImageTools testTools = new ImageTools(testImage);
        Pixel[][] testAllPixels = testTools.getPixelArray(testImage);
        int[][] testIntArray = testTools.getIntArray(testImage);

        System.out.println("testAllPixels length: " + testAllPixels.length + " testAllPixels width: " + testAllPixels[0].length);
        System.out.println("testIntArray length: " + testIntArray.length + " testIntArray width: " + testIntArray[0].length);

        // clean up the array
        int[][] testIntCleanedArray = testTools.cleanUpIntArray(testIntArray);
        System.out.println("testIntCleanedArray length: " + testIntCleanedArray.length + " testIntCleanedArray width: " + testIntCleanedArray[0].length);

        PixelBlob testTargetPixels = testTools.getTargetPixelsClean(testAllPixels, testIntCleanedArray);
        System.out.println(testTargetPixels.getSize());
    }

}
