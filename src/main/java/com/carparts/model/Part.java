package com.carparts.model;

public class Part {
    private int id;
    private String name;
    private String category;
    private String manufacturer;
    private double price;
    private int stock;
    private String description;

    public Part() {}

    public Part(int id, String name, String category, String manufacturer,
                double price, int stock, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getManufacturer() { return manufacturer; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setDescription(String description) { this.description = description; }
}