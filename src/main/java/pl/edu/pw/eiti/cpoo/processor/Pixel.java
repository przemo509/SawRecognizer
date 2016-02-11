package pl.edu.pw.eiti.cpoo.processor;

public class Pixel {
    final int x;
    final int y;
    final int referencedPixelValue;

    public Pixel(int x, int y, int referencedPixelValue) {
        this.x = x;
        this.y = y;
        this.referencedPixelValue = referencedPixelValue;
    }
}
