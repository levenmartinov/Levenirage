package InventoryManagement;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.*;

public class ProductService implements Management {

    Map<String, Product> products = new LinkedHashMap<>();

    Scanner sc = new Scanner(System.in);

    ProductSaveService saveService = new ProductSaveService();
    private PriorityQueue<Object> searchResults;

    public ProductService (PriorityQueue<Object> searchResults) {
        this.searchResults = searchResults;
        products = saveService.loadFromFile();
    }


    @Override
    public void addProduct(Map<String, Product> products) {

        Product pr = new Product(null, null, null, 0, null, " ");

        System.out.println("Enter a product name : ");
        String productName = sc.nextLine().toUpperCase().trim();
        System.out.println("Enter a supplier name : ");
        String supplierName = sc.nextLine().toUpperCase().trim();

        System.out.println("Enter a packaging unit : ");
        String unit = sc.nextLine().toUpperCase().trim();

        //Check if the same product is available
        for (Product w : products.values()) {
            if (w.getProductName().
                    equals(productName) && w.getSupplierName().
                    equals(supplierName) && w.getUnit().
                    equals(unit)) {

                System.out.println("This product already exists. You can update the quantity instead.");
                return;
            }
        }


        int productQuantity;
        do {
            System.out.println("Enter a quantity");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a numeric value for quantity.");
                sc.next(); //clear wrong input
            }
            productQuantity = sc.nextInt();
            sc.nextLine(); //hataa olbilirr daha sonra kotrol et ve bu yorumu sil
            if (productQuantity <= 0) {
                System.out.println("Quantity should be a positive number.");
            }
        } while (productQuantity <= 0);



        // Ürün özelliklerini ayarla ve SKU'yi oluştur
        pr.setProductName(productName);
        pr.setSupplierName(supplierName);
        pr.setQuantity(productQuantity);
        pr.setUnit(unit);
        productSku(pr);  // Ürün ID'sini ayarla
        products.put(pr.getSku(), pr); // Ürünü Map'e ekle

        // Counter dosyasına güncel değeri yaz
        Product.saveCounter(Product.counter);

