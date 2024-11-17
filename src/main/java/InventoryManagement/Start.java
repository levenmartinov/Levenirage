package InventoryManagement;

import java.util.PriorityQueue;
import java.util.Scanner;

public class Start {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        ProductService productService = new ProductService(new PriorityQueue<>()); //Creating a product service
        mainMenu(productService);

    }
    public static void mainMenu(ProductService productService) {

        int select;

        do {
            System.out.println("\n========= Depo Management System =========");
            System.out.println("\n1- Create a product");
            System.out.println("2- List products");
            System.out.println("3- Enter a product");
            System.out.println("4- Place a product on the shelf");
            System.out.println("5- Product output");
            System.out.println("6- Remove a product");
            System.out.println("7- Clear all products");
            System.out.println("8- updateProduct");
            System.out.println("9- searchProduct");
            System.out.println("0- EXIT");
            System.out.print("\nSelect an Option: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number between 0 and 7.");
                sc.next(); //Clear if input is invalid
            }
            select = sc.nextInt();
            sc.nextLine(); //Cleans

            //Check the validity of the selection
            if (select < 0 || select > 9) {
                System.out.println("Invalid selection, please enter a number between 0 and 7.");
                continue; //Return to loop on invalid selection
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
                    System.out.println("Entering a product...");
                    productService.enterProduct(productService.products);
                }

                case 4 -> {
                    System.out.println("Placing a product on the shelf...");
                    productService.putProductOnShelf(productService.products);
                }

                case 5 -> {
                    System.out.println("Outputting a product...");
                    productService.productOutput(productService.products);
                }

                case 6 -> {
                    System.out.println("Removing a product...");
                    productService.removeProduct(productService.products);
                }

                case 7 -> {
                    System.out.println("Clearing all products...");
                    productService.clearProducts(productService.products);
                }

                case 8 -> {
                    System.out.println("updateProduct");
                    productService.updateProduct(productService.products);
                }

                case 9 -> {
                    System.out.println("searchProduct");
                    productService.searchProduct(productService.products);
                }

                case 0 -> System.out.println("Exiting the system...");

            }

            if (select != 0) {
                promptReturnToMenu();
            }
        } while (select != 0);
    }

    private static void promptReturnToMenu() {
        System.out.println("Press Enter to return to the main menu...");
        sc.nextLine(); //Wait for Enter to return to the main menu
    }
}
