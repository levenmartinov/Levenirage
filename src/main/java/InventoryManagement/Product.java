package InventoryManagement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Product {

    private String sku; //stock keeping unit
    public static int counter = loadCounter(); //sku counter
    private static final String COUNTER_FILE = "counter.txt"; //sku safe file

    private String productName;
    private String supplierName;
    private int quantity;
    private String unit;
    private String shelf;

    public Product(String sku, String productName, String supplierName, int quantity, String unit, String shelf) {
        this.sku = sku;
        this.productName = productName;
        this.supplierName = supplierName;
        this.quantity = quantity;
        this.unit = unit;
        this.shelf = shelf;
    }

    //Static method that loads counter value from file
    public static int loadCounter() {
        try {
            if (Files.exists(Paths.get(COUNTER_FILE))) {
                BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE));
                String line = reader.readLine();
                reader.close();

                if (line != null && !line.trim().isEmpty()) {
                    return Integer.parseInt(line.trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 10000; // Varsayılan başlangıç değeri
    }


    public static void saveCounter(int counter) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(String.valueOf(counter));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

}
