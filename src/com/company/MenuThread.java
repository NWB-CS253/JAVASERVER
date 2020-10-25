package com.company;

import com.company.Invoice.Product;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MenuThread extends Thread {
    public void run() {
        while (true) {
            System.out.println("Enter 'R' to generate a report");
            System.out.println("Enter 'D' to delete all invoices from server");
            System.out.println("Enter 'E' to terminate the server");

            System.out.println("Enter the task to perform");

            Scanner scanner = new Scanner(System.in);
            String task = scanner.nextLine().toUpperCase();

            switch (task) {
                case "R":
                    System.out.println(report());
                    break;
                case "D":
                    delete();
                    break;
                case "E":
                    System.exit(0);
                default:
                    System.out.println("Invalid task\n");
            }

        }
    }

    private void delete() {
        String path = System.getProperty("user.home") + File.separator + "Invoices";

        System.out.println("WARNING! YOU HAVE CHOSEN TO DELETE ALL INVOICES.\n"
                + "THIS WILL DELETE EVERY FILE ENDING WITH .xml AND .json FROM " + path);

        System.out.println("THE FOLLOWING FILES WILL BE DELETED: \n");

        File directory = new File(path);
        File fileList[] = directory.listFiles();

        if (fileList == null)
            return;

        for (File file : fileList) {
            String fileToDelete = file.getName();
            if (fileToDelete.endsWith(".xml") || fileToDelete.endsWith(".json")) {
                System.out.println(fileToDelete);
            }
        }

        System.out.println("TYPE 'DELETE' OR PRESS ENTER TO CANCEL");

        Scanner scanner = new Scanner(System.in);

        String delete = scanner.nextLine().toUpperCase();

        if (delete.equals("DELETE")) {
            for (File file : fileList) {
                String fileToDelete = file.getName();
                if (fileToDelete.endsWith(".xml") || fileToDelete.endsWith(".json")) {
                    file.delete();
                }
            }

            System.out.println("All .xml and all .json invoices deleted\n");
        }
        else {
            System.out.println("Operation cancelled. No invoices have been deleted.");
        }

    }

    private String report() {
        ArrayList<Product> products = new ArrayList<Product>();
        double total = 0;

        Invoice invoice = new Invoice(products, total);

        ArrayList<Invoice> invoices = new ArrayList<Invoice>();

        String name = null;
        double price = 0;

        Product product = new Product(name, price);

        System.out.println("REPORT\n");

        String path = System.getProperty("user.home") + File.separator + "Invoices";

        File directory = new File(path);
        File[] fileList = directory.listFiles();

        for (File file: fileList) {
            String readThisFile = file.getName().toLowerCase();

            if (readThisFile.endsWith(".json")) {

                try {
                    Gson gson = new Gson();

                    // Create a reader
                    URI uri = file.toURI();

                    Reader reader = Files.newBufferedReader(Paths.get(uri));

                    JsonObject json = gson.fromJson(reader, JsonObject.class);

                    reader.close();

                    JsonArray jsonArray = json.get("invoice").getAsJsonObject().get("products").getAsJsonObject().get("product").getAsJsonArray();

                    for (JsonElement item : jsonArray) {
                        JsonObject elementAsJsonObject = item.getAsJsonObject();
                        name = elementAsJsonObject.get("name").toString();
                        price = elementAsJsonObject.get("price").getAsDouble();

                        product = new Product(name, price);
                        products.add(product);
                    }

                    total = json.get("invoice").getAsJsonObject().get("total").getAsDouble();

                    invoice = new Invoice(products, total);

                    invoices.add(invoice);

                    System.out.println(invoice.print());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else if (readThisFile.endsWith(".xml")) {
                try {
                    Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

                    NodeList nameList = xml.getElementsByTagName("name");
                    NodeList priceList = xml.getElementsByTagName("price");
                    products = new ArrayList<Product>();

                    for (int i = 0; i < nameList.getLength(); i++) {
                        name = nameList.item(i).getTextContent();
                        price = Double.parseDouble(priceList.item(i).getTextContent());

                        product = new Product(name, price);
                        products.add(product);

                    }

                    NodeList nodeList = xml.getElementsByTagName("total");
                    price = Double.parseDouble(priceList.item(0).getTextContent());

                    invoice = new Invoice(products, price);

                    System.out.println(invoice.print());

                    invoices.add(invoice);

                }
                catch (SAXException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();

                }
                catch (ParserConfigurationException e) {
                    e.printStackTrace();

                }

            }
        }

        total = 0;
        for (Invoice i: invoices) {
            total += i.getTotal();
        }

        return "The total amount for " + invoices.size() + " invoices is $" + String.format("%.2f", total)+ "\n";
    }
}
