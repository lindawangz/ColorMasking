package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class LindaColorMasking2 implements PixelFilter, Interactive {

    double threshold = 75;

    int targetR1;
    int targetG1;
    int targetB1;

    int targetR2;
    int targetG2;
    int targetB2;

    int targetR3;
    int targetG3;
    int targetB3;

    ArrayList<Point1> whitePixels = new ArrayList<>();

    @Override
    public DImage processImage(DImage img) {

        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        // Clear old points before processing a new frame/image.
        whitePixels.clear();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {

                double xDist1 = Math.abs(red[r][c] - targetR1);
                double yDist1 = Math.abs(green[r][c] - targetG1);
                double zDist1 = Math.abs(blue[r][c] - targetB1);

                double xDist2 = Math.abs(red[r][c] - targetR2);
                double yDist2 = Math.abs(green[r][c] - targetG2);
                double zDist2 = Math.abs(blue[r][c] - targetB2);

                double xDist3 = Math.abs(red[r][c] - targetR3);
                double yDist3 = Math.abs(green[r][c] - targetG3);
                double zDist3 = Math.abs(blue[r][c] - targetB3);

                double distance1 = Math.sqrt(
                        Math.pow(xDist1, 2)
                                + Math.pow(yDist1, 2)
                                + Math.pow(zDist1, 2));

                double distance2 = Math.sqrt(
                        Math.pow(xDist2, 2)
                                + Math.pow(yDist2, 2)
                                + Math.pow(zDist2, 2));

                double distance3 = Math.sqrt(
                        Math.pow(xDist3, 2)
                                + Math.pow(yDist3, 2)
                                + Math.pow(zDist3, 2));

                if (distance1 < threshold
                        || distance2 < threshold
                        || distance3 < threshold) {

                    red[r][c] = 255;
                    green[r][c] = 255;
                    blue[r][c] = 255;

                } else {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }

        img.setColorChannels(red, green, blue);

        short[][] pixels = img.getBWPixelGrid();

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] == 255) {
                    whitePixels.add(new Point1(i, j));
                }
            }
        }

        if (!whitePixels.isEmpty()) {
            findCenters(img);
        }

        return img;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {

        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        if (mouseY < 0 || mouseY >= red.length
                || mouseX < 0 || mouseX >= red[0].length) {
            return;
        }

        targetR1 = red[mouseY][mouseX];
        targetG1 = green[mouseY][mouseX];
        targetB1 = blue[mouseY][mouseX];

        targetR2 = 215;
        targetG2 = 100;
        targetB2 = 100;

        targetR3 = 30;
        targetG3 = 50;
        targetB3 = 120;

        System.out.println(targetR1 + " " + targetG1 + " " + targetB1);

        ratio(red, green, blue, targetR1, targetB1, targetG1);
    }

    public void findCenters(DImage img) {

        short[][] pixels = img.getBWPixelGrid();

        // Try to locate up to 4 centers.
        for (int centerNumber = 1;
             centerNumber <= 4 && !whitePixels.isEmpty();
             centerNumber++) {

            Point1 randomPoint =
                    whitePixels.get((int) (Math.random() * whitePixels.size()));

            int top = findTopRow(randomPoint, pixels);
            int bottom = findBottomRow(randomPoint, pixels);
            int midY = calcMiddleVert(top, bottom);

            int left = findLeftCol(randomPoint, midY, pixels);
            int right = findRightCol(randomPoint, midY, pixels);
            int midX = calcMiddleHorizontal(left, right);

            System.out.println(
                    "center " + centerNumber + ": (" + midX + ", " + midY + ")");

            clearPoints(randomPoint, midY, pixels);
        }
    }

    private void clearPoints(Point1 randomPoint, int midY, short[][] pixels) {

        int top = findTopRow(randomPoint, pixels);
        int bottom = findBottomRow(randomPoint, pixels);
        int left = findLeftCol(randomPoint, midY, pixels);
        int right = findRightCol(randomPoint, midY, pixels);

        for (int i = whitePixels.size() - 1; i >= 0; i--) {
            Point1 p = whitePixels.get(i);

            if (p.getRow() >= top
                    && p.getRow() <= bottom
                    && p.getCol() >= left
                    && p.getCol() <= right) {

                whitePixels.remove(i);
            }
        }
    }

    private int calcMiddleVert(int topRow, int bottomRow) {
        return (topRow + bottomRow) / 2;
    }

    private int calcMiddleHorizontal(int leftCol, int rightCol) {
        return (leftCol + rightCol) / 2;
    }

    private int findTopRow(Point1 randomPoint, short[][] pixels) {

        int col = randomPoint.getCol();
        int row = randomPoint.getRow();

        while (row > 0 && pixels[row][col] == 255) {
            row--;
        }

        if (pixels[row][col] != 255 && row < pixels.length - 1) {
            row++;
        }

        return row;
    }

    private int findBottomRow(Point1 randomPoint, short[][] pixels) {

        int col = randomPoint.getCol();
        int row = randomPoint.getRow();

        while (row < pixels.length - 1 && pixels[row][col] == 255) {
            row++;
        }

        if (pixels[row][col] != 255 && row > 0) {
            row--;
        }

        return row;
    }

    private int findLeftCol(Point1 randomPoint, int middleVert, short[][] pixels) {

        int col = randomPoint.getCol();

        while (col > 0 && pixels[middleVert][col] == 255) {
            col--;
        }

        if (pixels[middleVert][col] != 255 && col < pixels[0].length - 1) {
            col++;
        }

        return col;
    }

    private int findRightCol(Point1 randomPoint, int middleVert, short[][] pixels) {

        int col = randomPoint.getCol();

        while (col < pixels[0].length - 1
                && pixels[middleVert][col] == 255) {
            col++;
        }

        if (pixels[middleVert][col] != 255 && col > 0) {
            col--;
        }

        return col;
    }

    @Override
    public void keyPressed(char key) {

        if (key == 'a') {
            threshold += 5;
        }

        if (key == 'm') {
            threshold -= 5;
        }

        System.out.println("threshold = " + threshold);
    }

    public void ratio(
            short[][] red,
            short[][] green,
            short[][] blue,
            double targetRed,
            double targetBlue,
            double targetGreen) {

        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[0].length; j++) {

                double arrayRed = red[i][j];
                double arrayBlue = blue[i][j];
                double arrayGreen = green[i][j];

                if (targetBlue == 0) targetBlue = 1;
                if (targetRed == 0) targetRed = 1;
                if (targetGreen == 0) targetGreen = 1;

                if (arrayBlue == 0) arrayBlue = 1;
                if (arrayGreen == 0) arrayGreen = 1;
                if (arrayRed == 0) arrayRed = 1;

                double redGreenArrayRatio = arrayRed / arrayGreen;
                double greenBlueArrayRatio = arrayGreen / arrayBlue;

                double targetRedGreenRatio = targetRed / targetGreen;
                double targetGreenBlueRatio = targetGreen / targetBlue;

                if (redGreenArrayRatio > targetRedGreenRatio - 0.25
                        && redGreenArrayRatio < targetRedGreenRatio + 0.25) {

                    if (greenBlueArrayRatio > targetGreenBlueRatio - 0.25
                            && greenBlueArrayRatio < targetGreenBlueRatio + 0.25) {

                        red[i][j] = (short) targetRed;
                        green[i][j] = (short) targetGreen;
                        blue[i][j] = (short) targetBlue;
                    }
                }
            }
        }
    }
}
