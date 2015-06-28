import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTools {

    private BufferedImage image;
    private PixelBlob targetPixels;

    public ImageTools(BufferedImage image) {
        this.image = image;
    }

    public String getPositionOfTarget() {

        Pixel[][] allPixels = getPixelArray(this.image);
        CentreBoundary boundary = getCentreBoundary(this.image);
        int[][] intArray = getIntArray(this.image);
        int[][] cleanedArray = cleanUpIntArray(intArray);
        this.targetPixels = getTargetPixelsClean(allPixels, cleanedArray);
        return this.targetPixels.getPosition(boundary);
    }

    public int getSizeOfTarget() {

        return this.targetPixels.getSize();
    }

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

    private int[][] cleanUpIntArray(int[][] array) {

        // traverse the 2D array twice
        for (int pass = 0; pass < 2; pass++) {
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

    private void printIntArray(int[][] array) {
        for (int i = 0; i < array[0].length; i++) {
            System.out.println();
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[j][i] + " ");
            }
        }
    }

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
