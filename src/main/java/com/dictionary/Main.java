package com.dictionary;

import com.dictionary.controller.Controller;
import com.dictionary.model.*;

public class Main {
    public static void main(String[] args) {
        ModelInterface model = new DictionaryModel();
        new Controller(model);
    }
}