        // Ürünü dosyaya kaydet
        saveService.saveToFile(this.products);

    }

    @Override
    public void productSku(Product pr) {

        try {
            // ID'yi ürün adı ve yıl bilgisiyle oluştur, counter'ı kullanarak benzersiz yap
            pr.setSku(pr.getProductName().toUpperCase().substring(0, 2) + LocalDate.now().getYear() + Product.counter);
            Product.counter++;
        } catch (StringIndexOutOfBoundsException e) {
            // Eğer ürün adı kısa ise "NULL" kullanarak ID oluştur
            pr.setSku("NULL" + LocalDate.now().getYear() + Product.counter);
            Product.counter++;
        }

    }

    @Override
    public void listProduct(Map<String, Product> products) {
        System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", "~~ SKU ~~", "PRODUCT NAME", "SUPPLIER NAME", "QUANTITY", "UNIT", "SHELF");
        System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", "---------", "------------", "--------------", "--------", "----", "------");
        for (Product product : products.values()) {
            System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", product.getSku(), product.getProductName(),
                    product.getSupplierName(), product.getQuantity(), product.getUnit(), product.getShelf());
        }
    }

    // Ürünün miktarını günceller
    public void enterProduct(Map<String, Product> products) {
        System.out.print("Enter the product ID to update quantity: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product != null) {
            int quantity;
            do {
                System.out.print("Enter the quantity to add: ");
                while (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a numeric value for quantity.");
                    sc.next();
                }
                quantity = sc.nextInt();
                sc.nextLine();
                if (quantity <= 0) {
                    System.out.println("Quantity should be a positive number.");
                }
            } while (quantity <= 0);
            // Miktarı güncelle
            product.setQuantity(product.getQuantity() + quantity);
            System.out.println("Product quantity updated successfully. NEW STOCK: " + product.getQuantity());
        } else {
            System.out.println("The ID you have entered is not on the list. Please check again.");
        }

        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(this.products);
    }


    public void putProductOnShelf(Map<String, Product> products) {
        System.out.print("Enter the product ID to place on the shelf: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product != null) {
            int shelfNo;
            boolean isShelfAvailable = true; // Başlangıç değeri atanıyor.

            do {
                System.out.print("Enter a positive shelf number: ");

                // Kullanıcıdan sadece int türünde giriş alınmasını sağlıyoruz.
                while (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a valid positive integer for the Shelf Number.");
                    sc.next(); // Geçersiz girişi atla.
                }
                shelfNo = sc.nextInt();
                sc.nextLine(); // Satır sonunu temizle.

                if (shelfNo < 0) {
                    System.out.println("Shelf number must be a positive integer. Please try again.");
                    continue; // Negatif değer girdiyse yeniden döngüye dön.
                }

                // Rafın dolu olup olmadığını kontrol et
                isShelfAvailable = true; // Kontrol başlangıcı için varsayılan değer.
                for (Product p : products.values()) {
                    if (p.getShelf() != null && p.getShelf().equals("SHELF" + shelfNo)) {
                        System.out.println("This shelf is already occupied. Try a different one.");
                        isShelfAvailable = false;
                        break;
                    }
                }

            } while (!isShelfAvailable);

            // Ürünü rafa yerleştir
            product.setShelf("SHELF" + shelfNo);
            System.out.println("Product placed on shelf " + product.getShelf() + " successfully.");
        } else {
            System.out.println("The ID you have entered is not on the list. Please check again.");
        }

        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(this.products);
    }


    // Ürün çıkışı yapar
    public void productOutput(Map<String, Product> products) {
        System.out.print("Enter the product ID for output: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product != null) {
            int quantity;
            do {
                System.out.print("Enter the quantity to remove: ");
                while (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a numeric value for quantity.");
                    sc.next();
                }
                quantity = sc.nextInt();
                sc.nextLine();

                // Mevcut stok miktarını kontrol et
                if (quantity > product.getQuantity()) {
                    System.out.println("Insufficient quantity in stock. MAXIMUM AVAILABLE: " + product.getQuantity());
                } else if (quantity <= 0) {
                    System.out.println("Quantity should be a positive number.");
                }
            } while (quantity <= 0 || quantity > product.getQuantity());

            // Miktarı azalt
            product.setQuantity(product.getQuantity() - quantity);
            System.out.println("Product output successful. REMAINING STOCK: " + product.getQuantity());
        } else {
            System.out.println("The ID you have entered is not on the list. Please check again.");
        }

        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(this.products);
    }

    // Ürünü listeden kaldırır
    public void removeProduct(Map<String, Product> products) {
        System.out.print("Enter the product ID to remove: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product != null) {
            // Ürünü sil
            products.remove(productSku);
            System.out.println("Product with ID " + productSku + " has been removed successfully.");
        } else {
            System.out.println("The ID you entered is not in the list. Please check again.");
        }

        // Güncellenen listeyi dosyaya kaydet
        saveService.saveToFile(this.products);
    }

    // Listeyi tamamen sıfırlar
    public void clearProducts(Map<String, Product> products) {
        // Kullanıcıdan onay al: tüm ürünleri silmek isteyip istemediğini sorar
        System.out.print("Are you sure you want to clear all products? (yes/no): ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        // İlk onay "yes" ise, ikinci bir onay iste
        if (confirmation.equals("yes")) {
            System.out.println("Warning: This action will remove all products from the list.");
            System.out.print("Are you really sure? Type 'yes' to confirm: ");
            String secondConfirmation = sc.nextLine().trim().toLowerCase();

            // İkinci onay da "yes" ise ürün listesini tamamen temizle
            if (secondConfirmation.equals("yes")) {
                products.clear(); // Ürünleri temizle
                System.out.println("All products have been cleared successfully.");
            } else {
                // İkinci onay verilmezse işlemi iptal et
                System.out.println("Action canceled. Products remain in the list.");
            }
        } else {
            // İlk onay verilmezse işlemi iptal et
            System.out.println("Action canceled. Products remain in the list.");
        }

        // Ürün listesi temizlendikten veya iptal edildikten sonra, mevcut durumu dosyaya kaydet
        saveService.saveToFile(this.products);
    }

    public void updateProduct(Map<String, Product> products) {

        if (!Utils.isProductAvailable(this.products)) return;

        System.out.print("--Please enter the ID of the product you want to update: ");
        String id = sc.nextLine().trim();

        if (this.products.containsKey(id)) {
            Product product = this.products.get(id);

            System.out.println("You may leave fields blank to keep them unchanged.");

            System.out.print("New Product Name (leave blank to keep unchanged): ");
            String newName = sc.nextLine();
            if (!newName.trim().isEmpty()) {
                product.setProductName(newName);
            }

            System.out.print("New Shelf (between 100 and 999, leave blank to keep unchanged): ");
            Integer newShelf = Utils.checkIntegerRange("New Shelf Number: ", 100, 999);
            if (newShelf != null) {
                product.setShelf("SHELF" + newShelf);
            }

            System.out.println("--Product information updated successfully.--");
        } else {
            System.out.println("--No product found with this ID.--");
        }
        sc.nextLine();
        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(this.products);
    }


    public void searchProduct(Map<String, Product> products) {

        if (!Utils.isProductAvailable(products)) {
            System.out.println("--Returning to main menu as there are no products available.--");
            return;
        }

        boolean continueSearch = true;
        while (continueSearch) {
            System.out.println("----Please Select a Search Type----");
            System.out.println("1-Search by Product Name");
            System.out.println("2-Search by Manufacturer Name");
            System.out.println("3-Search for Products Below a Certain Quantity");
            System.out.println("4-Search by Shelf Number");
            System.out.println("0-Return to Main Menu");

            int choice = sc.nextInt();
            sc.nextLine();

            PriorityQueue<Product> searchResults = new PriorityQueue<>(
                    Comparator.comparing(Product::getSku)  // Adjust comparator as needed
            );

            switch (choice) {
                case 1:
                    System.out.print("Enter Product Name to Search: ");
                    String name = sc.nextLine();
                    products.values().stream()
                            .filter(p -> p.getProductName().equalsIgnoreCase(name))
                            .forEach(searchResults::offer);
                    break;
                case 2:
                    System.out.print("Enter Manufacturer Name to Search: ");
                    String manufacturer = sc.nextLine();
                    products.values().stream()
                            .filter(p -> p.getSupplierName().equalsIgnoreCase(manufacturer))
                            .forEach(searchResults::offer);
                    break;
                case 3:
                    System.out.print("Enter Maximum Quantity: ");
                    int maxQuantity = sc.nextInt();
                    products.values().stream()
                            .filter(p -> p.getQuantity() <= maxQuantity)
                            .forEach(searchResults::offer);
                    break;
                case 4:
                    System.out.print("Enter Shelf Number to Search: ");
                    String shelf = sc.nextLine();
                    products.values().stream()
                            .filter(p -> p.getShelf() != null && p.getShelf().equalsIgnoreCase(shelf))
                            .forEach(searchResults::offer);
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    continueSearch = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 0 and 4.");
                    continue;
            }

            if (choice != 0) {
                if (searchResults.isEmpty()) {
                    System.out.println("--No products found matching your criteria.--");
                } else {
                    System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", "SKU", "PRODUCT NAME", "SUPPLIER NAME", "QUANTITY", "UNIT", "SHELF");
                    searchResults.forEach(product -> System.out.println(Utils.formatProduct(product)));
                }
                System.out.println("--Search completed.--");
            }
        }
        sc.nextLine();
        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(this.products);


    }














}
