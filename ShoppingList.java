package csci366;

import java.util.ArrayList;
import java.util.List;
import java.sql.*; 
/**
 *
 * @author nafis
 */

 public class ShoppingList {
    static final String jdbcUrl = "jdbc:postgresql://localhost:5432/ShoppingList";
    static final String user = "postgres";
    static final String password = "!1Snappycrab";

    private int shoppingListId;
    private int customerId;
    private List<Item> items;

    public ShoppingList(int shoppingListId, int customerId) {
        this.shoppingListId = shoppingListId;
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static ShoppingList createNewShoppingList(int customerId) {
        try (Connection connection = connect()) {
            String createShoppingListQuery = "INSERT INTO ShoppingList (customer_id) VALUES (?) RETURNING shopping_list_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(createShoppingListQuery)) {
                preparedStatement.setInt(1, customerId);

                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int shoppingListId = resultSet.getInt("shopping_list_id");
                        return new ShoppingList(shoppingListId, customerId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if there's an error
    }

    public void addItemToShoppingList(Item item) {
        int itemId = item.createNewItem();

        if (itemId != -1) {
            try (Connection connection = connect()) {
                String insertShoppingListItemQuery = "INSERT INTO ShoppingListItem (shopping_list_id, item_id) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertShoppingListItemQuery)) {
                    preparedStatement.setInt(1, shoppingListId);
                    preparedStatement.setInt(2, itemId);
                    preparedStatement.executeUpdate();

                    // Add the item to the local items list
                    items.add(item);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteItemFromShoppingList(Item item) {
        try (Connection connection = connect()) {
            String deleteShoppingListItemQuery = "DELETE FROM ShoppingListItem WHERE shopping_list_id = ? AND item_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteShoppingListItemQuery)) {
                preparedStatement.setInt(1, shoppingListId);
                preparedStatement.setInt(2, item.getItemId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllItemsInShoppingList() {
        System.out.println("Items in Shopping List (ID: " + shoppingListId + "):");

        try (Connection connection = connect()) {
            String selectItemsQuery = "SELECT i.item_id, i.item_name, i.price FROM Item i " +
                    "JOIN ShoppingListItem sli ON i.item_id = sli.item_id " +
                    "WHERE sli.shopping_list_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectItemsQuery)) {
                preparedStatement.setInt(1, shoppingListId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("No items in this shopping list.");
                    } else {
                        while (resultSet.next()) {
                            int itemId = resultSet.getInt("item_id");
                            String itemName = resultSet.getString("item_name");
                            double price = resultSet.getDouble("price");
                            System.out.println("- Item ID: " + itemId + ", Name: " + itemName + " (Price: $" + price + ")");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findHighestPricedItem() {
        try (Connection connection = connect()) {
            String selectHighestPricedItemQuery = "SELECT i.item_id, i.item_name, i.price " +
                    "FROM Item i " +
                    "JOIN ShoppingListItem sli ON i.item_id = sli.item_id " +
                    "WHERE sli.shopping_list_id = ? " +
                    "ORDER BY i.price DESC " +
                    "LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectHighestPricedItemQuery)) {
                preparedStatement.setInt(1, shoppingListId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        String itemName = resultSet.getString("item_name");
                        double price = resultSet.getDouble("price");
                        System.out.println("Highest Priced Item in Shopping List: ");
                        System.out.println("- Item ID: " + itemId + ", Name: " + itemName + " (Price: $" + price + ")");
                    } else {
                        System.out.println("No items in this shopping list.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findLowestPricedItem() {
        try (Connection connection = connect()) {
            String selectLowestPricedItemQuery = "SELECT i.item_id, i.item_name, i.price " +
                    "FROM Item i " +
                    "JOIN ShoppingListItem sli ON i.item_id = sli.item_id " +
                    "WHERE sli.shopping_list_id = ? " +
                    "ORDER BY i.price ASC " +
                    "LIMIT 1";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectLowestPricedItemQuery)) {
                preparedStatement.setInt(1, shoppingListId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int itemId = resultSet.getInt("item_id");
                        String itemName = resultSet.getString("item_name");
                        double price = resultSet.getDouble("price");
                        System.out.println("Lowest Priced Item in Shopping List: ");
                        System.out.println("- Item ID: " + itemId + ", Name: " + itemName + " (Price: $" + price + ")");
                    } else {
                        System.out.println("No items in this shopping list.");
                    }
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