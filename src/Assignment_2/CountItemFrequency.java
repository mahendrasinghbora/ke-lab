package Assignment_2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class CountItemFrequency {
    public static void main(String[] args) {
        FileOutputStream fileOut;

        try (BufferedReader fileIn = new BufferedReader(new FileReader(
                "InputFile.txt"))) {
            String input;
            System.out.println("-".repeat(60));
            System.out.println("Frequency of Items in the Transactions");
            System.out.println("-".repeat(60));
            System.out.println("Item = Frequency");
            System.out.println("-".repeat(60));

            // Map to keep track of the count of items
            TreeMap<String, Integer> itemCount = new TreeMap<>();

            // Read transaction data from the input file
            while ((input = fileIn.readLine()) != null) {
                String[] transactionData = input.split(": ");
                String[] items = transactionData[1].split("\s+");

                for (String item : items) {
                    // Count the frequency of items
                    itemCount.merge(item, 1, Integer::sum);
                }
            }

            // print the count of the items
            itemCount.forEach((item, count) -> System.out.println(item + " = " + count));
            System.out.println("-".repeat(60));

            fileOut = new FileOutputStream("ItemCount.txt");

            // Write data to the item count file
            for (String item : itemCount.keySet()) {
                fileOut.write((item + " = " + itemCount.get(item) + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
