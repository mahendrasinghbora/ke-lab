package Decision_Tree;

/*
youth high no fair no
youth high no excellent no
middle_aged high no fair yes
senior medium no fair yes
senior low yes fair yes
senior low yes excellent no
middle_aged low yes excellent yes
youth medium no fair no
youth low yes fair yes
senior medium yes fair yes
youth medium yes excellent yes
middle_aged medium no excellent yes
middle_aged high yes fair yes
senior medium no excellent no
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DecisionTree {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String age;
        String creditRating;
        String income;
        String isStudent;

        System.out.println("------------------------------------------------------------");
        System.out.println("Decision Tree");
        System.out.println("------------------------------------------------------------");

        BufferedReader reader = null;
        ArrayList<ArrayList<String>> dataSet = new ArrayList<>();
        int numberOfTransactions = 0;

        try {
            reader = new BufferedReader(new FileReader("dataSet.txt"));
            String input;

            while ((input = reader.readLine()) != null) {
                dataSet.add(new ArrayList<>(Arrays.asList(input.split(" "))));
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

        int numberOfAttributes = dataSet.get(0).size() - 1;

        // Find the class labels
        HashSet<String> classLabels = new HashSet<>();

        for (ArrayList<String> data : dataSet) {
            classLabels.add(data.get(numberOfAttributes));
        }

        int numberOfClassLabels = classLabels.size();

        System.out.println("DataSet (AllElectronics)");
        System.out.println("------------------------------------------------------------");

        for (ArrayList<String> data : dataSet) {
            System.out.println(data);
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Number of attributes: " + numberOfAttributes);
        System.out.println("------------------------------------------------------------");
        System.out.println("Number of distinct class labels: " + numberOfClassLabels + " (" + classLabels + ")");
        System.out.println("------------------------------------------------------------");

        HashMap<String, Integer> classLabelCountMap = new HashMap<>();

        for (ArrayList<String> data : dataSet) {
            String classLabel = data.get(numberOfAttributes);

            if (!classLabelCountMap.containsKey(classLabel)) {
                classLabelCountMap.put(classLabel, 1);
            } else {
                classLabelCountMap.put(classLabel, classLabelCountMap.get(classLabel) + 1);
            }
        }

        ArrayList<Integer> classLabelCount = new ArrayList<>();

        classLabelCountMap.forEach((label, count) -> {
            classLabelCount.add(count);
        });

        System.out.println("Class label = count: " + classLabelCountMap);
        System.out.println("------------------------------------------------------------");

        // Entropy of D [Info(D)]
        double expectedInformation = 0;

        for (int i = 0; i < numberOfClassLabels; i++) {
            double p = (double) classLabelCount.get(i) / numberOfTransactions;
            double temp = -(Math.log(p) / Math.log(2)) * p;
            expectedInformation += temp;
        }

        expectedInformation = Double.parseDouble(String.format("%.3f", expectedInformation));

        System.out.println("Expected Information (Entropy), Info(D) = " + expectedInformation);
        System.out.println("------------------------------------------------------------");
    }
}
