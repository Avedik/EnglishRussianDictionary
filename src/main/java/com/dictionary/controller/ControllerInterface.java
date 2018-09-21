package com.dictionary.controller;

import java.io.IOException;

public interface ControllerInterface {
    void addNewWord(String word, String translation) throws IOException;
    void editWord(String word, String newTranslation);
    void removeWord(String word);
    void clearDictionary();
}
