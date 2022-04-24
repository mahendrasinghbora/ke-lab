package Assignment_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadTransactions {
    public static void main(String[] args) {
        try (BufferedReader fileIn = new BufferedReader(new FileReader(
                "InputFile.txt"))) {
            String input;
            System.out.println("-".repeat(60));
            System.out.println("Transaction Data from the Input File");
            System.out.println("-".repeat(60));

            // Read transaction data from the input file
            while ((input = fileIn.readLine()) != null) {
                // Print the data to the console
                System.out.println(input);
            }

            System.out.println("-".repeat(60));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
