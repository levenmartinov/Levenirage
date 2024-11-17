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

    //Validate and get a valid integer within a specific range
    public static Integer checkIntegerRange(String prompt, int min, int max) {

        Integer validInteger = null;

        while (validInteger == null) {
            System.out.println(prompt);

            String input = sc.nextLine();

            if (input.isEmpty()) return null; //Allow empty input for optional fields

            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    validInteger = value;
                }else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }

        }
        return validInteger;
    }

    // Format product output for consistent display
    public static String formatProduct(Product product) {
        return String.format("%-20s | %-20s | %-20s | %-10d | %-10s | %-10s",
                product.getSku(),
                product.getProductName(),
                product.getSupplierName(),
                product.getQuantity(),
                product.getUnit(),
                product.getShelf());
    }


    //
}