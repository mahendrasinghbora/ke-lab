package Assignment_2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ExtractFrequentItemSets {
    // Function to find all subsets of a given set
    public static List<ArrayList<String>> subsets(String[] items) {
        List<ArrayList<String>> list = new ArrayList<>();
        Arrays.sort(items);
        backtrack(list, new ArrayList<>(), items, 0);
        return list;
    }

    // Helper function for the subsets() function
    private static void backtrack(List<ArrayList<String>> list,
                                  ArrayList<String> tempList, String[] items,
                                  int start) {
        list.add(new ArrayList<>(tempList));
        for (int i = start; i < items.length; i++) {
            tempList.add(items[i]);
            backtrack(list, tempList, items, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    public static void main(String[] args) {
        FileOutputStream fileOut;

        try (BufferedReader fileIn = new BufferedReader(new FileReader(
                "InputFile.txt"))) {
            String input;
            System.out.println("-".repeat(60));
            System.out.println("Extract Frequent Item Sets");
            System.out.println("-".repeat(60));

            int numberOfTransactions = 0;

            // Map to keep track of the frequency of various item sets
            HashMap<HashSet<String>, Integer> itemSetFrequency =
                    new HashMap<>();
            HashSet<String> itemList = new HashSet<>();
            ArrayList<HashSet<String>> itemsInTransactions = new ArrayList<>();

            // Read transaction data from the input file
            while ((input = fileIn.readLine()) != null) {
                String[] transactionData = input.split(": ");
                String[] items = transactionData[1].split("\s+");

                // Add items to the list of items (set)
                itemList.addAll(Arrays.asList(items));

                // Add items of the transaction to a list
                itemsInTransactions.add(new HashSet<>(Arrays.asList(items)));

                numberOfTransactions++;
            }

            String[] itemListArray = new String[itemList.size()];
            itemList.toArray(itemListArray);

            for (ArrayList<String> set : subsets(itemListArray)) {
                // Check if the set is empty (if yes, then ignore it)
                if (set.isEmpty()) {
                    continue;
                }

                // Add the set to the map
                itemSetFrequency.put(new HashSet<>(set), 0);
            }

            // Calculate the support count of all the item sets
            for (HashSet<String> itemSet : itemsInTransactions) {
                for (HashSet<String> key : itemSetFrequency.keySet()) {
                    if (itemSet.containsAll(key)) {
                        itemSetFrequency.put(key,
                                itemSetFrequency.get(key) + 1);
                    }
                }
            }

            // Minimum Support (Threshold)
            int minimumSupport = 2;

            System.out.println("Items = " + itemList);
            System.out.println("Number of Transactions = " + numberOfTransactions);
            System.out.println("Minimum Support Count = 2");
            System.out.println("-".repeat(60));
            System.out.println("Frequent Item Set = Support Count");
            System.out.println("-".repeat(60));

            // Display the frequent item sets and their support count
            for (HashSet<String> itemSet : itemSetFrequency.keySet()) {
                if (itemSetFrequency.get(itemSet) >= minimumSupport) {
                    System.out.println(itemSet + " = " + itemSetFrequency.get(itemSet));
                }
            }

            System.out.println("-".repeat(60));

            fileOut = new FileOutputStream("FrequentItemSets.txt");

            // Write data to the frequent item sets file
            for (HashSet<String> itemSet : itemSetFrequency.keySet()) {
                if (itemSetFrequency.get(itemSet) >= minimumSupport) {
                    fileOut.write((itemSet + " = " + itemSetFrequency.get(itemSet) + "\n").getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
