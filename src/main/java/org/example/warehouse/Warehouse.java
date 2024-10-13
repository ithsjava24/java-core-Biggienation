package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class Warehouse {
    private static final Map<String,Warehouse> warehouses = new HashMap<>();
    private static String name;
    private static List<ProductRecord> products;
    private static List<ProductRecord> ChangedProducts;


    private Warehouse() {
        products = new ArrayList<>();
        ChangedProducts = new ArrayList<>();
    }


    private Warehouse(String name){
        Warehouse.name = name;
        products = new ArrayList<>();
        ChangedProducts = new ArrayList<>();
    }


    public static Warehouse getInstance() {
        return new Warehouse();
    }


    public static Warehouse getInstance(String name) {
        return warehouses.computeIfAbsent(name, Warehouse::new);
    }


    public String getName() {
        return name;
    }


    public boolean isEmpty() {
        return products.isEmpty();
    }


    public List<ProductRecord> getProducts() {
        return products.stream().toList();
    }


    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }


        if (price == null) {
            price = BigDecimal.ZERO;
        }


        UUID tempUuid = uuid;
        boolean extra = products.stream().anyMatch(e -> e.uuid().equals(tempUuid));
        if(extra){
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }


        ProductRecord productRecord = new ProductRecord(uuid, name, category, price);
        products.add(productRecord);
        return productRecord;
    }


    public Optional<ProductRecord> getProductById(UUID uuid) {
        return products.stream().filter(e -> e.uuid().equals(uuid)).findFirst();
    }


    public void updateProductPrice(UUID uuid, BigDecimal bigDecimal) {
       Optional<ProductRecord> product = getProductById(uuid);
       if (product.isEmpty()) noProductWithId();
       product.ifPresent(e -> {
            ProductRecord temp = new ProductRecord(e.uuid(),e.name(),e.category(),bigDecimal);
            ChangedProducts.add(e);
            products.remove(e);
            products.add(temp);
        });
    }

    public void noProductWithId (){
        throw new IllegalArgumentException("Product with that id doesn't exist.");
    }

    public List<ProductRecord> getChangedProducts() {
        return ChangedProducts;
    }


    public Map<Category,List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream().collect(Collectors.groupingBy(ProductRecord::category));

    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream().filter(e -> e.category().equals(category)).toList();
    }

}
