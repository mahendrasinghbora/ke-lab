package Assignment_3;

import java.util.Scanner;

public class UsingMean {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=".repeat(120));
        System.out.println("Filling in the Missing Values Using the Mean");
        System.out.println("=".repeat(120));

        System.out.println("Enter 20 values.");
        System.out.println("-".repeat(120));

        double[] randomValues = new double[20];
        double sum = 0;

        for (int i = 0; i < 20; i++) {
            randomValues[i] = sc.nextDouble();
            sum += randomValues[i];
        }

        double mean = sum / 20;

        System.out.println("-".repeat(120));
        System.out.print("Known Values: ");

        for (int i = 0; i < 20; i++) {
            System.out.printf("%.2f", randomValues[i]);

            if (i != 19) {
                System.out.print(", ");
            }
        }

        System.out.println();

        System.out.println("-".repeat(120));
        System.out.printf("Mean of the Known Values (%.2f / 20): %.2f\n",
                sum, mean);

        double[] missingValues = {
                mean,
                mean,
        };

        System.out.println("-".repeat(120));
        System.out.printf("Missing Values after Estimation: %.2f, %.2f\n",
                missingValues[0], missingValues[1]);
        System.out.println("-".repeat(120));
    }
}
