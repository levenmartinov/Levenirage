package InventoryManagement;



import java.time.LocalDate;
import java.util.*;

public class ProductService implements Management {

    Map<String, Product> products;

    Scanner sc = new Scanner(System.in);

    ProductSaveService saveService = new ProductSaveService();
    private PriorityQueue<Object> searchResults;

    public ProductService() {
        this.searchResults = searchResults;
        products = saveService.loadFromFile();

    }


    @Override
    public void addProduct(Map<String, Product> products) {

        Product pr = new Product(null, null, null, 0, null, null);

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

        int productQuantity = 0; // Değişken başlatıldı
        do {
            System.out.println("Enter a quantity (or type 'M' to return to the main menu): ");
            String input = sc.nextLine().trim(); // Girdiyi al ve baştaki/sondaki boşlukları temizle

            // Ana menüye dönme kontrolü
            if (input.equalsIgnoreCase("M")) { // Kullanıcı 'M' veya 'm' girerse
                System.out.println("Returning to the main menu...");
                return; // Ana menüye döner
            }

            // Boş kontrolü
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty! Please enter a valid quantity.");
                continue; // Döngüyü başa döndür
            }

            // Sayısal kontrol
            try {
                productQuantity = Integer.parseInt(input);
                if (productQuantity <= 0) {
                    System.out.println("Quantity should be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a numeric value for quantity.");
            }
        } while (productQuantity <= 0);

// Burada ürün miktarı işlemleri devam eder
        System.out.println("Quantity successfully entered: " + productQuantity);




/*        do {
            System.out.println("Enter a quantity : ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a numeric value for quantity.");
                sc.next(); //clear wrong input
            }
            productQuantity = sc.nextInt();
            //sc.nextLine(); //hataa olbilirr daha sonra kotrol et ve bu yorumu sil
            if (productQuantity <= 0) {
                System.out.println("Quantity should be a positive number.");
            }
        } while (productQuantity <= 0);*/



        int shelfNo;

        boolean isShelfAvailable = false;

        do {
            System.out.print("Enter a positive shelf number : \n");


            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid positive integer for the Shelf Number.");
                sc.next();
            }

            shelfNo = sc.nextInt();


            if (shelfNo < 0) {
                System.out.println("Shelf number must be a positive integer. Please try again.");
                continue;
            }

            isShelfAvailable = true;// Kontrol başlangıcı için varsayılan değer.

            for (Product p : products.values()) {
                if (p.getShelf() != null && p.getShelf().equals("SHELF" + shelfNo)) {

                    System.out.println("This shelf is already occupied. Try a different one.");
                    isShelfAvailable = false;
                    break;
                }

            }


        } while (!isShelfAvailable);


        // Ürün özelliklerini ayarla ve SKU'yi oluştur
        pr.setProductName(productName);
        pr.setSupplierName(supplierName);
        pr.setQuantity(productQuantity);
        pr.setUnit(unit);
        pr.setShelf("SHELF" + shelfNo); // Shelf değerini ayarla
        productSku(pr);  // Ürün ID'sini ayarla
        products.put(pr.getSku(), pr); // Ürünü Map'e ekle

        sc.nextLine();


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

    public void updateProduct(Map<String, Product> products) {
        System.out.print("Enter the product ID to update: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product == null) {
            System.out.println("The ID you have entered is not on the list. Please check again.");
            return;
        }

        // Kullanıcıdan hangi işlemi yapmak istediğini soruyoruz.
        System.out.println("\nWhat would you like to update?\n");
        System.out.println("1. Update Quantity On Products : ");
        System.out.println("2. Update Shelf in Warehouse : ");
        System.out.println("3. Update All Products Informations");
        System.out.print("\nEnter your choice: \n");

        int choice;
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid number (1-3).");
            sc.next();
        }
        choice = sc.nextInt();
        sc.nextLine(); // Satır sonunu temizle.

