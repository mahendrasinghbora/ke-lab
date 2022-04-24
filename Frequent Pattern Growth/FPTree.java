import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class FPTree {
    static int numberOfTransactions = 0;
    static List<LinkedList<String>> transactions = new LinkedList<>();
    static List<String> frequentSets = new LinkedList<>();

    public List<LinkedList<String>> readTransactionFile(String fileName, String separator) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String input;

            while ((input = reader.readLine()) != null) {
                transactions.add(new LinkedList<>(Arrays.asList(input.split(separator))));
                numberOfTransactions++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        System.out.println("Number of transactions: " + numberOfTransactions);
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Transactions");
        System.out.println("----------------------------------------------------------------------");

        // print the transactions
        for (LinkedList<String> transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println("----------------------------------------------------------------------");

        return transactions;
    }

    public List<TreeNode> buildHeaderTable(List<LinkedList<String>> records, int minSupport) {
        Map<String, Long> map = new HashMap<>();

        for (LinkedList<String> record : records) {
            for (String s : record) {
                if (map.containsKey(s)) {
                    map.put(s, map.get(s) + 1);
                } else {
                    map.put(s, 1L);
                }
            }
        }

        List<Entry<String, Long>> table = new LinkedList<>(map.entrySet());
        table.sort((o1, o2) -> o1.getValue() > o2.getValue() ? -1 : 1);

        List<TreeNode> result = new LinkedList<>();

        for (Entry<String, Long> entry : table) {
            if (entry.getValue() < minSupport) {
                break;
            }

            TreeNode node = new TreeNode();
            node.setName(entry.getKey());
            node.setCount(entry.getValue());
            result.add(node);
        }

        System.out.println("Header Table (Item = Support count)");
        System.out.println("----------------------------------------------------------------------");

        for (TreeNode node : result) {
            System.out.println(node);
        }

        System.out.println("----------------------------------------------------------------------");

        return result;

    }

    private void sortByFrequency(List<LinkedList<String>> records, List<TreeNode> headerTable) {

        final Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headerTable.size(); ++i) {
            map.put(headerTable.get(i).getName(), i);
        }

        for (LinkedList<String> record : records) {
            record.sort((o1, o2) -> {
                if (!map.containsKey(o1))
                    return 1;
                if (!map.containsKey(o2))
                    return -1;
                return map.get(o1) > map.get(o2) ? 1 : -1;
            });
        }

//        System.out.println("Transactions after sorting");
//        System.out.println("----------------------------------------------------------------------");
//
//        // print the transactions
//        for (LinkedList<String> transaction : records) {
//            System.out.println(transaction);
//        }
//
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Candidate Pattern Base = " + records);
    }

    private void insertNodes(TreeNode root, LinkedList<String> records, List<TreeNode> headerTable) {
        TreeNode subRoot = root;

        while (records.size() != 0) {
            TreeNode node = new TreeNode();
            node.setName(records.pop());
            node.increase();
            TreeNode lastNode = getLastNode(node.getName(), headerTable);

            if (lastNode == null) {
                records.poll();
                continue;
            }

            lastNode.setNext(node);
            node.setParent(subRoot);
            subRoot.addChild(node);
            subRoot = node;
        }
    }

    private TreeNode getLastNode(String name, List<TreeNode> headerTable) {
        TreeNode node = null;
        for (TreeNode treeNode : headerTable)
            if (treeNode.getName() != null && treeNode.getName().equals(name)) {
                node = treeNode;
                break;
            }
        if (node == null)
            return null;

        while (node.getNext() != null && node.getNext().getName() != null)
            node = node.getNext();
        return node;
    }

    private void traceTree(TreeNode root, int blanks) {
        for (int i = 0; i < blanks; ++i) {
            System.out.print("  --  ");
        }

        System.out.println("[" + root + "]");

        if (root.getChildren() != null) {
            for (TreeNode node : root.getChildren()) {
                traceTree(node, blanks + 1);
            }
        }
    }

    public TreeNode buildFPTree(List<LinkedList<String>> records, List<TreeNode> headerTable) {
        sortByFrequency(records, headerTable);
        TreeNode root = new TreeNode();
        TreeNode subRoot;
        TreeNode tmpNode;

        for (LinkedList<String> record : records) {
//            System.out.println("records for build tree=" + records);
            subRoot = root;
            while (record.size() > 0 && getLastNode(record.peek(), headerTable) != null //not frequency item
                    && (tmpNode = subRoot.findChild(record.peek())) != null) {

                tmpNode.increase();
                subRoot = tmpNode;
                record.poll();
            }
            insertNodes(subRoot, record, headerTable);
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.println("Conditional FP - Tree");
        System.out.println("----------------------------------------------------------------------");
        traceTree(root, 0);
        System.out.println("----------------------------------------------------------------------");

        return root;
    }

    private void combination(List<TreeNode> nodes, int i, String itemSet, List<String> suffix) {
        if (i == nodes.size()) {
            StringBuilder sb = new StringBuilder();
            sb.append(itemSet);
            for (String s : suffix) {
                sb.append(s).append(" ");
            }
            if (sb.toString().length() > 2) {
                frequentSets.add(sb.toString());
//                System.out.println(sb + " added");
            }
            return;
        }
        TreeNode node = nodes.get(i);
        combination(nodes, i + 1, itemSet + node.getName() + " ", suffix);
        combination(nodes, i + 1, itemSet, suffix);

    }


    public void FPGrowth(List<LinkedList<String>> records, List<String> pattern, int minSupport) {
        List<TreeNode> headerTable = buildHeaderTable(records, minSupport);

        System.out.println("Pattern = " + pattern + ", Header Table = " + headerTable);

        TreeNode root = buildFPTree(records, headerTable);

//        System.out.println("Record size: " + records.size());

        if (records.size() == 0 || records.size() == 2) {
            combination(headerTable, 0, "", pattern);
            return;
        }
        if (root.getChildren() == null || root.getChildren().size() == 0) {
            return;
        }

        for (int i = headerTable.size() - 1; i >= 0; i--) {
            TreeNode header = headerTable.get(i);
            TreeNode headerNode = header;
            List<LinkedList<String>> conditionalPatternBase = new LinkedList<>();

            while ((headerNode = headerNode.getNext()) != null) {
                TreeNode backNode = headerNode;
                LinkedList<String> preNodes = new LinkedList<>();

                while ((backNode = backNode.getParent()).getName() != null) {
                    preNodes.add(backNode.getName());
                }

                long count = headerNode.getCount();

                if (preNodes.size() != 0)
                    while (count-- > 0) {
                        conditionalPatternBase.add((LinkedList<String>) preNodes.clone());
                    }
            }
            LinkedList<String> postPattern = new LinkedList<>();
            postPattern.add(header.getName());
            if (pattern != null) {
                postPattern.addAll(pattern);
            }

            FPGrowth(conditionalPatternBase, postPattern, minSupport);
        }
    }

    public void showFrequentSets() {
        for (String s : frequentSets) {

            if (s.length() == 3) {
                continue;
            }

            System.out.println(s);
        }
    }

}
