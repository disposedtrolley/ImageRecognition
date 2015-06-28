import java.util.ArrayList;
import java.util.Collections;

public class PixelBlob {
    private ArrayList<Pixel> pixelArray;

    public PixelBlob() {
        pixelArray = new ArrayList<>();
    }

    public void addPixel(Pixel newPixel) {
        this.pixelArray.add(newPixel);
    }

    public String getDistance() {
        return null;
    }

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

            System.out.println("xValues 10: " + xValues.get(10));
            System.out.println("xValues max: " + xValues.get(xValues.size()-1));

            System.out.println("yValues 10: " + yValues.get(10));
            System.out.println("yValues max: " + yValues.get(yValues.size()-1));
        } else {
            width = 0;
            length = 0;
        }


        return width * length;
    }

    public String getPosition(CentreBoundary b) {

        String position;

        if (this.pixelArray.size() > 0) {
            int x = calculateMedianX();
            int y = calculateMedianY();

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

    private int calculateMedianX() {
        ArrayList<Integer> xValues = new ArrayList<>();
        for (Pixel pixel : this.pixelArray) {
            xValues.add(pixel.getX());
        }
        Collections.sort(xValues);
        return xValues.get(xValues.size()/2);
    }

    private int calculateMedianY() {
        ArrayList<Integer> yValues = new ArrayList<>();
        for (Pixel pixel : this.pixelArray) {
            yValues.add(pixel.getY());
        }
        Collections.sort(yValues);
        return yValues.get(yValues.size()/2);
    }

    public ArrayList<Pixel> getPixelArray() {
        return this.pixelArray;
    }
}