        switch (choice) {
            case 1: // Stok Güncelleme
                updateQuantity(products);
                break;

            case 2: // Raf Yerleştirme
                placeOnShelf(product, products);
                break;

            case 3: // Ürün Bilgisi Güncelleme
                updateInfo(product);
                break;

            default:
                System.out.println("Invalid choice. Please select a number between 0 and 4.");
                break;
        }

        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(products);
    }


    // Stok güncelleme işlevi (artırma veya azaltma)
    private void updateQuantity (Map<String, Product> products) {
        System.out.print("Enter the product ID: ");
        String productSku = sc.nextLine().trim();
        Product product = products.get(productSku);

        if (product != null) {
            System.out.println("\nDo you want to:");
            System.out.println("1. Add stock");
            System.out.println("2. Remove stock");
            System.out.print("Enter your choice (1 or 2): \n");

            int choice;
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter 1 or 2.");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // Satır sonunu temizle

            if (choice == 1) {
                // Stok artırma
                int quantity;
                do {
                    System.out.print("Enter the quantity to add: \n");
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

                product.setQuantity(product.getQuantity() + quantity);
                System.out.println("Product quantity updated successfully. NEW STOCK: " + product.getQuantity());

            } else if (choice == 2) {
                // Stok azaltma
                int quantity;
                do {
                    System.out.print("Enter the quantity to remove: \n");
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
                System.out.println("Invalid choice. Please enter 1 or 2.");
                return;
            }

            // Güncellenen ürünü dosyaya kaydet
            saveService.saveToFile(products);
        } else {
            System.out.println("The ID you have entered is not on the list. Please check again.");
        }
    }

    // Raf yerleştirme işlevi
    private void placeOnShelf(Product product, Map<String, Product> products) {
        int shelfNo;
        boolean isShelfAvailable = false;
        do {
            System.out.print("Enter a positive shelf number: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid positive integer for the Shelf Number.");
                sc.next();
            }
            shelfNo = sc.nextInt();
            sc.nextLine();

            if (shelfNo < 0) {
                System.out.println("Shelf number must be a positive integer. Please try again.");
                continue;
            }

            isShelfAvailable = true; // Kontrol başlangıcı için varsayılan değer.
            for (Product p : products.values()) {
                if (p.getShelf() != null && p.getShelf().equals("SHELF" + shelfNo)) {
                    System.out.println("This shelf is already occupied. Try a different one.");
                    isShelfAvailable = false;
                    break;
                }
            }

        } while (!isShelfAvailable);

        product.setShelf("SHELF" + shelfNo);
        System.out.println("Product placed on shelf " + product.getShelf() + " successfully.");

        // Güncellenen ürünü dosyaya kaydet
        saveService.saveToFile(products);
    }


    // Ürün bilgisi güncelleme işlevi
    private void updateInfo(Product product) {
        System.out.println("You may leave fields blank to keep them unchanged!!!\n");

        System.out.print("New Product Name (leave blank to keep unchanged) : \n");
        String newName = sc.nextLine().toUpperCase().trim();
        boolean nameUpdated = false;
        String oldSku = product.getSku(); // Eski SKU'yu kaydet

        if (!newName.isEmpty() && !newName.equals(product.getProductName())) {
            product.setProductName(newName);
            updateSku(product); // Yeni SKU oluştur
            nameUpdated = true;
            System.out.println("Product name and SKU updated successfully.\n");
        }

        System.out.print("New Supplier Name (leave blank to keep unchanged) : \n");
        String newSupplierName = sc.nextLine().toUpperCase().trim();
        if (!newSupplierName.isEmpty() && !newSupplierName.equals(product.getSupplierName())) {
            product.setSupplierName(newSupplierName);
            System.out.println("Supplier name updated successfully.\n");
        }

        System.out.print("New Unit (leave blank to keep unchanged) : \n");
        String newUnit = sc.nextLine().toUpperCase().trim();
        if (!newUnit.isEmpty() && !newUnit.equals(product.getUnit())) {
            product.setUnit(newUnit);
            System.out.println("Unit updated successfully.\n");
        }

        // Eski SKU ile yeni SKU aynı değilse eski kaydı sil ve yeni SKU ile ekle
        if (nameUpdated) {
            products.remove(oldSku); // Eski SKU'yu sil
            products.put(product.getSku(), product); // Yeni SKU ile ekle
        }

        if (!nameUpdated && newName.isEmpty() && newSupplierName.isEmpty() && newUnit.isEmpty()) {
            System.out.println("No changes were made as all fields were left blank.\n");
        } else {
            System.out.println("Updated Product Information :\n");
            System.out.printf("SKU: %s | Name: %s | Supplier: %s | Unit: %s\n",
                    product.getSku(), product.getProductName(), product.getSupplierName(), product.getUnit());
            System.out.println("\n");
        }

        saveService.saveToFile(products); // Dosyaya kaydet
    }

    private void updateSku(Product product) {
        try {
            String newSku = product.getProductName().substring(0, 2) + LocalDate.now().getYear() + Product.counter;
            Product.counter++; // Benzersiz yapıcı sayaç artırımı
            product.setSku(newSku); // Yeni SKU ayarla
        } catch (StringIndexOutOfBoundsException e) {
            product.setSku("NULL" + LocalDate.now().getYear() + Product.counter);
            Product.counter++;
        }
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
            System.out.println("2-Search by Supplier Name");
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
                    System.out.print("Enter Supplier Name to Search: ");
                    String supplier = sc.nextLine();
                    products.values().stream()
                            .filter(p -> p.getSupplierName().equalsIgnoreCase(supplier))
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
                    System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", "~~SKU~~", "PRODUCT NAME", "SUPPLIER NAME", "QUANTITY", "UNIT", "SHELF");
                    System.out.printf("%-20s %-20s %-20s %-15s %-10s %-10s%n", "-------", "------------", "-------------", "--------", "----", "-----");
                    searchResults.forEach(product -> System.out.println(Utils.formatProduct(product)));
                }
                System.out.println("\n~~~~~Search completed~~~~~\n");
            }
        }
        sc.nextLine();
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


}
