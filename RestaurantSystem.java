import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class RestaurantSystem {

    static String[] items = new String[50];
    static int[] prices = new int[50];
    static int itemCount = 0;

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        loadMenuFromFile();

        System.out.println("========== RESTAURANT SYSTEM ==========");
        System.out.print("Enter password (admin123 for admin): ");
        String password = input.nextLine();

        if (password.equals("admin123")) {
            adminPanel();
        } else {
            customerPanel();
        }

        input.close();
    }

    // ================= ADMIN PANEL =================
    static void adminPanel() {

        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Show Menu");
            System.out.println("3. Take Order");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();

                switch (choice) {

                    case 1:
                        addMenuItem();
                        break;

                    case 2:
                        showMenu();
                        break;

                    case 3:
                        takeOrder();
                        break;

                    case 4:
                        System.out.println("Exiting Admin Panel.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input.");
                input.nextLine();
            }

        } while (choice != 4);
    }

    // ================= CUSTOMER PANEL =================
    static void customerPanel() {

        Scanner input = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- CUSTOMER PANEL ---");
            System.out.println("1. Show Menu");
            System.out.println("2. Give Order");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();

                switch (choice) {

                    case 1:
                        showMenu();
                        break;

                    case 2:
                        takeOrder();
                        break;

                    case 3:
                        System.out.println("Thank you for visiting.");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input.");
                input.nextLine();
            }

        } while (choice != 3);
    }

    // ================= ADD MENU ITEM =================
    static void addMenuItem() {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter item name: ");
        String name = input.nextLine();

        System.out.print("Enter item price: ");
        int price = input.nextInt();

        items[itemCount] = name;
        prices[itemCount] = price;
        itemCount++;

        try {
            FileWriter writer = new FileWriter("menu.txt", true);
            writer.write(name + "," + price + "\n");
            writer.close();
            System.out.println("Item added successfully.");
        } catch (IOException e) {
            System.out.println("Error saving menu.");
        }
    }

    // ================= SHOW MENU =================
    static void showMenu() {

        if (itemCount == 0) {
            System.out.println("Menu is empty.");
            return;
        }

        System.out.println("\n------ MENU ------");

        for (int i = 0; i < itemCount; i++) {
            System.out.println(i + ". " + items[i] + " Rs." + prices[i]);
        }
    }

    // ================= LOAD MENU FROM FILE =================
    static void loadMenuFromFile() {

        try {
            File file = new File("menu.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length == 2) {
                    items[itemCount] = data[0];
                    prices[itemCount] = Integer.parseInt(data[1]);
                    itemCount++;
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading menu file.");
        }
    }

    // ================= TAKE ORDER =================
    static void takeOrder() {

        Scanner input = new Scanner(System.in);

        int[] orderIndex = new int[20];
        int[] orderQuantity = new int[20];
        int orderCount = 0;

        while (true) {

            showMenu();

            System.out.print("Enter item number (-1 to finish): ");
            int number = input.nextInt();

            if (number == -1) {
                break;
            }

            if (number >= 0 && number < itemCount) {

                System.out.print("Enter quantity: ");
                int quantity = input.nextInt();

                orderIndex[orderCount] = number;
                orderQuantity[orderCount] = quantity;
                orderCount++;

            } else {
                System.out.println("Invalid item number.");
            }
        }

        if (orderCount > 0) {
            generateBill(orderIndex, orderQuantity, orderCount);
            saveOrderToFile(orderIndex, orderQuantity, orderCount);
        } else {
            System.out.println("No order placed.");
        }
    }

    // ================= GENERATE BILL =================
    static void generateBill(int[] orderIndex, int[] orderQuantity, int orderCount) {

        int total = 0;

        System.out.println("\n------ BILL ------");

        for (int i = 0; i < orderCount; i++) {

            int index = orderIndex[i];
            int quantity = orderQuantity[i];
            int price = prices[index] * quantity;

            total = total + price;

            System.out.println(items[index] + " x " + quantity + " = Rs." + price);
        }

        System.out.println("Total Amount: Rs." + total);
    }

    // ================= SAVE ORDER TO FILE =================
    static void saveOrderToFile(int[] orderIndex, int[] orderQuantity, int orderCount) {

        try {
            FileWriter writer = new FileWriter("orders.txt", true);
            writer.write("----- New Order -----\n");

            for (int i = 0; i < orderCount; i++) {

                int index = orderIndex[i];
                int quantity = orderQuantity[i];
                int price = prices[index] * quantity;

                writer.write(items[index] + " x " + quantity + " = Rs." + price + "\n");
            }

            writer.write("\n");
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving order.");
        }
    }
}