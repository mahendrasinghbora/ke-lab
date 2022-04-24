package Assignment_3;

import java.util.Arrays;
import java.util.Random;

public class MinMaxNormalization {
    public static void main(String[] args) {
        System.out.println("=".repeat(120));
        System.out.println("Min - Max Normalization");
        System.out.println("=".repeat(120));

        int[] firstColumn = new int[20];
        int[] secondColumn = new int[20];

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            firstColumn[i] = random.nextInt(1, 50);
            secondColumn[i] = random.nextInt(1, 20);
        }

        System.out.println("Randomly Generated Data");
        System.out.println("-".repeat(120));

        System.out.printf("%s\t%s\n", "First Column", "Second Column");
        System.out.println("-".repeat(120));

        for (int i = 0; i < 20; i++) {
            System.out.printf("%d\t\t\t\t\t%d\n", firstColumn[i], secondColumn[i]);
        }

        float newMin = 0;
        float newMax = 1;

        System.out.println("-".repeat(120));
        System.out.println("New Minimum: " + newMin);
        System.out.println("New Maximum: " + newMax);
        System.out.println("-".repeat(120));
        System.out.println("Normalized Data");
        System.out.println("-".repeat(120));
        System.out.printf("%s\t%s\n", "First Column", "Second Column");
        System.out.println("-".repeat(120));

        float[] normalizedFirstColumn = normalize(firstColumn, newMin, newMax);
        float[] normalizedSecondColumn = normalize(secondColumn, newMin, newMax);

        for (int i = 0; i < 20; i++) {
            System.out.printf("%.2f\t\t\t\t%.2f\n", normalizedFirstColumn[i], normalizedSecondColumn[i]);
        }

        System.out.println("-".repeat(120));
    }


    private static float[] normalize(int[] data, float newMin, float newMax) {
        float[] normalizedData = new float[data.length];

        int[] temp = new int[data.length];

        System.arraycopy(data, 0, temp, 0, data.length);
        Arrays.sort(temp);

        for (int i = 0; i < data.length; i++) {
            float max = temp[data.length - 1];
            float min = temp[0];

            float z = ((data[i] - min) / (max - min) * (newMax - newMin)) + newMin;

            normalizedData[i] = z;
        }

        return normalizedData;
    }
}
