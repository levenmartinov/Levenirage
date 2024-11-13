package InventoryManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private int shelf;

    public Product(String sku, String productName, String supplierName, int quantity, String unit, int shelf) {
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
                return Integer.parseInt(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 10000;

    }



}
