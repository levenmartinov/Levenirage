

package InventoryManagement;

import java.util.Scanner;

public class Start {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        ProductService productService = new ProductService(); // Düzeltilmiş başlatma
        mainMenu(productService);
    }

    public static void mainMenu(ProductService productService) {

        int select = 0;
        do {
            System.out.println("\n========= Depo Management System =========\n");
            System.out.println("1- Create a product");
            System.out.println("2- List products");
            System.out.println("3- Update product");
            System.out.println("4- Search product");
            System.out.println("5- Remove a product");
            System.out.println("6- Clear all products");
            System.out.println("0- EXIT");
            System.out.print("\nSelect an Option: ");


            try {
                select = Integer.parseInt(sc.nextLine().trim());
                if (select < 0 || select > 6) {
                    System.out.println("Invalid selection, please enter a number between 0 and 6.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (select) {
                case 1 -> {
                    System.out.println("Creating a new product...");
                    productService.addProduct(productService.products);
                }

                case 2 -> {
                    System.out.println("Listing all products...");
                    productService.listProduct(productService.products);
                }
                case 3 -> {
                    System.out.println("Updating a product...");
                    productService.updateProduct(productService.products);
                }
                case 4 -> {
                    System.out.println("Searching for a product...");
                    productService.searchProduct(productService.products);
                }
                case 5 -> {
                    System.out.println("Removing a product...");
                    productService.removeProduct(productService.products);
                }
                case 6 -> {
                    System.out.println("Clearing all products...");
                    productService.clearProducts(productService.products);
                }
                case 0 -> System.out.println("Exiting the system...");
            }

            if (select != 0) {
                promptReturnToMenu();
            }
        } while (select != 0);
        sc.nextLine();
    }

    private static void promptReturnToMenu() {
        System.out.println("Press Enter to return to the main menu...");
        sc.nextLine();// Kullanıcıdan Enter bekle
        //System.out.println("Returning to the main menu...");

    }
}

