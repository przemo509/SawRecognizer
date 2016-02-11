package pl.edu.pw.eiti.cpoo.processor;

import java.lang.reflect.Array;
import java.util.Stack;

public class ImageProcessor {

    private static final int THRESHOLD = 10;
    public static final int BYTE = 256;

    public static boolean[][] binarize(int[][] image) {
        boolean[][] object = (boolean[][]) Array.newInstance(boolean.class, image.length, image[0].length);
        boolean[][] visited = (boolean[][]) Array.newInstance(boolean.class, image.length, image[0].length);
        Stack<Pixel> pixelStack = new Stack<>();
        pixelStack.push(new Pixel(0, 0, image[0][0])); // add first pixel to stack

        while (!pixelStack.empty()) {
            Pixel p = pixelStack.pop();
            visited[p.x][p.y] = true;

            int value = image[p.x][p.y];
            if (value >= p.referencedPixelValue - THRESHOLD && value <= p.referencedPixelValue + THRESHOLD) {
                object[p.x][p.y] = true;
            }

            pushNeighboursToStack(pixelStack, visited, p, (int) (0.9 * value + 0.1 * p.referencedPixelValue));
        }
        return object;
    }

    private static void pushNeighboursToStack(Stack<Pixel> stack, boolean[][] visited, Pixel p, int referencedValue) {
        pushToStack(stack, visited, p.x - 1, p.y, referencedValue);
        pushToStack(stack, visited, p.x + 1, p.y, referencedValue);
        pushToStack(stack, visited, p.x, p.y - 1, referencedValue);
        pushToStack(stack, visited, p.x, p.y + 1, referencedValue);
    }

    private static void pushToStack(Stack<Pixel> stack, boolean[][] visited, int x, int y, int referencedPixelValue) {
        if (x < 0 || x >= visited.length || y < 0 || y >= visited[0].length || visited[x][y]) {
            return;
        }
        stack.push(new Pixel(x, y, referencedPixelValue));
    }

    public static int[][] equalizeHistogram(int[][] image) {
        int[] h = new int[BYTE]; // histogram
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                h[image[i][j]]++;
            }
        }

        int[] hCum = new int[BYTE]; // cumulated histogram
        hCum[0] = h[0];
        for (int i = 1; i < BYTE; i++) {
            hCum[i] = hCum[i - 1] + h[i];
        }

        int minHCum = hCum[0];
        for (int i = 1; i < BYTE; i++) {
            if(hCum[i] != 0) {
                minHCum = hCum[i];
                break;
            }
        }

        double factor = (double)(BYTE - 1) / (imageWidth * imageHeight - minHCum);
        int[] hEq = new int[BYTE]; // histogram equalized
        for (int i = 0; i < BYTE; i++) {
            hEq[i] = (int)((hCum[i] - minHCum) * factor);
        }

        int[][] equalizedImage = (int[][]) Array.newInstance(int.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                equalizedImage[i][j] = hEq[image[i][j]];
            }
        }

        return equalizedImage;
    }
}
