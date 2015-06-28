import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageToPixel {

    private BufferedImage image;
    private Pixel[][] allPixels;
    private CentreBoundary boundary;
    private PixelBlob targetPixels;

    public ImageToPixel(BufferedImage image) {
        this.image = image;
    }

    public String getPositionOfTarget() {
        this.allPixels = getPixelArray(this.image);
        this.boundary = getCentreBoundary(this.image);
        this.targetPixels = addTargetPixelsHSB(allPixels);
        return targetPixels.getPosition(boundary);
    }

    public int getSizeOfTarget() {
        return this.targetPixels.getSize();
    }

    public void test() {
        try {
            BufferedImage image = ImageIO.read(new File("green-test-light.png"));
            //BufferedImage image = ImageIO.read(new File("1280x720_test.jpg"));
            //displayResults(image);

            Pixel[][] allPixels = getPixelArray(image);

            //System.out.println(allPixels[426][116].getGreen());

            CentreBoundary boundary = getCentreBoundary(image);

            //PixelBlob targetPixels = addTargetPixels(allPixels, 10, 5, 10, 34, 79, 31);
            PixelBlob targetPixels = addTargetPixelsHSB(allPixels);

            System.out.println(targetPixels.getPosition(boundary));

            /*
            System.out.println(test[0][0].getAlpha());
            System.out.println(test[0][0].getRed());
            System.out.println(test[0][0].getGreen());
            System.out.println(test[0][0].getBlue());

            CentreBoundary testB = getCentreBoundary(image);

            System.out.println(testB.getTop());
            System.out.println(testB.getBottom());
            System.out.println(testB.getLeft());
            System.out.println(testB.getRight());

            System.out.println(test[0][0].getPosition(testB));
            System.out.println(test[9][9].getPosition(testB));
            System.out.println(test[4][4].getPosition(testB));
            */

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    }

    public void displayResults(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                System.out.println("x,y: " + i + ", " + j);
                int pixel = image.getRGB(i, j);
                printPixelARGB(pixel);
                System.out.println("");
            }
        }
    }

    /*
    returns a 2D array of Pixel objects created from an image
     */
    public Pixel[][] getPixelArray(BufferedImage image) {

        int w = image.getWidth();
        int h = image.getHeight();

        Pixel[][] pixelArray = new Pixel[w][h];

        // adds every Pixel object required for the image into the array
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int currPixel = image.getRGB(i, j);
                int alpha = (currPixel >> 24) & 0xff;
                int red = (currPixel >> 16) & 0xff;
                int green = (currPixel >> 8) & 0xff;
                int blue = (currPixel) & 0xff;
                pixelArray[i][j] = new Pixel(i, j, alpha, red, green, blue);
            }
        }
        return pixelArray;
    }

    /*
    returns a CentreBoundary object storing values of the boundaries of the centre region,
    defined as a 20% deviation in all directions from the centre point
     */
    public CentreBoundary getCentreBoundary(BufferedImage image) {

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


    public PixelBlob addTargetPixels(Pixel[][] pixelArray, int rT, int gT, int bT, int rM, int gM, int bM) {
        // xT = +- tolerance for colour x
        // xM = midpoint for colour x
        PixelBlob targetPixels = new PixelBlob();

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                Pixel currPixel = pixelArray[i][j];
                int red = currPixel.getRed();
                if (red >= rM - rT && red <= rM + rT) {
                    int green = currPixel.getGreen();
                    if (green >= gM - gT && green <= gM + gT) {
                        int blue = currPixel.getBlue();
                        if (blue >= bM - bT && blue <= bM + bT) {
                            //System.out.println("i: " + i + " j: " + j);
                            targetPixels.addPixel(currPixel);
                        }
                    }
                }
            }
        }
        return targetPixels;
    }

    public PixelBlob addTargetPixelsHSB(Pixel[][] pixelArray) {
        PixelBlob targetPixels = new PixelBlob();

        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                Pixel currPixel = pixelArray[i][j];
                if (isGreen(currPixel)) {
                    targetPixels.addPixel(currPixel);
                }
            }
        }
        return targetPixels;
    }

    public boolean isGreen(Pixel pixel) {
        boolean green = false;

        float hsb[] = new float[3];

        Color.RGBtoHSB(pixel.getRed(), pixel.getGreen(), pixel.getBlue(), hsb);

        float deg = hsb[0]*360;

        // @todo
        // determine appropriate tolerance (90-150 did not work for green-test-light.png below-right)
        if (deg >=  120 && deg < 150) {
            green = true;
        }

        return green;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("green-test-light.png"));
        ImageToPixel test = new ImageToPixel(image);
        test.test();

        /*
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320, 240));
        webcam.open();
        ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
        */

    }

}