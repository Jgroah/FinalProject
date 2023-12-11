/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci366;

import java.util.ArrayList;
import java.util.List;
import java.sql.*; 
/**
 *
 * @author jacob
 */
public class Category {
    
    static final String jdbcUrl = "jdbc:postgresql://localhost:5432/ShoppingList";
    static final String user = "postgres";
    static final String password = "!1Snappycrab";
        
    private int categoryId;
    private String categoryName;
    private List<Item> items; 
    
   // Constructor for existing categories in the database
    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.items = new ArrayList<>();
    }

// Constructor for new categories not yet in the database
    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.items = new ArrayList<>();
    }
    
    //Getter and Setters
    
    public int getCategoryId(){
        return categoryId;
    }
    
    public void setCategoryId(int id){
        this.categoryId = id; 
    }
    
    public String getCategoryName(){
        return categoryName; 
    }
    
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;    
    }
    
    public List<Item> getItems(){
        return items;
    }
    
    public void setItems(List<Item> items){
        this.items = items;
    }
    
   //Method to add an item to a category in the database
    public void addItemToCategory(Item item) {
    int itemId = item.createNewItem(); 

    if (itemId != -1) {
        try (Connection connection = connect()) {
            String insertItemCategoryQuery = "INSERT INTO ItemCategory (item_id, category_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertItemCategoryQuery)) {
                preparedStatement.setInt(1, itemId);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    // Method to delete an item from the category in the database
    public void deleteItemFromCategory(Item item) {
        try (Connection connection = connect()) {
            String deleteItemCategoryQuery = "DELETE FROM ItemCategory WHERE item_id = ? AND category_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteItemCategoryQuery)) {
                preparedStatement.setInt(1, item.getItemId());
                preparedStatement.setInt(2, categoryId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   public String getCategoryNameFromDatabase() {
    try (Connection connection = connect()) {
        String getCategoryNameQuery = "SELECT category_name FROM Category WHERE category_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getCategoryNameQuery)) {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("category_name");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    
    public void updateItemInCategory(Item oldItem, Item newItem) {
        deleteItemFromCategory(oldItem); // Delete the old item from the category
        addItemToCategory(newItem); // Add the new item to the category
    }
    
    public static Category createNewCategory(String categoryName) {
        try (Connection connection = connect()) {
            String insertCategoryQuery = "INSERT INTO Category (category_name) VALUES (?) RETURNING category_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCategoryQuery)) {
                preparedStatement.setString(1, categoryName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int categoryId = resultSet.getInt("category_id");
                        return new Category(categoryId, categoryName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void updateCategoryName(String newCategoryName) {
        try (Connection connection = connect()) {
            String updateCategoryQuery = "UPDATE Category SET category_name = ? WHERE category_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateCategoryQuery)) {
                preparedStatement.setString(1, newCategoryName);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.executeUpdate();
                setCategoryName(newCategoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to display items in the category (not querying the database, just displaying the local items list)
    public void displayItemsInCategory() {
        System.out.println("Items in Category '" + getCategoryName() + "':");
        for (Item item : items) {
            System.out.println(item.getItemName());
        }
    }
    
     private static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}
