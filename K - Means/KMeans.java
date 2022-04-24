import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class KMeans {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BufferedReader reader = null;
        ArrayList<ArrayList<Double>> dataSet = new ArrayList<>();

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String input;

            while ((input = reader.readLine()) != null) {
                ArrayList<Double> entry = new ArrayList<>();

                double first = Double.parseDouble(input.split("\s+")[0]);
                double second = Double.parseDouble(input.split("\s+")[1]);
                entry.add(first);
                entry.add(second);
                dataSet.add(entry);

                minX = Math.min(first, minX);
                maxX = Math.max(first, maxX);
                minY = Math.min(second, minY);
                maxY = Math.max(second, maxY);
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

        System.out.println("=".repeat(120));
        System.out.println("K - Means Clustering Algorithm");
        System.out.println("=".repeat(120));

        System.out.println("Dataset");
        System.out.println("-".repeat(120));

        // Print the data set
        for (ArrayList<Double> data : dataSet) {
            System.out.println(data);
        }

        System.out.println("-".repeat(120));
        System.out.print("Number of clusters (k): ");
        int k = Integer.parseInt(sc.nextLine());
        System.out.println("-".repeat(120));

        ArrayList<ArrayList<Double>> centroids = new ArrayList<>();
        Random random = new Random();

        // Calculate k centroids
        for (int i = 0; i < k; i++) {
            Double x = Double.parseDouble(String.format("%.2f",
                    minX + (maxX - minX) * random.nextDouble()));
            Double y = Double.parseDouble(String.format("%.2f",
                    minY + (maxY - minY) * random.nextDouble()));
            ArrayList<Double> centroid = new ArrayList<>();

            centroid.add(x);
            centroid.add(y);
            centroids.add(centroid);
        }

        System.out.println("Random Centroids: " + centroids);
        System.out.println("-".repeat(120));

        // Initial clusters
        TreeMap<Integer, ArrayList<ArrayList<Double>>> clusters = clustering(k, dataSet, centroids);

        System.out.println("Initial Clusters");
        System.out.println("-".repeat(120));

        // Print the initial clusters
        clusters.forEach((clusterNumber, clusterData) -> System.out.println((clusterNumber + 1) + " = " + clusterData));

        System.out.println("-".repeat(120));

        TreeMap<Integer, ArrayList<ArrayList<Double>>> previousClusters = clusters;

        while (true) {
            centroids.clear();

            // Calculate new centroids
            for (int i = 0; i < k; i++) {
                double sumX = 0;
                double sumY = 0;

                ArrayList<ArrayList<Double>> clusterData = previousClusters.get(i);

                for (ArrayList<Double> data : clusterData) {
                    sumX += data.get(0);
                    sumY += data.get(1);
                }

                double meanX = Double.parseDouble(String.format("%.2f", sumX / clusterData.size()));
                double meanY = Double.parseDouble(String.format("%.2f", sumY / clusterData.size()));

                ArrayList<Double> centroid = new ArrayList<>();
                centroid.add(meanX);
                centroid.add(meanY);
                centroids.add(centroid);
            }

            System.out.println("New Centroids: " + centroids);
            System.out.println("-".repeat(120));

            TreeMap<Integer, ArrayList<ArrayList<Double>>> newClusters = clustering(k, dataSet, centroids);

            System.out.println("New Clusters");
            System.out.println("-".repeat(120));

            clusters.forEach((clusterNumber, clusterData) -> System.out.println((clusterNumber + 1) + " = " + clusterData));

            System.out.println("-".repeat(120));

            if (previousClusters.equals(newClusters)) {
                break;
            } else {
                previousClusters = newClusters;
            }
        }
    }


    public static TreeMap<Integer, ArrayList<ArrayList<Double>>> clustering
            (int k, ArrayList<ArrayList<Double>> dataSet, ArrayList<ArrayList<Double>> centroids) {
        TreeMap<Integer, ArrayList<ArrayList<Double>>> clusters = new TreeMap<>();

        for (ArrayList<Double> data : dataSet) {
            double minDistance = Double.MAX_VALUE;
            int index = Integer.MIN_VALUE;

            for (int i = 0; i < k; i++) {
                double distance = Math.sqrt(Math.pow((centroids.get(i).get(1) - data.get(1)), 2)
                        + Math.pow((centroids.get(i).get(0) - data.get(0)), 2));

                // System.out.println(centroids.get(i) + " : " + data + " : " + distance);

                if (distance < minDistance) {
                    minDistance = distance;
                    index = i;
                }
            }

            if (!clusters.containsKey(index)) {
                ArrayList<ArrayList<Double>> clusterData = new ArrayList<>();
                clusterData.add(data);

                clusters.put(index, clusterData);
            } else {
                clusters.get(index).add(data);
            }
        }

        return clusters;
    }
}
