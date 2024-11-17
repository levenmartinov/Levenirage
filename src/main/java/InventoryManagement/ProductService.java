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

        Product pr = new Product(null, null, null, 0, null, 0);

        System.out.println("Enter a product name : ");
        String productName = sc.nextLine().toUpperCase().trim();
        System.out.println("Enter a supplier name : ");
        String supplierName = sc.nextLine().toUpperCase().trim();

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

        System.out.println("Enter a packaging unit : ");
        String unit = sc.nextLine().toUpperCase().trim();

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







}
