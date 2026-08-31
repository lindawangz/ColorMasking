package core;

public class DImage {

    private short[][] red;
    private short[][] green;
    private short[][] blue;

    public DImage(int height, int width) {
        red = new short[height][width];
        green = new short[height][width];
        blue = new short[height][width];
    }

    public DImage(short[][] red, short[][] green, short[][] blue) {
        this.red = copy(red);
        this.green = copy(green);
        this.blue = copy(blue);
    }

    public short[][] getRedChannel() {
        return red;
    }

    public short[][] getGreenChannel() {
        return green;
    }

    public short[][] getBlueChannel() {
        return blue;
    }

    public void setColorChannels(
            short[][] red,
            short[][] green,
            short[][] blue) {

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public short[][] getBWPixelGrid() {

        short[][] bw = new short[red.length][red[0].length];

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[r].length; c++) {

                int avg =
                        (red[r][c] + green[r][c] + blue[r][c]) / 3;

                bw[r][c] = (short) avg;
            }
        }

        return bw;
    }

    private static short[][] copy(short[][] input) {

        short[][] output = new short[input.length][];

        for (int i = 0; i < input.length; i++) {
            output[i] = input[i].clone();
        }

        return output;
    }
}
