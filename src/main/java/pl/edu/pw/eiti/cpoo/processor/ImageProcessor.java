package pl.edu.pw.eiti.cpoo.processor;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Stack;

public class ImageProcessor {

    private static final int THRESHOLD = 10;

    public static boolean[][] binarize(Color[][] image) {
        boolean[][] object = (boolean[][]) Array.newInstance(boolean.class, image.length, image[0].length);
        boolean[][] visited = (boolean[][]) Array.newInstance(boolean.class, image.length, image[0].length);
        Stack<Pixel> pixelStack = new Stack<>();
        pixelStack.push(new Pixel(0, 0, getPixelValue(image, 0, 0))); // add first pixel to stack

        while (!pixelStack.empty()) {
            Pixel p = pixelStack.pop();
            visited[p.x][p.y] = true;

            int value = getPixelValue(image, p.x, p.y);
            if (value >= p.referencedPixelValue - THRESHOLD && value <= p.referencedPixelValue + THRESHOLD) {
                object[p.x][p.y] = true;
            }

            pushNeighboursToStack(pixelStack, visited, p, (int) (0.9 * value + 0.1 * p.referencedPixelValue));
        }
        return object;
    }

    private static int getPixelValue(Color[][] image, int x, int y) {
        Color c = image[x][y];
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
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
}
