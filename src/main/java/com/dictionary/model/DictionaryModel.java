package com.dictionary.model;

import com.dictionary.observers.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class DictionaryModel implements ModelInterface {
    private static final String URL_DB = "jdbc:sqlite:dictionary.db3";
    private final List<CurrentItemObserver> itemObservers = new ArrayList<>();
    private final List<AllItemsObserver> itemsObservers = new ArrayList<>();
    private Connection connection;
    private DictionaryItem currentItem;

    public void initialize() throws SQLException {
        connection = DriverManager.getConnection(URL_DB);
    }

    public void addNewDictionaryItem(DictionaryItem newItem) {
        // Create a prepared statement to avoid SQL injections - attacks on the database
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO dictionary VALUES (?, ?, ?)")) {

            statement.setString(1, newItem.getWord());
            statement.setString(2, newItem.getTransliteration());
            statement.setString(3, newItem.getTranslation());
            // Execute the query
            statement.execute();
            currentItem = newItem;
            // Notify about a model state changing
            notifyCurrentItemObservers();
            notifyAllItemsObservers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Такое слово уже имеется в словаре.");
        }
    }

    public void editItemTranslation(String word, String newTranslation) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE dictionary SET translation = ? WHERE word = ?")) {

            statement.setString(1, newTranslation);
            statement.setString(2, word);
            statement.execute();

            if (currentItem != null
                    && currentItem.getWord().equals(word))
            {
                currentItem.setTranslation(newTranslation);
            }

            notifyCurrentItemObservers();
            notifyAllItemsObservers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDictionaryItem(String word) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM dictionary WHERE word = ?")) {

            statement.setString(1, word);
            // Execute the query to the DB
            statement.execute();
            if (currentItem != null && currentItem.getWord().equals(word))
                currentItem = null;

            notifyCurrentItemObservers();
            notifyAllItemsObservers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DictionaryItem> getAllItems() {
        // Use Statement to execute an sql query
        try (Statement statement = connection.createStatement()) {
            // Dictionary items list
            List<DictionaryItem> items = new ArrayList<>();

            // The resultSet stores query result
            // which executes by the command statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dictionary");

            while (resultSet.next()) {
                items.add(new DictionaryItem(resultSet.getString("word"),
                                            resultSet.getString("transliteration"),
                                            resultSet.getString("translation")));
            }

            return items;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public DictionaryItem getCurrentItem() {
        return currentItem;
    }

    public void removeAllItems() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM dictionary");

            currentItem = null;
            notifyCurrentItemObservers();
            notifyAllItemsObservers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerObserver(CurrentItemObserver o) {
        itemObservers.add(o);
    }

    public void removeObserver(CurrentItemObserver o) {
        int i = itemObservers.indexOf(o);
        if (i >= 0) {
            itemObservers.remove(i);
        }
    }

    public void registerObserver(AllItemsObserver o) {
        itemsObservers.add(o);
    }

    public void removeObserver(AllItemsObserver o) {
        int i = itemsObservers.indexOf(o);
        if (i >= 0) {
            itemsObservers.remove(i);
        }
    }

    // Notify observers
    private void notifyCurrentItemObservers() {
        for (CurrentItemObserver observer : itemObservers)
            observer.updateCurrentItem();
    }

    private void notifyAllItemsObservers() {
        for (AllItemsObserver observer : itemsObservers)
            observer.updateItemsList();
    }
}
