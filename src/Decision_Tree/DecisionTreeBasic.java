package Decision_Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DecisionTreeBasic {
    public static void main(String[] args) {
        // Scanner sc = new Scanner(System.in);
        String age;
        String creditRating;
        String income;
        String isStudent;

        System.out.println("------------------------------------------------------------");
        System.out.println("Decision Tree");
        System.out.println("------------------------------------------------------------");

        BufferedReader reader = null;
        ArrayList<ArrayList<String>> dataSet = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader("dataSet.txt"));
            String input;

            while ((input = reader.readLine()) != null) {
                dataSet.add(new ArrayList<>(Arrays.asList(input.split(" "))));
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

        System.out.println("DataSet (AllElectronics)");
        System.out.println("------------------------------------------------------------");

        for (ArrayList<String> data : dataSet) {
            System.out.println(data);
        }

        System.out.println("------------------------------------------------------------");

        ArrayList<String> classLabels = new ArrayList<>();

        for (ArrayList<String> strings : dataSet) {
            age = strings.get(0);
            income = strings.get(1);
            isStudent =strings.get(2);
            creditRating = strings.get(3);

            if ("middle_aged".equals(age)) {
                classLabels.add("yes");
            } else if ("youth".equals(age)) {
                classLabels.add(isStudent);
            } else {
                if ("fair".equals(creditRating)) {
                    classLabels.add("yes");
                } else {
                    classLabels.add("no");
                }
            }
        }

        System.out.println("Data = Class label");
        System.out.println("------------------------------------------------------------");

        for (int i = 0; i < dataSet.size(); i++) {
            System.out.println(dataSet.get(i) + " = " + classLabels.get(i));
        }

        System.out.println("------------------------------------------------------------");
    }
}
