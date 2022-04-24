import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        FPTree tree = new FPTree();
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Frequent Pattern Growth Algorithm");
        System.out.println("----------------------------------------------------------------------");

        List<LinkedList<String>> records = tree.readTransactionFile("in.txt", " ");

        tree.FPGrowth(records, null, 2);

        System.out.println("Frequent Item Sets");
        System.out.println("----------------------------------------------------------------------");

        tree.showFrequentSets();

        System.out.println("----------------------------------------------------------------------");
    }
}
