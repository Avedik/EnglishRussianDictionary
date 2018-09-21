package com.dictionary.controller;

import com.dictionary.model.*;
import com.dictionary.DictionaryView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.sql.SQLException;

public class Controller implements ControllerInterface{
    private ModelInterface model;
    private DictionaryView view;

    public Controller(ModelInterface model) {
        this.model = model;
        view = new DictionaryView(model, this);
        try {
            model.initialize();
            view.init();
        } catch (SQLException e) {
            e.printStackTrace();
            exit();
        }
    }

    public void addNewWord(String word, String translation) throws IOException {
        DictionaryItem item = new DictionaryItem(word, transliterate(word), translation);

        model.addNewDictionaryItem(item);
        view.enableButtons();
        view.selectCurrentItemTab();
    }

    private String transliterate(String word) throws IOException {
        Document doc = Jsoup.connect("https://tophonetics.com/ru")
                .data("native", "checked")  // selection of checkbox "Транскрипция русскими буквами"
                .data("text_to_transcribe", word) // insert a word in the text area
                .post();  // executes a query on transliteration

        String transliteration = doc.select("span.transcribed_word").text();
        if (transliteration.isEmpty())
            throw new UnsupportedOperationException("не удалось выполнить транслитерацию слова " + word);

        return transliteration;
    }


    public void editWord(String word, String newTranslation) {
        model.editItemTranslation(word, newTranslation);
    }

    public void removeWord(String word) {
        model.removeDictionaryItem(word);
        if (view.isTheListEmpty())
            view.disableButtons();
    }

    public void clearDictionary() {
        model.removeAllItems();
        view.disableButtons();
    }

    private void exit() {
        view.showErrorMessage();
        System.exit(1);
    }
}
