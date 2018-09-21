package com.dictionary.model;

import com.dictionary.observers.AllItemsObserver;
import com.dictionary.observers.CurrentItemObserver;
import java.sql.SQLException;
import java.util.List;

public interface ModelInterface {
    void initialize() throws SQLException;
    void addNewDictionaryItem(DictionaryItem item);
    void editItemTranslation(String word, String newTranslation);
    void removeDictionaryItem(String word);
    List<DictionaryItem> getAllItems();
    DictionaryItem getCurrentItem();
    void removeAllItems();

    void registerObserver(CurrentItemObserver o);
    void removeObserver(CurrentItemObserver o);
    void registerObserver(AllItemsObserver o);
    void removeObserver(AllItemsObserver o);
}
