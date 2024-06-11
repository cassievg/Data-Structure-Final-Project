import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ManagementApp {
    private static final String FILENAME = "products.json";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        //Asks user which data structure to use
        System.out.println("Choose your data structure: ");
        System.out.println("1. ArrayList \n2. LinkedList");
        int choice = scanner.nextInt();
        scanner.nextLine();
        List<Product> productList;

        switch (choice) {
            case 1:
                productList = new ArrayList<>();
                break;
            case 2:
                productList = new LinkedList<>();
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                return;
        }

        //Load data from file
        loadFromFile(productList);

        //Start up window
        while (true) {

            //Prompts user to choose what action to take
            System.out.println("\nWelcome to your inventory! \nWhat would you like to do?");
            System.out.println("1. View products \n2. Add product \n3. Remove product \n4. Edit product \n5. Search product \n6. Sort products \n7. View products to be restocked \n8. Save and Exit");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    viewProducts(productList);
                    break;
                case 2:
                    addProduct(productList, scanner);
                    break;
                case 3:
                    removeProduct(productList, scanner);
                    break;
                case 4:
                    editProduct(productList, scanner);
                    break;
                case 5:
                    searchProduct(productList, scanner);
                    break;
                case 6:
                    sortProducts(productList, scanner);
                    break;
                case 7:
                    viewProductsToBeRestocked(productList);
                    break;
                case 8:
                    saveToFile(productList);
                    System.out.println("Data saved successfully. Exiting...");
                    return;
                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    //Displays a list of the products
    private static void viewProducts(List<Product> productList) {
        System.out.println("Viewing products:");

        for (Product product : productList) {
            System.out.println(" ID: " + product.getId() + "\n Name: " + product.getName() + "\n Stock: " + product.getStock() + "\n Min Stock: " + product.getMinStock() + "\n");
        }
    }

    //Adds a new product from details given by the user
    private static void addProduct(List<Product> productList, Scanner scanner) {
        System.out.println("\nEnter product name: ");
        String addName = scanner.nextLine();
        System.out.println("Enter product stock: ");
        int addStock = scanner.nextInt();
        System.out.println("Enter product minimum stock: ");
        int addMinStock = scanner.nextInt();
        scanner.nextLine();
        int newId = productList.isEmpty() ? 1 : productList.getLast().getId() + 1; //New id is the last product id incremented by 1
        Product addProduct = new Product(newId, addName, addStock, addMinStock); //Product is created
        productList.add(addProduct); //Product is added to the list
        System.out.println("Product added successfully.");
    }

    //Removes a product based off product id given by user
    private static void removeProduct(List<Product> productList, Scanner scanner) {
        System.out.println("\nEnter product ID to remove: ");
        int removeId = scanner.nextInt();
        scanner.nextLine();
        boolean removed = productList.removeIf(product -> product.getId() == removeId);
        if (removed) { //Checks if product id exists in the list
          System.out.println("Product removed successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    //Overwrites product details based off details given by the user
    private static void editProduct(List<Product> productList, Scanner scanner) {
        System.out.println("\nEnter product ID to edit: ");
        int editId = scanner.nextInt();
        scanner.nextLine();

        //Binary search algorithm
        boolean found = false;
        int left = 0;
        int right = productList.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midProduct = productList.get(mid);
            if (midProduct.getId() == editId) {
                System.out.println("Current details:");
                System.out.println(" ID: " + midProduct.getId() + "\n Name: " + midProduct.getName() + "\n Stock: " + midProduct.getStock() + "\n Min Stock: " + midProduct.getMinStock() + "\n");
                System.out.println("\nEnter new name: ");
                String newName = scanner.nextLine();
                System.out.println("Enter new stock: ");
                int newStock = scanner.nextInt();
                System.out.println("Enter new minimum stock: ");
                int newMinStock = scanner.nextInt();
                scanner.nextLine();
                midProduct.setName(newName);
                midProduct.setStock(newStock);
                midProduct.setMinStock(newMinStock);
                System.out.println("Product updated successfully.");
                found = true;
            }
            if (midProduct.getId() < editId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (!found) { //Checks if product id exists
            System.out.println("Product not found.");
        }
    }

    //Searches product based off product name given by the user
    private static void searchProduct(List<Product> productList, Scanner scanner) {
        System.out.println("\nEnter product name to search: ");
        String searchName = scanner.nextLine();
        boolean found = false;

        //Linear search algorithm
        for (Product product : productList) {
            if (product.getName().equalsIgnoreCase(searchName)) {
                System.out.println(" ID: " + product.getId() + "\n Name: " + product.getName() + "\n Stock: " + product.getStock() + "\n Min Stock: " + product.getMinStock() + "\n");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Product not found.");
        }
    }

    //Sorts products
    private static void sortProducts(List<Product> productList, Scanner scanner) {
        System.out.println("\nSort by: \n1. Newest to oldest");
        int sortInput = scanner.nextInt();
        scanner.nextLine();
        switch (sortInput) {
            case 1:
                productList.sort(Comparator.comparingInt(Product::getId).reversed()); //By default timsort
                break;
            default:
                System.out.println("Invalid input.");
                return;
        }
        viewProducts(productList);
        productList.sort(Comparator.comparingInt(Product::getId)); //Returns list to original order
    }

    //Filters products to display the ones that need to be restocked
    private static void viewProductsToBeRestocked(List<Product> productList) {
        System.out.println("\nProducts to be restocked:");
        for (Product product : productList) {
            if (product.getStock() < product.getMinStock()) { //When current stock < min stock
                System.out.println(product.getId() + " " + product.getName());
            }
        }
    }

    //Saves data into JSON file
    private static void saveToFile(List<Product> productList) {

        JSONArray jsonArray = new JSONArray();

        //Conversion of each product into JSONObject
        for (Product product : productList) {
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("id", product.getId());
            jsonProduct.put("name", product.getName());
            jsonProduct.put("stock", product.getStock());
            jsonProduct.put("minStock", product.getMinStock());
            jsonArray.add(jsonProduct); //Each JSONObject of product is put into JSONArray
        }

        try (FileWriter file = new FileWriter(FILENAME)) {
            file.write(jsonArray.toJSONString()); //Converts JSONArray to string and writes to the file
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Loads data from JSON file
    @SuppressWarnings("unchecked")
    private static void loadFromFile(List<Product> productList) {

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(FILENAME));

            JSONArray jsonArray = (JSONArray) obj;

            //Conversion of JSONArray and JSONObject back into List and Product
            for (Object o : jsonArray) {
                JSONObject jsonProduct = (JSONObject) o;
                int id = ((Long) jsonProduct.get("id")).intValue();
                String name = (String) jsonProduct.get("name");
                long stock = (long) jsonProduct.get("stock");
                long minStock = (long) jsonProduct.get("minStock");
                Product product = new Product(id, name, (int) stock, (int) minStock);
                productList.add(product);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting with an empty inventory."); //Message output when file is not found
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
