package pl.edu.pw.eiti.cpoo.processor;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Stack;

public class ImageProcessor {
    public static final int BYTE = 256;
    public static final int BACKGROUND_VALUE = 255;
    private static final int MAX_CORNER_NEIGHBOURS = 3;

    public static int[][] makeGrayScale(int[][] rgb) {
        int imageWidth = rgb.length;
        int imageHeight = rgb[0].length;
        int[][] image = (int[][]) Array.newInstance(int.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                Color c = new Color(rgb[i][j]);
                image[i][j] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
            }
        }
        return image;
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
            if (hCum[i] != 0) {
                minHCum = hCum[i];
                break;
            }
        }

        double factor = (double) (BYTE - 1) / (imageWidth * imageHeight - minHCum);
        int[] hEq = new int[BYTE]; // histogram equalized
        for (int i = 0; i < BYTE; i++) {
            hEq[i] = (int) ((hCum[i] - minHCum) * factor);
        }

        int[][] equalizedImage = (int[][]) Array.newInstance(int.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                equalizedImage[i][j] = hEq[image[i][j]];
            }
        }

        return equalizedImage;
    }

    public static boolean[][] binarize(int[][] image, int threshold) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] visited = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        boolean[][] object = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                object[i][j] = true;
            }
        }
        Stack<Point> pixelStack = new Stack<>();

        // starting points in all corners
        pixelStack.push(new Point(0, 0));
        pixelStack.push(new Point(imageWidth - 1, 0));
        pixelStack.push(new Point(0, imageHeight - 1));
        pixelStack.push(new Point(imageWidth - 1, imageHeight - 1));

        while (!pixelStack.empty()) {
            Point p = pixelStack.pop();
            visited[p.x][p.y] = true;

            int value = image[p.x][p.y];
            if (value >= BACKGROUND_VALUE - threshold && value <= BACKGROUND_VALUE + threshold) {
                // still background
                object[p.x][p.y] = false;
                pushNeighboursToStack(pixelStack, visited, p);
            }
        }
        return object;
    }

    private static void pushNeighboursToStack(Stack<Point> stack, boolean[][] visited, Point p) {
        pushToStack(stack, visited, p.x - 1, p.y);
        pushToStack(stack, visited, p.x + 1, p.y);
        pushToStack(stack, visited, p.x, p.y - 1);
        pushToStack(stack, visited, p.x, p.y + 1);
    }

    private static void pushToStack(Stack<Point> stack, boolean[][] visited, int x, int y) {
        if (x < 0 || x >= visited.length || y < 0 || y >= visited[0].length || visited[x][y]) {
            return;
        }
        stack.push(new Point(x, y));
    }

    public static boolean[][] median(boolean[][] image) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] result = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 1; i < imageWidth - 1; i++) {
            for (int j = 1; j < imageHeight - 1; j++) {
                result[i][j] = medianPixel(image, i, j);
            }
        }
        return result;
    }

    private static boolean medianPixel(boolean[][] image, int x, int y) {
        int objectPixels = 0;
        for (int i = -1; i <= 1; i++) {
            int xi = x + i;
            for (int j = -1; j <= 1; j++) {
                int yj = y + j;
                if (image[xi][yj]) {
                    objectPixels++;
                }
                if (objectPixels >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean[][] erode(boolean[][] image, int maxErosionSteps) {
        boolean[][] result = image;

        int erosionStep = 0;
        while (!stopErosion(erosionStep, maxErosionSteps)) {
            result = erodeOnce(result);
            erosionStep++;
        }

        return result;
    }

    private static boolean stopErosion(int erosionStep, int maxErosionSteps) {
        return erosionStep > maxErosionSteps;
    }

    private static boolean[][] erodeOnce(boolean[][] image) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] result = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 1; i < imageWidth - 1; i++) {
            for (int j = 1; j < imageHeight - 1; j++) {
                result[i][j] = erodePixel(image, i, j);
            }
        }
        return result;
    }

    private static boolean erodePixel(boolean[][] image, int x, int y) {
        for (int i = -1; i <= 1; i++) {
            int xi = x + i;
            for (int j = -1; j <= 1; j++) {
                int yj = y + j;
                if (image[xi][yj]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean[][] fillGaps(boolean[][] image) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] visited = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        boolean[][] object = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                object[i][j] = true;
            }
        }
        Stack<Point> pixelStack = new Stack<>();

        // starting points in all corners
        pixelStack.push(new Point(0, 0));
        pixelStack.push(new Point(imageWidth - 1, 0));
        pixelStack.push(new Point(0, imageHeight - 1));
        pixelStack.push(new Point(imageWidth - 1, imageHeight - 1));

        while (!pixelStack.empty()) {
            Point p = pixelStack.pop();
            visited[p.x][p.y] = true;

            if (!image[p.x][p.y]) {
                // still background
                object[p.x][p.y] = false;
                pushNeighboursToStack(pixelStack, visited, p);
            }
        }
        return object;
    }

    public static boolean[][] dilate(boolean[][] image, int maxDilationSteps) {
        boolean[][] result = image;

        int dilationStep = 0;
        while (!stopDilation(dilationStep, maxDilationSteps)) {
            result = dilateOnce(result);
            dilationStep++;
        }

        return result;
    }

    private static boolean stopDilation(int dilationStep, int maxDilationSteps) {
        return dilationStep > maxDilationSteps;
    }

    private static boolean[][] dilateOnce(boolean[][] image) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] result = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 1; i < imageWidth - 1; i++) {
            for (int j = 1; j < imageHeight - 1; j++) {
                result[i][j] = dilatePixel(image, i, j);
            }
        }
        return result;
    }

    private static boolean dilatePixel(boolean[][] image, int x, int y) {
        for (int i = -1; i <= 1; i++) {
            int xi = x + i;
            for (int j = -1; j <= 1; j++) {
                int yj = y + j;
                if (!image[xi][yj]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean[][] detectCorners(boolean[][] image) {
        int imageWidth = image.length;
        int imageHeight = image[0].length;
        boolean[][] result = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
        for (int i = 1; i < imageWidth - 1; i++) {
            for (int j = 1; j < imageHeight - 1; j++) {
                result[i][j] = detectCornerPixel(image, i, j);
            }
        }
        return result;
    }

    private static boolean detectCornerPixel(boolean[][] image, int x, int y) {
        if(!image[x][y]) {
            return false;
        }

        int neighbours = 0;
        for (int i = -1; i <= 1; i++) {
            int xi = x + i;
            for (int j = -1; j <= 1; j++) {
                int yj = y + j;
                if (image[xi][yj] && i + j != 0) {
                    neighbours++;
                }
            }
        }
        return neighbours >= 1 && neighbours <= MAX_CORNER_NEIGHBOURS;
    }
}
