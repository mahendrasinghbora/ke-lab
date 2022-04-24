import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CreateInputFile {
    public static void main(String[] args) {
        System.out.println("-".repeat(60));
        System.out.println("Create Transaction Data File");
        System.out.println("-".repeat(60));

        Random random = new Random();

        // List to store the items available (8 items)
        ArrayList<String> items = new ArrayList<>(Arrays.asList("a ", "b ", "c ", "d ", "e ", "f ", "g ",
                "h "));
        // "a ", "b ", "c ", "d ", "e ", "f ", "g ", "h ", "i ", "j ", "k ",
        // "l ", "m ", "n ", "o ", "p ", "q ", "r ", "s ", "t ", "u ", "v ", "w ", "x ", "y ", "z "

        // Upper bound = 20, lower bound = 1
        int numberOfTransactions = random.nextInt(20 - 1 + 1) + 1;
        System.out.printf("Number of Transactions: %d\n", numberOfTransactions);

        System.out.println("-".repeat(60));
        String[] transactions = new String[numberOfTransactions];

        // Generate the transactions
        for (int i = 0; i < numberOfTransactions; i++) {
            // Upper bound = 5, lower bound = 1
            int numberOfItems = random.nextInt(5 - 1 + 1) + 1;

            // Shuffling items for randomness
            Collections.shuffle(items);
            transactions[i] = "";

            for (int j = 0; j < numberOfItems; j++) {
                transactions[i] += items.get(j);
            }
        }

        // Display the randomly generated transaction data
        for (int i = 0; i < numberOfTransactions; i++) {
            System.out.println("T" + (i + 1) + ": " + transactions[i]);
        }

        FileOutputStream fileOut = null;

        try {
            fileOut = new FileOutputStream("InputFile.txt");

            // Write data to the transaction data file
            for (int i = 0; i < numberOfTransactions; i++) {
                fileOut.write(("T" + (i + 1) + ": " + transactions[i] + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("-".repeat(60));
            System.out.println("Transaction data file generated.");
        }

        System.out.println("-".repeat(60));
    }
}
