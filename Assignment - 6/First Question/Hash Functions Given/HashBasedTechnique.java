import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HashBasedTechnique {
    static ArrayList<ArrayList<String>> transactions = new ArrayList<>();
    static int numberOfTransactions;
    static Map<String, Integer> itemOrder = new HashMap<>();
    static ArrayList<String> items = new ArrayList<>();
    static int minimumSupportCount;
    static FileOutputStream fileOut;

    public static void main(String[] args) {
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

            // Get all items
            items = getUniqueItems(transactions);

            // Set the item order
            for (int i = 0; i < items.size(); i++) {
                itemOrder.put(items.get(i), i + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ArrayList<String> strings : transactions) {
            items.addAll(strings);
        }

        System.out.println("=".repeat(80));
        System.out.println("Hash Based Technique for Improving the Efficiency" +
                " of Apriori");
        System.out.println("=".repeat(80));
        System.out.println("Transactions");
        System.out.println("-".repeat(80));

        for (ArrayList<String> transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println("-".repeat(80));
        System.out.println("Number of transactions: " + numberOfTransactions);
        System.out.println("-".repeat(80));
        System.out.print("Minimum support count: ");

        minimumSupportCount = Integer.parseInt(new Scanner(System.in).nextLine());

        System.out.println("-".repeat(80));
        System.out.println("Hash Function for 2 - Length Item Sets: (10 * x +" +
                " y) % 7");
        System.out.println("Hash Function for 3 - Length Item Sets: (10 * x - 5 * y + z) % " +
                "7");
        System.out.println("-".repeat(80));

        try {
            fileOut = new FileOutputStream("OutputFile.txt");
            fileOut.write("-".repeat(80).getBytes());
            fileOut.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        hashingItemSet(2);
        hashingItemSet(3);
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

    static void hashingItemSet(int k) {
        HashMap<ArrayList<String>, Integer> itemSets = new HashMap<>();

        getItemSets(getUniqueItems(transactions), k).forEach((itemSet) -> itemSets.put(itemSet, 0));

        // Calculate each item set's support count
        for (ArrayList<String> itemSet : itemSets.keySet()) {
            int count = 0;

            for (ArrayList<String> transaction : transactions) {
                if (existsInTransaction(itemSet, transaction)) {
                    count++;
                }
            }
            itemSets.put(itemSet, count);
        }

        HashMap<Integer, Integer> hashBinFrequency = new HashMap<>();
        HashMap<Integer, ArrayList<ArrayList<String>>> bucketContent = new HashMap<>();

        itemSets.forEach((itemSet, supportCount) -> {
            if (supportCount != 0) {
                Integer hashValue = getHash(itemSet, k);

                if (hashBinFrequency.containsKey(hashValue)) {
                    hashBinFrequency.put(hashValue,
                            (hashBinFrequency.get(hashValue) + supportCount));
                    bucketContent.get(hashValue).add(itemSet);
                    bucketContent.put(hashValue, bucketContent.get(hashValue)
                    );
                } else {
                    ArrayList<ArrayList<String>> newItemSet = new ArrayList<>();
                    newItemSet.add(itemSet);
                    hashBinFrequency.put(hashValue, supportCount);
                    bucketContent.put(hashValue, newItemSet);
                }
            }
        });

        System.out.println("Frequent item sets of size " + k);
        System.out.println("-".repeat(80));

        try {
            fileOut.write(("Frequent item sets of size " + k + "\n").getBytes());
            fileOut.write("-".repeat(80).getBytes());
            fileOut.write("\n".getBytes());

            bucketContent.forEach((key, value) -> {
                int hashValue = key;
                int bucketCount = hashBinFrequency.get(hashValue);

                if (bucketCount >= minimumSupportCount) {
                    System.out.println("Bucket hash value: " + hashValue + ", " +
                            "Bucket count: " + bucketCount);

                    try {
                        fileOut.write(("Bucket hash value: " + hashValue + ", " +
                                "Bucket count: " + bucketCount).getBytes());
                        fileOut.write("\n".getBytes());
                        fileOut.write("Item sets: ".getBytes());
                        fileOut.write(bucketContent.get(hashValue).toString().getBytes());
                        fileOut.write("\n".getBytes());
                        fileOut.write("-".repeat(80).getBytes());
                        fileOut.write("\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.print("Item sets: ");
                    System.out.println(bucketContent.get(hashValue));
                    System.out.println("-".repeat(80));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calculate hash value for the item set
    static int getHash(ArrayList<String> itemSet, int k) {
        if (k == 3) {
            int x = itemOrder.get(itemSet.get(0));
            int y = itemOrder.get(itemSet.get(1));
            int z = itemOrder.get(itemSet.get(2));

            return (10 * x - 5 * y + z) % 7;
        } else if (k == 2) {
            int x = itemOrder.get(itemSet.get(0));
            int y = itemOrder.get(itemSet.get(1));

            return (10 * x + y) % 7;
        }
        return -1;
    }
}
