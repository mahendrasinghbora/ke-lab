package Assignment_1;

/*
T1: a b c e
T2: b d f
T3: a c d f
T4: d f
T5: c d e
*/

/*
T1: a b
T2: b d
T3: x y
T4: a c e
T5: b c d
T6: x a z
T7: d b a
T8: c e d
T9: a b x e
T10: z x a
*/

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class CreateInputFile {
    public static void main(String[] args) {
        System.out.println("-".repeat(60));
        System.out.println("Create Transaction Data File");
        System.out.println("-".repeat(60));
        System.out.print("Number of Transactions: ");

        Scanner sc = new Scanner(System.in);
        int numberOfTransactions = sc.nextInt();
        sc.nextLine(); // To clear the buffer

        System.out.println("-".repeat(60));
        String[] transactions = new String[numberOfTransactions];

        // Read the transactions
        for (int i = 0; i < numberOfTransactions; i++) {
            System.out.printf("T%d: ", i + 1);
            transactions[i] = sc.nextLine();
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
