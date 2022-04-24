package Assignment_2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class ExtractItemTransactionList {
    public static void main(String[] args) {
        FileOutputStream fileOut;

        try (BufferedReader fileIn = new BufferedReader(new FileReader(
                "InputFile.txt"))) {
            String input;
            System.out.println("-".repeat(60));
            System.out.println("Extract Item Transaction List");
            System.out.println("-".repeat(60));
            System.out.println("Item = Transactions");
            System.out.println("-".repeat(60));

            // Map to keep maintain the item - transaction list
            TreeMap<String, String> transactionList = new TreeMap<>();

            // Read transaction data from the input file
            while ((input = fileIn.readLine()) != null) {
                String[] transactionData = input.split(": ");
                String[] items = transactionData[1].split("\s+");

                for (String item : items) {
                    // Associate transactions with the respective items
                    transactionList.merge(item, transactionData[0] + " ",
                            String::concat);
                }
            }

            // print the item - transaction list
            transactionList.forEach((item, transactions) -> System.out.println(item + " = " + transactions));
            System.out.println("-".repeat(60));

            fileOut = new FileOutputStream("ItemTransactionList.txt");

            // Write data to the item - transaction list file
            for (String item : transactionList.keySet()) {
                fileOut.write((item + " = " + transactionList.get(item) + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
