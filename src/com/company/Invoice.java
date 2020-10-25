package com.company;

import java.util.ArrayList;

public class Invoice {

    static class Product {
        String name;
        double price;

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String toString() {
            return getName() + ": " + "$" + String.format("%2f", getPrice());
        }
    }

    private ArrayList<Product> products;
    private double total;

    public Invoice(ArrayList<Product> products, double total) {
        this.products = products;
        this.total = total;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public double getTotal() {
        return total;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String print() {
        for (Product product : products) {
            System.out.println(product.toString());
        }

        return "Invoice total: $" + String.format("%2f", getTotal()) + "\n";
    }
}
