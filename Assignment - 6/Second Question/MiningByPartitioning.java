import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MiningByPartitioning {
    static FileOutputStream fileOut;
    static ArrayList<ArrayList<String>> transactions = new ArrayList<>();
    static int numberOfTransactions;
    static ArrayList<String> items = new ArrayList<>();
    static HashMap<Integer, ArrayList<ArrayList<String>>> partitions =
            new HashMap<>();
    static ArrayList<ArrayList<String>> localFrequentItemSets =
            new ArrayList<>();
    static int minimumSupportCount;
    static int partitionSize; // Number of transactions a partition can hold
    static int numberOfPartitions;
    // List of support count of each item set
    static ArrayList<Integer> supportCountList = new ArrayList<>();
    static HashMap<ArrayList<String>, Integer> itemSupportCount =
            new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=".repeat(100));
        System.out.println("Mining by Partitioning");
        System.out.println("=".repeat(100));
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
        System.out.print("Partition Size: ");
        partitionSize = Integer.parseInt(sc.nextLine());

        System.out.println("-".repeat(100));

        numberOfPartitions =
                (int) Math.ceil((double) numberOfTransactions / partitionSize);
        System.out.println("Number of Partitions: " + numberOfPartitions);
        System.out.println("-".repeat(100));

        // Create partitions
        for (int i = 0; i < numberOfPartitions; i++) {
            ArrayList<ArrayList<String>> contents = new ArrayList<>(transactions.subList(i, i + partitionSize));
            partitions.put(i, contents);
            System.out.println("Partition #" + (i + 1) + ": " + contents);
            System.out.println("-".repeat(100));
        }

        System.out.println("First Scan");
        System.out.println("-".repeat(100));

        // First scan
        partitions.forEach((key, partitionDatabase) -> {
            int minimumSupportCountPartition = (int)
                    Math.ceil((double) minimumSupportCount / numberOfTransactions * partitionDatabase.size());

            ArrayList<ArrayList<String>> itemSets = new ArrayList<>();

            for (ArrayList<String> item : partitionDatabase) {
                int k = item.size();
                while (k > 0) {
                    ArrayList<ArrayList<String>> items = getItemSets(item, k);
                    itemSets.addAll(items);
                    k--;
                }
            }

            getSupportCount(itemSets);

            System.out.println("Partition #" + (key + 1));
            System.out.println("-".repeat(100));
            System.out.println("Minimum support count of the partition: " + minimumSupportCountPartition);
            System.out.println("-".repeat(100));

            itemSupportCount.forEach((itemSet, supportCount) -> System.out.println(itemSet + " = " + supportCount));

            System.out.println("-".repeat(100));

            // Out of all the item sets, get the item sets with
            // support count greater than or equal to the minimumSupportCount
            ArrayList<ArrayList<String>> itemSetsWithMinimumSupportCount =
                    getItemSetsWithMinimumSupportCount(itemSets, supportCountList, minimumSupportCountPartition);

            System.out.println("Local frequent item sets");
            System.out.println("-".repeat(100));

            for (ArrayList<String> itemSet : itemSetsWithMinimumSupportCount) {
                System.out.println(itemSet + " = " + itemSupportCount.get(itemSet));
            }

            System.out.println("-".repeat(100));

            localFrequentItemSets.addAll(itemSetsWithMinimumSupportCount);
        });

        Set<ArrayList<String>> uniqueLocalFrequentItemSets =
                new LinkedHashSet<>(localFrequentItemSets);

        localFrequentItemSets.clear();
        localFrequentItemSets.addAll(uniqueLocalFrequentItemSets);

        getSupportCount(localFrequentItemSets);

        System.out.println("Second Scan");
        System.out.println("-".repeat(100));
        System.out.println("Local frequent item sets");
        System.out.println("-".repeat(100));

        localFrequentItemSets.forEach((itemSet) -> System.out.println(itemSet + " = " + itemSupportCount.get(itemSet)));

        System.out.println("-".repeat(100));
        System.out.println("Minimum support count: " + minimumSupportCount);
        System.out.println("-".repeat(100));

        // Second scan
        ArrayList<ArrayList<String>> globalFrequentItemSets =
                getItemSetsWithMinimumSupportCount(localFrequentItemSets, supportCountList,
                        minimumSupportCount);

        Set<ArrayList<String>> uniqueGlobalFrequentItemSets =
                new LinkedHashSet<>(globalFrequentItemSets);

        globalFrequentItemSets.clear();
        globalFrequentItemSets.addAll(uniqueGlobalFrequentItemSets);

        System.out.println("Global frequent item sets");
        System.out.println("-".repeat(100));

        try {
            fileOut = new FileOutputStream("OutputFile.txt");
            fileOut.write("-".repeat(100).getBytes());
            fileOut.write("\n".getBytes());
            fileOut.write("Global frequent item sets".getBytes());
            fileOut.write("\n".getBytes());
            fileOut.write("-".repeat(100).getBytes());
            fileOut.write("\n".getBytes());

            for (ArrayList<String> itemSet : globalFrequentItemSets) {
                fileOut.write((itemSet.toString() + " = " + itemSupportCount.get(itemSet)).getBytes());
                fileOut.write("\n".getBytes());
            }

            fileOut.write("-".repeat(100).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        globalFrequentItemSets.forEach((itemSet) -> System.out.println(itemSet + " = " + itemSupportCount.get(itemSet)));

        System.out.println("-".repeat(100));
    }

    private static void getSupportCount(ArrayList<ArrayList<String>> itemSets) {
        supportCountList.clear();
        itemSupportCount.clear();
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

        for (int i = 0; i < supportCount.size(); i++) {
            int c = supportCount.get(i);

            if (c >= minimumSupportCount) {
                toReturn.add(itemSets.get(i));
            }
        }

        return toReturn;
    }
}
