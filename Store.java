
package csci366;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Store {
        
    private static String jdbcUrl = "jdbc:postgresql://localhost:5432/ShoppingList";
    private static String user = "postgres";
    private static String password = "!1Snappycrab";
    
    private int storeID;
    private String storeName;
    private String storeLocation;
    private List<Item> storeItems;

    public Store(int storeID, String storeName, String storeLocation) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeItems = new ArrayList<>();
    }

    public Store() {
    }
    
    

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public List<Item> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<Item> storeItems) {
        this.storeItems = storeItems;
    }
    //connection to database
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
    //creates a new store
    public static Store createNewStore(String storeName, String storeLocation) {
    try (Connection connection = connect()) {
        String insert = "INSERT INTO Store (store_name, store_location) VALUES (?, ?) RETURNING store_id";
        PreparedStatement ps = connection.prepareStatement(insert);
        ps.setString(1, storeName);
        ps.setString(2, storeLocation);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int storeID = rs.getInt("store_id");
            return new Store(storeID, storeName, storeLocation);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
    
    //displays all items available at a store
    public static Store displayStoreItems (int storeID) {
        try (Connection connection = connect()) {
            String select = "SELECT item_id FROM ItemStore WHERE store_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            preparedStatement.setInt(1, storeID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println("this store contains item id: " + rs.getInt("item_id"));
                System.out.println("-------------");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //adds an item to the store
    public static Store addItemToStore (int itemID, int storeID) {
        if (itemID != -1) {
            try (Connection connection = connect()) {
                String insert = "INSERT INTO ItemStore (item_id, store_id) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insert);
                preparedStatement.setInt(1, itemID);
                preparedStatement.setInt(2, storeID);
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
            e.printStackTrace();
            }
            
        }   
        return null;
    }
    //deletes an item from the store
    public static Store deleteItemFromStore (int itemID, int storeID) {

        try (Connection connection = connect()) {
            String delete = "DELETE FROM ItemStore WHERE item_id = ? AND store_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setInt(1, itemID);
            preparedStatement.setInt(2, storeID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //looks up a store from an id
    public String getStoreNameFromDatabase(int storeID) {
        try (Connection connection = connect()) {
            String getStoreNameFromDB = "SELECT store_name FROM Store WHERE store_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getStoreNameFromDB);
            preparedStatement.setInt(1, this.storeID);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("store_name");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //looks up a store id based on name and location
    public String getStoreIDFromDatabase(String storeName, String storeLocation) {
        try (Connection connection = connect()) {
            String getStoreIDFromDB = "SELECT store_id FROM Store WHERE store_name = ? AND store_location = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getStoreIDFromDB);
            preparedStatement.setString(1, this.storeName);
            preparedStatement.setString(2, this.storeLocation);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("store_id");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //updates an item in a store
    public static Store updateStoreItem(int storeID, int oldItemID, int newItemID) {
        try (Connection connection = connect()) {
            String updateStoreItem = "UPDATE ItemStore SET item_id = ? WHERE item_id = ? AND store_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateStoreItem);
            preparedStatement.setInt(1, newItemID);
            preparedStatement.setInt(2, oldItemID);
            preparedStatement.setInt(3, storeID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}