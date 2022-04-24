import java.util.ArrayList;
import java.util.Random;

public class FillingMissingDataUsingClassLabels {
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("Filling Missing Data Using Class Labels");
        System.out.println("=".repeat(80));

        int[] data = new int[10];

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            data[i] = random.nextInt(10, 50);
        }

        // Mark the missing values as Integer.MIN_VALUE
        data[random.nextInt(0, 10)] = Integer.MIN_VALUE;
        data[random.nextInt(0, 10)] = Integer.MIN_VALUE;

        System.out.println("Randomly Generated Data");
        System.out.println("-".repeat(80));
        System.out.println("Data\t\tClass Label");
        System.out.println("-".repeat(80));

        float meanA = 0;
        float meanB = 0;

        int countA = 0;
        int countB = 0;

        ArrayList<Integer> index = new ArrayList<>();

        for (int ele : data) {
            if (random.nextBoolean()) {
                countA++;

                if (ele != Integer.MIN_VALUE) {
                    meanA += ele;
                    System.out.println(ele + "\t\t\t\tA");
                } else {
                    index.add(0);
                    System.out.println("---\t\t\t\tA");
                }
            } else {
                countB++;

                if (ele != Integer.MIN_VALUE) {
                    meanB += ele;
                    System.out.println(ele + "\t\t\t\tB");
                } else {
                    index.add(1);
                    System.out.println("---\t\t\t\tB");
                }
            }
        }

        meanA /= countA;
        meanB /= countB;

        System.out.println("-".repeat(80));
        System.out.println("Mean of Class A: " + meanA);
        System.out.println("Mean of Class B: " + meanB);
        System.out.println("-".repeat(80));

        float first = index.get(0) == 0 ? meanA : meanB;
        float second = index.get(1) == 0 ? meanA : meanB;

        System.out.println("Missing Values: " + first + ", " + second);
        System.out.println("-".repeat(80));
    }
}
