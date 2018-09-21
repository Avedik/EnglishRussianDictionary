package com.dictionary.model;

public class DictionaryItem {
    private String word;
    private String transliteration;
    public String translation;

    public DictionaryItem(String word, String transliteration, String translation) {
        this.word = word;
        this.transliteration = transliteration;
        this.translation = translation;
    }

    public String getWord() {

        return word;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DictionaryItem)) return false;

        DictionaryItem item = (DictionaryItem) obj;
        return word.equals(item.word)
                && transliteration.equals(item.transliteration)
                && translation.equals(item.translation);
    }

    @Override
    public int hashCode() {
        return word.hashCode() * transliteration.hashCode() + translation.hashCode();
    }

    @Override
    public String toString() {
        return word + " ["
                + transliteration
                + "] — "
                + translation;
    }
}
