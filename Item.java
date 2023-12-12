/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci366;

import static csci366.Category.jdbcUrl;
import static csci366.Category.password;
import static csci366.Category.user;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */

public class Item {
   static final String jdbcUrl = "jdbc:postgresql://localhost:5432/ShoppingList";
   static final String user = "postgres";
   static final String password = "!1Snappycrab";
   private int itemId;  
   private String itemName;
   private double price;
   private String category;
   private int categoryId;
   
   public Item() {
    }

    public Item(String itemName, double price, String category) {
        this.itemName = itemName;
        this.price = price;
        this.category = category;
    }

    // Getter and Setter methods
    public int getItemId(){
        return itemId;
    }
    
    public void setItemId(int itemId){
        this.itemId = itemId; 
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getCategoryId(){
        return categoryId;
    }
    
    public void setCategoryId(int categoryId){
        this.categoryId = categoryId; 
    }

   public int createNewItem() {
        try (Connection connection = connect()) {
            String insertItemQuery = "INSERT INTO Item(item_name, price, category_id) VALUES (?, ?, ?) RETURNING item_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertItemQuery)) {
                preparedStatement.setString(1, itemName);
                preparedStatement.setDouble(2, price);
                preparedStatement.setInt(3, categoryId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        itemId = resultSet.getInt("item_id");
                        return itemId;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void deleteItemFromCategory() {
    try (Connection connection = connect()) {
        String updateItemQuery = "UPDATE Item SET category_id = NULL WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateItemQuery)) {
            preparedStatement.setInt(1, itemId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Item removed from the category successfully.");
            } else {
                System.out.println("Failed to remove the item from the category.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void updateItemName(String newItemName) {
    try (Connection connection = connect()) {
        // Retrieve the current item name before updating
        String currentItemName = getCurrentItemName();

        // Display the current item name
        System.out.println("Current Item Name: " + currentItemName);

        String updateItemNameQuery = "UPDATE Item SET item_name = ? WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateItemNameQuery)) {
            preparedStatement.setString(1, newItemName);
            preparedStatement.setInt(2, itemId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Item name updated successfully.");
            } else {
                System.out.println("Failed to update the item name.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Add this method to retrieve the current item name
public String getCurrentItemName() {
    try (Connection connection = connect()) {
        String selectItemNameQuery = "SELECT item_name FROM Item WHERE item_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectItemNameQuery)) {
            preparedStatement.setInt(1, itemId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("item_name");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}

public double getCurrentItemPrice() {
        try (Connection connection = connect()) {
            String selectItemPriceQuery = "SELECT price FROM Item WHERE item_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectItemPriceQuery)) {
                preparedStatement.setInt(1, itemId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("price");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if there's an error or if the item is not found
    }

    public void updateItemPrice(double newPrice) {
        try (Connection connection = connect()) {
            String updateItemPriceQuery = "UPDATE Item SET price = ? WHERE item_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateItemPriceQuery)) {
                preparedStatement.setDouble(1, newPrice);
                preparedStatement.setInt(2, itemId);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Item price updated successfully.");
                } else {
                    System.out.println("Failed to update item price. Item not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}

