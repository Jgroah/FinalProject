package csci366;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Main Menu:");
            System.out.println("1. Create a new shopping list");
            System.out.println("2. Administrative Section");
            System.out.println("3. Exit");
            

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                   //Add User section here
                    break;
                case 2:
                    enterAdminSection();
                    break;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void enterAdminSection() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Administrative Section:");
            System.out.println("1. Create a new category");
            System.out.println("2. Update category name");
            System.out.println("3. Add item to category");
            System.out.println("4. Delete item from category");
            System.out.println("5. Update item in category");
            System.out.println("6. Exit to Main Menu");

            int adminChoice = scanner.nextInt();

            switch (adminChoice) {
                case 1:
                    createNewCategory();
                    break;
                case 2:
                    updateCategoryName();
                    break;
                case 3:
                    addItemToCategory();
                    break;
                case 4:
                    deleteItemFromCategory();
                    break;
                case 5: 
                    updateItemInCategory();
                    break;
                case 6:
                    return; // Exit to the main menu
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void createNewCategory() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the name of the new category: ");
    String categoryName = scanner.next();

    Category newCategory = Category.createNewCategory(categoryName);
    if (newCategory != null) {
        System.out.println("Category created with ID: ");
    } else {
        System.out.println("Failed to create a new category.");
    }
}


    private static void updateCategoryName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the category ID you want to update: ");
        int categoryId = scanner.nextInt();

        // Retrieve the current category name from the database
        Category categoryToUpdate = new Category(categoryId, "");
        String currentCategoryName = categoryToUpdate.getCategoryNameFromDatabase();

        if (currentCategoryName != null) {
            System.out.println("Current Category Name: " + currentCategoryName);

            System.out.print("Enter the new category name: ");
            String newCategoryName = scanner.next();

            categoryToUpdate.updateCategoryName(newCategoryName);
            System.out.println("Category name updated successfully.");
        } else {
            System.out.println("Category with ID " + categoryId + " not found in the database.");
        }
    }
    
    private static void addItemToCategory() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the category ID where you want to add the item: ");
    int categoryId = scanner.nextInt();

    System.out.println("Category ID entered: " + categoryId);

    // Retrieve the current category name from the database
    Category category = new Category(categoryId, "");
    String currentCategoryName = category.getCategoryNameFromDatabase();

    if (currentCategoryName != null) {
        System.out.println("Current Category Name: " + currentCategoryName);

        System.out.print("Enter the item name: ");
        String itemName = scanner.next();

        System.out.print("Enter the item price: ");
        double price = scanner.nextDouble();

        // Create an instance of the Item class and set the categoryId
        Item newItem = new Item(itemName, price, currentCategoryName);
        newItem.setCategoryId(categoryId);

        // Call the createNewItem method
        int newItemId = newItem.createNewItem();

        if (newItemId != -1) {
            System.out.println("Item added to category successfully with ID: " + newItemId);
        } else {
            System.out.println("Failed to add the item to the category.");
        }
    } else {
        System.out.println("Category with ID " + categoryId + " not found in the database.");
    }
}
     
    private static void deleteItemFromCategory() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the item ID you want to remove from the category: ");
    int itemId = scanner.nextInt();

    // Create an instance of the Item class
    Item item = new Item();
    item.setItemId(itemId);

    // Call the deleteItemFromCategory method
    item.deleteItemFromCategory();
}
    
  private static void updateItemInCategory() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the item ID you want to update: ");
    int itemId = scanner.nextInt();

    // Create an instance of the Item class
    Item item = new Item();
    item.setItemId(itemId);

    // Displays the previous name
    String currentItemName = item.getCurrentItemName();
    if (currentItemName != null) {
        System.out.println("Current Item Name: " + currentItemName);
        
        // Prompt the user for the new item name
        System.out.print("Enter the new item name: ");
        String newItemName = scanner.next();

        
        item.updateItemName(newItemName);
    } else {
        System.out.println("Item with ID " + itemId + " not found in the database.");
    }
}

  private static void displayItemsInCategory() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the category ID to display items: ");
    int categoryId = scanner.nextInt();

    // Create an instance of the Category class
    Category category = new Category(categoryId, "");

    // Retrieve the category name from the database
    String categoryName = category.getCategoryNameFromDatabase();

    // Check if the categoryName is not null before creating the Category object
    if (categoryName != null) {
        category.setCategoryName(categoryName);

        // Display items in the category
        category.displayItemsInCategory();
    } else {
        System.out.println("Category with ID " + categoryId + " not found in the database.");
    }
}
   
}
