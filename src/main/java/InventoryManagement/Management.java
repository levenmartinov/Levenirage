package InventoryManagement;

import java.util.Map;

public interface Management {

    void addProduct(Map<String, Product> products);

    void productSku(Product pr);

    void listProduct (Map<String, Product> products);

    void updateProduct(Map<String, Product> products);

    void searchProduct(Map<String, Product> products);
   /* void enterProduct(Map<String, Product> products);


    void putProductOnShelf(Map<String, Product> products);
*/
    void productOutput(Map<String, Product> products);

    void removeProduct(Map<String, Product> products);

    void clearProducts(Map<String, Product> products);





}
