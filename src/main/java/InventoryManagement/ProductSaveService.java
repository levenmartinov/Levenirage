package InventoryManagement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProductSaveService {

    private static final String FILE_PATH = "products.json";

    public void saveToFile(Map<String, Product> products) {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            Gson gson = new Gson();
            gson.toJson(products, writer);

        }catch (IOException e) {
            System.err.println("Error while saving products : " + e.getMessage());
        }
    }


    public Map<String, Product> loadFromFile() {

        Map<String, Product> products = new LinkedHashMap<>();

        try  (FileReader reader = new FileReader(FILE_PATH)) {

            Gson gson =new Gson();
            Type type = new TypeToken<LinkedHashMap<String, Product>>() {}.getType();
            products = gson.fromJson(reader, type);

            if (products == null) {
                products = new LinkedHashMap<>();
            }

        } catch (IOException e) {
            System.err.println("Error while loading products : " + e.getMessage());
        }
        return products;
    }

}
