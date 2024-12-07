package InventoryManagement;

import java.util.Map;
import java.util.Scanner;

public class Utils {

    static Scanner sc = new Scanner(System.in);

    //Check if products are available
    public static boolean isProductAvailable(Map<String, Product> products) {

        if (products == null || products.isEmpty()) {
            System.out.println("--No products available in the inventory.--");
            return false;
        }
        return true;
    }


    // Format product output for consistent display
    public static String formatProduct(Product product) {
        return String.format("%-20s %-20s %-20s %-15s %-10s %-10s%n",
                product.getSku(),
                product.getProductName(),
                product.getSupplierName(),
                product.getQuantity(),
                product.getUnit(),
                product.getShelf());
    }


    //
}