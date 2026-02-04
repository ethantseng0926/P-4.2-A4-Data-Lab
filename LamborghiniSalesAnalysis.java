import java.util.*;
import java.io.*;

/**
 * Class to store individual sale records from the CSV.
 */
class LamborghiniSale {
    String model;
    int year;
    String color;
    int salesVolume;

    public LamborghiniSale(String model, int year, String color, int salesVolume) {
        this.model = model;
        this.year = year;
        this.color = color;
        this.salesVolume = salesVolume;
    }
}

/**
 * Class to aggregate sales data for a specific model.
 */
class LamborghiniModel {
    String name;
    int totalSales = 0;
    
    // Maps to track sales per year and per color
    Map<Integer, Integer> yearSales = new HashMap<>();
    Map<String, Integer> colorSales = new HashMap<>();

    public LamborghiniModel(String name) {
        this.name = name;
    }

    public void addSale(int year, String color, int volume) {
        this.totalSales += volume;
        yearSales.put(year, yearSales.getOrDefault(year, 0) + volume);
        colorSales.put(color, colorSales.getOrDefault(color, 0) + volume);
    }

    public String getBestYear() {
        return Collections.max(yearSales.entrySet(), Map.Entry.comparingByValue()).getKey().toString();
    }

    public String getBestColor() {
        return Collections.max(colorSales.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}

public class LamborghiniSalesAnalysis {
    public static void main(String[] args) {
        String csvFile = "lamborghini_sales_2020_2025.csv";
        ArrayList<LamborghiniSale> allSales = new ArrayList<>();

        // 1. Load data into an ArrayList of objects
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] v = line.split(",");
                if (v.length >= 8) {
                    allSales.add(new LamborghiniSale(v[0], Integer.parseInt(v[1]), v[3], Integer.parseInt(v[7])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: CSV file not found.");
            return;
        }

        // 2. Aggregate data using a Map of Model objects
        Map<String, LamborghiniModel> report = new HashMap<>();
        for (LamborghiniSale sale : allSales) {
            report.putIfAbsent(sale.model, new LamborghiniModel(sale.model));
            report.get(sale.model).addSale(sale.year, sale.color, sale.salesVolume);
        }

        // 3. Sort models by total volume to find the Top 3
        List<LamborghiniModel> sortedList = new ArrayList<>(report.values());
        sortedList.sort((a, b) -> b.totalSales - a.totalSales);

        // 4. Output Results
        System.out.println("TOP 3 BEST-SELLING LAMBORGHINI MODELS (2020-2025)");
        System.out.println("=================================================");
        for (int i = 0; i < 3 && i < sortedList.size(); i++) {
            LamborghiniModel m = sortedList.get(i);
            System.out.println("RANK " + (i + 1) + ": " + m.name.toUpperCase());
            System.out.println("   Total Units Sold: " + String.format("%, d", m.totalSales));
            System.out.println("   Most Successful Year: " + m.getBestYear());
            System.out.println("   Most Popular Color:   " + m.getBestColor());
            System.out.println("-------------------------------------------------");
        }
    }
}