import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AprioriAlgorithm {
    static int numberOfTransactions;
    static int minimumSupportCount;
    static int candidateSetCount;
    static HashMap<ArrayList<String>, Integer> itemSupportCount =
            new HashMap<>();
    static ArrayList<ArrayList<String>> frequentItemSets = new ArrayList<>();

    public static void main(String[] args) {
        FileOutputStream fileOut;
        Scanner sc = new Scanner(System.in);

        System.out.println("=".repeat(100));
        System.out.println("Apriori Algorithm");
        System.out.println("=".repeat(100));

        ArrayList<ArrayList<String>> transactions = new ArrayList<>();
        ArrayList<ArrayList<String>> previousItemSetsWithMinimumSupportCount = new ArrayList<>();

        try (BufferedReader fileIn = new BufferedReader(new FileReader(
                "InputFile.txt"))) {
            String input;

            // Read transaction data from the input file
            while ((input = fileIn.readLine()) != null) {
                ArrayList<String> transaction = new ArrayList<>();
                Collections.addAll(transaction, input.split("\s+"));
                transactions.add(transaction);
                numberOfTransactions++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Number of transactions: " + numberOfTransactions);
        System.out.println("-".repeat(100));
        System.out.println("Transactions");
        System.out.println("-".repeat(100));

        for (ArrayList<String> transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println("-".repeat(100));
        System.out.print("Minimum support count (ceil(minimumSupport * numberOfTransactions)): ");
        minimumSupportCount = Integer.parseInt(sc.nextLine());

        System.out.println("-".repeat(100));
        System.out.println("Item set = Support count");
        System.out.println("-".repeat(100));

        // Get all items
        ArrayList<String> items = getUniqueItems(transactions);

        int k = 0; // k is the number of elements in the item sets to consider

        while (true) {
            // Consider one more item than the last iteration
            k++;

            // List of support count of each item set
            ArrayList<Integer> supportCountList = new ArrayList<>();

            // Get permuted item sets with items. There will be k elements in
            // each item set
            ArrayList<ArrayList<String>> itemSets = getItemSets(items, k);

            // Calculate each item set's support count
            for (ArrayList<String> itemSet : itemSets) {
                int count = 0;

                for (ArrayList<String> transaction : transactions) {
                    if (existsInTransaction(itemSet, transaction)) {
                        count++;
                    }
                }
                supportCountList.add(count);
                itemSupportCount.put(itemSet, count);
            }

            // Out of all the item sets, get the item sets with
            // support count greater than or equal to the minimumSupportCount
            ArrayList<ArrayList<String>> itemSetsWithMinimumSupportCount = getItemSetsWithMinimumSupportCount(itemSets, supportCountList, minimumSupportCount);

            try {
                fileOut = new FileOutputStream("OutputFile.txt");

                fileOut.write("-".repeat(100).getBytes());
                fileOut.write("\nFrequent Item Sets\n".getBytes());
                fileOut.write("-".repeat(100).getBytes());
                fileOut.write("\n".getBytes());

                for (ArrayList<String> itemSet : frequentItemSets) {
                    fileOut.write((itemSet.toString() + "\n").getBytes());
                }

                fileOut.write("-".repeat(100).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            frequentItemSets.addAll(itemSetsWithMinimumSupportCount);

            // No itemSetsWithMinimumSupportCount exist
            if (itemSetsWithMinimumSupportCount.size() == 0) {
                if (k == candidateSetCount) {
                    System.out.println("-".repeat(100));
                }

                System.out.print("Most frequent item set(s): ");
                System.out.println(previousItemSetsWithMinimumSupportCount);
                System.out.println("-".repeat(100));
                System.out.println("Frequent Item Sets");
                System.out.println("-".repeat(100));

                // Print the frequent item sets
                frequentItemSets.forEach(System.out::println);
                System.out.println("-".repeat(100));

                break;
            }

            System.out.println("-".repeat(100));
            System.out.println(k + "-item sets with support count greater " +
                    "than the minimum support count (L" + k + ")");
            System.out.println("-".repeat(100));

            for (ArrayList<String> itemSet : itemSetsWithMinimumSupportCount) {
                System.out.println(itemSet + " = " + itemSupportCount.get(itemSet));
            }

            System.out.println("-".repeat(100));

            items = getUniqueItems(itemSetsWithMinimumSupportCount);
            previousItemSetsWithMinimumSupportCount = itemSetsWithMinimumSupportCount;
        }
    }

    // Returns the list of unique items from a list of transactions
    private static ArrayList<String> getUniqueItems(ArrayList<ArrayList<String>> data) {
        ArrayList<String> toReturn = new ArrayList<>();

        for (ArrayList<String> transaction : data) {
            for (String item : transaction) {
                if (!toReturn.contains(item)) {
                    toReturn.add(item);
                }
            }
        }

        Collections.sort(toReturn);
        return toReturn;
    }

    // Returns a list of item sets, where each item set has k number of items
    private static ArrayList<ArrayList<String>> getItemSets(ArrayList<String> items, int k) {
        if (k == 1) {
            // Return ArrayList of (ArrayList with one item)
            ArrayList<ArrayList<String>> toReturn = new ArrayList<>();

            for (String item : items) {
                ArrayList<String> itemList = new ArrayList<>();
                itemList.add(item);
                toReturn.add(itemList);
            }

            return toReturn;
        } else {
            int size = items.size();
            ArrayList<ArrayList<String>> toReturn = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                // Copy items to remainingItems
                ArrayList<String> remainingItems = new ArrayList<>(items);

                // Get item at ith position
                String thisItem = items.get(i);

                // Remove items upto i, inclusive
                remainingItems.subList(0, i + 1).clear();

                // Get permutations of the remaining items
                ArrayList<ArrayList<String>> permutationsBelow =
                        getItemSets(remainingItems, k - 1);

                // Add thisItem to each permutation and add the permutation to toReturn
                for (ArrayList<String> itemList : permutationsBelow) {
                    itemList.add(thisItem);
                    Collections.sort(itemList);
                    toReturn.add(itemList);
                }
            }

            return toReturn;
        }
    }

    // Checks if all items exist in a transaction
    private static boolean existsInTransaction(ArrayList<String> items, ArrayList<String> transaction) {
        for (String item : items) {
            if (!transaction.contains(item)) {
                return false;
            }
        }
        return true;
    }

    // Returns item sets with support count greater than or equal to the minimum
    // support count
    private static ArrayList<ArrayList<String>> getItemSetsWithMinimumSupportCount(
            ArrayList<ArrayList<String>> itemSets,
            ArrayList<Integer> supportCount,
            int minimumSupportCount) {
        ArrayList<ArrayList<String>> toReturn = new ArrayList<>();

        if (supportCount.size() > 0) {
            System.out.println("Candidate set - " + (candidateSetCount + 1)
                    + " (C" + (candidateSetCount + 1) + ")");
            candidateSetCount++;
            System.out.println("-".repeat(100));
        }

        // Print the current candidate set
        for (int i = 0; i < supportCount.size(); i++) {
            int c = supportCount.get(i);
            System.out.printf("%s = %d\n",
                    itemSets.get(i), c);

            if (c >= minimumSupportCount) {
                toReturn.add(itemSets.get(i));
            }
        }

        return toReturn;
    }
}
