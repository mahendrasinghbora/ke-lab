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
