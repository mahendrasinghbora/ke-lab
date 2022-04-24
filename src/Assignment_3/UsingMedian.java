package Assignment_3;

import java.util.Arrays;
import java.util.Scanner;

public class UsingMedian {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=".repeat(120));
        System.out.println("Filling in the Missing Values Using the Median");
        System.out.println("=".repeat(120));

        System.out.println("Enter 20 values.");
        System.out.println("-".repeat(120));

        double[] randomValues = new double[20];

        for (int i = 0; i < 20; i++) {
            randomValues[i] = sc.nextDouble();
        }

        System.out.println("-".repeat(120));
        System.out.print("Known Values: ");

        for (int i = 0; i < 20; i++) {
            System.out.printf("%.2f", randomValues[i]);

            if (i != 19) {
                System.out.print(", ");
            }
        }

        System.out.println();

        Arrays.sort(randomValues);
        double median =
                (randomValues[randomValues.length / 2] + randomValues[randomValues.length / 2 - 1]) / 2;

        System.out.println("-".repeat(120));
        System.out.printf("Median of the Known Values: %.2f\n",
                median);

        double[] missingValues = {
                median,
                median,
        };

        System.out.println("-".repeat(120));
        System.out.printf("Missing Values after Estimation: %.2f, %.2f\n",
                missingValues[0], missingValues[1]);
        System.out.println("-".repeat(120));
    }
}
