package Assignment_3;

import java.util.Arrays;
import java.util.Random;

public class SmootheningByBinBoundaries {
    public static void main(String[] args) {
        System.out.println("=".repeat(140));
        System.out.println("Smoothing By Bin Boundaries");
        System.out.println("=".repeat(140));
        int[] textBookData = {4, 8, 15, 21, 21, 24, 25, 28, 34};

        System.out.println("Text Book Data: " + Arrays.toString(textBookData));

        int binFrequency = 3;

        generateBins(textBookData, binFrequency);

        Random random = new Random();

        int[] randomData = new int[20];

        // Generate random data
        for (int i = 0; i < randomData.length; i++) {
            randomData[i] = random.nextInt(50);
        }

        // Sort the data
        Arrays.sort(randomData);

        System.out.println("Random Data: " + Arrays.toString(randomData));

        binFrequency = 4;

        generateBins(randomData, binFrequency);
    }


    private static void generateBins(int[] data, int binFrequency) {
        System.out.println("Bin Frequency: " + binFrequency);
        System.out.println("Number of Bins: " + (data.length / binFrequency));
        System.out.println("-".repeat(140));

        int[][] bins = new int[data.length / binFrequency][binFrequency];

        for (int i = 0; i < data.length / binFrequency; i++) {
            int lowerBound = data[i * binFrequency];
            int upperBound = data[(i + 1) * binFrequency - 1];

            System.out.println("Bin" + (i + 1) + " Boundaries: [" + lowerBound + ", " + upperBound + "]");

            for (int j = 0; j < binFrequency; j++) {
                bins[i][j] =
                        Math.abs(data[i * binFrequency + j] - upperBound) >
                                Math.abs(data[i * binFrequency + j] - lowerBound)
                                ? lowerBound : upperBound;
            }
        }

        System.out.println("-".repeat(140));

        for (int i = 0; i < data.length / binFrequency; i++) {
            System.out.println("Bin " + (i + 1) + ": " + Arrays.toString(bins[i]));
        }

        System.out.println("-".repeat(140));
    }


}
