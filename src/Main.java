import Filters.LindaColorMasking2;
import core.DImage;

public class Main {

    public static void main(String[] args) {

        int height = 200;
        int width = 300;

        short[][] red = new short[height][width];
        short[][] green = new short[height][width];
        short[][] blue = new short[height][width];

        // Black background
        fill(red, (short) 0);
        fill(green, (short) 0);
        fill(blue, (short) 0);

        // Four colored rectangular regions for testing.
        addRectangle(red, green, blue, 20, 20, 50, 50, 215, 100, 100);
        addRectangle(red, green, blue, 90, 30, 125, 65, 30, 50, 120);
        addRectangle(red, green, blue, 170, 40, 205, 75, 215, 100, 100);
        addRectangle(red, green, blue, 230, 80, 270, 120, 30, 50, 120);

        DImage image = new DImage(red, green, blue);

        LindaColorMasking2 filter = new LindaColorMasking2();

        // Simulate clicking the first rectangle.
        filter.mouseClicked(30, 30, image);

        filter.processImage(image);

        System.out.println("Finished processing.");
    }

    private static void fill(short[][] grid, short value) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = value;
            }
        }
    }

    private static void addRectangle(
            short[][] red,
            short[][] green,
            short[][] blue,
            int left,
            int top,
            int right,
            int bottom,
            int rValue,
            int gValue,
            int bValue) {

        for (int r = top; r <= bottom; r++) {
            for (int c = left; c <= right; c++) {
                red[r][c] = (short) rValue;
                green[r][c] = (short) gValue;
                blue[r][c] = (short) bValue;
            }
        }
    }
}
