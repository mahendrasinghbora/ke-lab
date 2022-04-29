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

import org.w3c.dom.ls.LSOutput;

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

        System.out.println("=".repeat(80));
        System.out.println("Decision Tree");
        System.out.println("=".repeat(80));

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
        System.out.println("-".repeat(80));

        for (ArrayList<String> data : dataSet) {
            System.out.println(data);
        }

        System.out.println("-".repeat(80));
        System.out.println("Number of attributes: " + numberOfAttributes);
        System.out.println("-".repeat(80));
        System.out.println("Number of distinct class labels: " + numberOfClassLabels + " (" + classLabels + ")");
        System.out.println("-".repeat(80));

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

        classLabelCountMap.forEach((label, count) -> classLabelCount.add(count));

        System.out.println("Class label = count: " + classLabelCountMap);
        System.out.println("-".repeat(80));

        // Entropy of D [Info(D)]
        double expectedInformation = 0;

        for (int i = 0; i < numberOfClassLabels; i++) {
            double p = (double) classLabelCount.get(i) / numberOfTransactions;
            double temp = -(Math.log(p) / Math.log(2)) * p;
            expectedInformation += temp;
        }

        expectedInformation = Double.parseDouble(String.format("%.3f", expectedInformation));

        System.out.println("Expected Information (Entropy), Info(D) = " + expectedInformation + " bits");
        System.out.println("-".repeat(80));

        ArrayList<Integer> attributeValueCount = new ArrayList<>();
        ArrayList<HashSet<String>> attributeValues = new ArrayList<>();

        for (int i = 0; i < numberOfAttributes; i++) {
            attributeValues.add(new HashSet<>());
        }

        for (int i = 0; i < numberOfTransactions; i++) {
            for (int j = 0; j < numberOfAttributes; j++) {
                attributeValues.get(j).add(dataSet.get(i).get(j));
            }
        }

        for (int i = 0; i < numberOfAttributes; i++) {
            attributeValueCount.add(attributeValues.get(i).size());
            System.out.println("Attribute #" + (i + 1) + " = " + attributeValueCount.get(i) + " " + attributeValues.get(i));
        }

        System.out.println("-".repeat(80));

        ArrayList<ArrayList<Integer>> attributeValueSubCount = new ArrayList<>();

        for (HashSet<String> attributeValue : attributeValues) {
            ArrayList<Integer> valueCount = new ArrayList<>();

            for (String value : attributeValue.toArray(new String[0])) {
                int count = 0;

                for (ArrayList<String> data : dataSet) {
                    if (data.subList(0, numberOfAttributes).contains(value)) {
                        count++;
                    }
                }

                valueCount.add(count);
            }

            attributeValueSubCount.add(valueCount);
        }

        System.out.println("Attribute Value: Count");
        System.out.println("-".repeat(80));

        for (int i = 0; i < attributeValues.size(); i++) {
            Iterator<String> value = attributeValues.get(i).iterator();

            for (int j = 0; value.hasNext(); j++) {
                System.out.println(value.next() + ": " + attributeValueSubCount.get(i).get(j));
            }

            System.out.println("-".repeat(80));
        }

        ArrayList<ArrayList<Integer>> attributeValueSubsetLabelCount = new ArrayList<>();

        for (HashSet<String> attributeValue : attributeValues) {
            ArrayList<Integer> labelCount = new ArrayList<>();

            for (String value : attributeValue.toArray(new String[0])) {
                int count = 0;

                for (ArrayList<String> data : dataSet) {
                    if (data.subList(numberOfAttributes, numberOfAttributes + 1).contains("yes") &&
                            data.subList(0, numberOfAttributes).contains(value)) {
                        count++;
                    }
                }

                labelCount.add(count);
            }

            attributeValueSubsetLabelCount.add(labelCount);
        }

        for (int i = 0; i < attributeValues.size(); i++) {
            Iterator<String> value = attributeValues.get(i).iterator();

            for (int j = 0; value.hasNext(); j++) {
                int numberOfYes = attributeValueSubsetLabelCount.get(i).get(j);
                int numberOfNo = attributeValueSubCount.get(i).get(j) - numberOfYes;

                System.out.println(value.next() + " -> yes = " +
                        numberOfYes + ", no = " + numberOfNo);
            }

            System.out.println("-".repeat(80));
        }

        ArrayList<ArrayList<Double>> entropyOfAttributeValues = new ArrayList<>();

        for (int i = 0; i < attributeValues.size(); i++) {
            Iterator<String> value = attributeValues.get(i).iterator();
            ArrayList<Double> entropy = new ArrayList<>();

            for (int j = 0; value.hasNext(); j++) {
                int numberOfYes = attributeValueSubsetLabelCount.get(i).get(j);
                int numberOfNo = attributeValueSubCount.get(i).get(j) - numberOfYes;
                double weight = (double) attributeValueSubCount.get(i).get(j) / numberOfTransactions;
                double p1 = (double) numberOfYes / attributeValueSubCount.get(i).get(j);
                double p2 = (double) numberOfNo / attributeValueSubCount.get(i).get(j);
                double information = 0;

                double u = -(Math.log(p1) / Math.log(2));
                double v = -(Math.log(p2) / Math.log(2));

                if (p1 != 0 && p2 != 0) {
                    information = u * p1 + v * p2;
                } else if (p1 != 0) {
                    information = u * p1;
                } else {
                    information = v * p2;
                }

                information *= weight;
                information = Double.parseDouble(String.format("%.3f", information));

                entropy.add(information);

                System.out.println("Entropy(" + value.next() + ") = " +
                        information + " bits");
            }

            entropyOfAttributeValues.add(entropy);
            System.out.println("-".repeat(80));
        }

        ArrayList<Double> gainOfAttributes = new ArrayList<>();

        for (int i = 0; i < numberOfAttributes; i++) {
            double sumOfSubsetEntropy = 0;

            for (Double entropySubset : entropyOfAttributeValues.get(i)) {
                sumOfSubsetEntropy += entropySubset;
            }

            System.out.println("Entropy(attribute " + (i + 1) + ") = " +
                    String.format("%.3f", sumOfSubsetEntropy) + " bits");

            gainOfAttributes.add(expectedInformation - sumOfSubsetEntropy);
        }

        System.out.println("-".repeat(80));

        double maximumGain = Double.MIN_VALUE;
        int maximumGainAttribute = Integer.MIN_VALUE;

        for (int i = 0; i < numberOfAttributes; i++) {
            System.out.println("Gain(attribute " + (i + 1) + ") = " +
                    String.format("%.3f", gainOfAttributes.get(i)) + " bits");

            if (gainOfAttributes.get(i) > maximumGain) {
                maximumGain = gainOfAttributes.get(i);
                maximumGainAttribute = i;
            }
        }

        System.out.println("-".repeat(80));
        System.out.println("Attribute " + (maximumGainAttribute + 1) +
                " is selected as the splitting attribute.");
        System.out.println("-".repeat(80));

        ArrayList<ArrayList<String>> dataSetWithoutLabels = new ArrayList<>();

        for (ArrayList<String> data : dataSet) {
            ArrayList<String> noLabelData = new ArrayList<>(data.subList(0, numberOfAttributes));
            dataSetWithoutLabels.add(noLabelData);
        }

        String criteria = "";

        Iterator<String> values = attributeValues.get(maximumGainAttribute).iterator();
        int j = 0;

        while (j < attributeValues.get(maximumGainAttribute).size()) {
            if (attributeValueSubsetLabelCount.get(maximumGainAttribute).get(j) -
                    attributeValueSubCount.get(maximumGainAttribute).get(j) == 0) {
                criteria = values.next();
            } else {
                values.next();
            }
            j++;
        }

        System.out.println("Transaction -> Class Label");
        System.out.println("-".repeat(80));

        for (ArrayList<String> data : dataSetWithoutLabels) {
            String classLabel = "---";

            if (data.contains(criteria)) {
                classLabel = "YES";
            }

            System.out.println(data + " -> " + classLabel);
        }

        System.out.println("-".repeat(80));
    }
}
