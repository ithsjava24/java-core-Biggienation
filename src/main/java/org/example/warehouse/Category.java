package org.example.warehouse;
import java.util.HashMap;
import java.util.Map;

public class Category {
    private static final Map<String, Category> categories = new HashMap<>();
    private final String name;

    Category(String name) {
        String s1 = name.substring(0, 1).toUpperCase();
        this.name = s1 + name.substring(1);
    }

    public String getName() {
        return name;
    }

    public static Category of (String name) {
       if( name == null)
            throw new IllegalArgumentException("Category name can't be null");
        return categories.computeIfAbsent(name, Category::new);
    }

}
