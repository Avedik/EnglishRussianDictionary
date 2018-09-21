package com.dictionary;

import com.dictionary.controller.*;
import com.dictionary.actions.*;
import com.dictionary.model.*;
import com.dictionary.observers.*;
import java.awt.*;
import javax.swing.*;

public class DictionaryView extends JFrame implements CurrentItemObserver, AllItemsObserver {
    private ModelInterface model;
    private ControllerInterface controller;
    private JTabbedPane tabbedPane;
    private JPanel currentWordPane;
    private JPanel dictionaryPane;
    private JList<DictionaryItem> list;

    public DictionaryView(ModelInterface model, ControllerInterface controller) {
        this.model = model;
        this.controller = controller;
        model.registerObserver((CurrentItemObserver) this);
        model.registerObserver((AllItemsObserver) this);
    }

    public void init() {
        setTitle("Англо-русский словарь");
        initTabbedPane();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        // Display the window
        setVisible(true);
    }

    private void initTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(null, Font.BOLD, 15));
        initCurrentWordPane();
        initDictionaryPane();
    }

    private void initCurrentWordPane() {
        currentWordPane = new JPanel(new GridBagLayout());
        currentWordPane.setBackground(Color.CYAN);
        updateCurrentItem();

        tabbedPane.addTab("Текущее слово", currentWordPane);
        add(tabbedPane);
    }

    private void initDictionaryPane() {
        list = new JList<>();
        dictionaryPane = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.weightx = 1.0; // request any extra horizontal space
        constraints.fill = GridBagConstraints.BOTH;

        JButton addWordButton = new JButton("Добавить слово");
        addWordButton.addActionListener(new AddAction(controller));
        dictionaryPane.add(addWordButton, constraints);

        JButton editButton = new JButton("Редактировать слово");
        editButton.addActionListener(new EditAction(controller));
        constraints.gridx = 1; // second column
        dictionaryPane.add(editButton, constraints);

        JButton removeWordButton = new JButton("Удалить слово");
        removeWordButton.addActionListener(new RemoveAction(controller));
        constraints.gridx = 2;
        dictionaryPane.add(removeWordButton, constraints);

        JButton clearDictionaryButton = new JButton("Очистить словарь");
        clearDictionaryButton.addActionListener(new ClearAction(controller));
        constraints.gridx = 3;
        dictionaryPane.add(clearDictionaryButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1; // second row
        constraints.gridwidth = 4; // 4 columns wide
        constraints.weighty = 1.0; // request any extra vertical space
        constraints.insets = new Insets(10 , 250, 10, 250);
        list.setFont(new Font("Arial", Font.ITALIC,25));

        updateItemsList();
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));

        dictionaryPane.add(scrollPane, constraints);
        tabbedPane.addTab("Словарь", dictionaryPane);

        if (isTheListEmpty())
            disableButtons();
    }

    public void updateCurrentItem() {
        currentWordPane.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        DictionaryItem currentItem = model.getCurrentItem();

        if (currentItem != null) {
            constraints.weighty = 1.0;

            JLabel word = new JLabel(currentItem.getWord());
            word.setFont(new Font(null, Font.PLAIN, 30));
            currentWordPane.add(word, constraints);

            JLabel label = new JLabel(currentItem.getTransliteration());
            label.setFont(new Font("Arial", Font.BOLD, 50));
            constraints.gridy = 1;
            currentWordPane.add(label, constraints);

            JLabel translation = new JLabel(currentItem.getTranslation());
            translation.setFont(new Font(null, Font.PLAIN, 30));
            constraints.gridy = 2;
            currentWordPane.add(translation, constraints);

            constraints.gridy = 3;
            constraints.weighty = 0.0;
            constraints.weightx = 1.0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }

        constraints.ipadx = 40; // the internal padding
        constraints.ipady = 40;
        JButton addWordButton = new JButton("Добавить слово");
        addWordButton.addActionListener(new AddAction(controller));
        currentWordPane.add(addWordButton, constraints);
    }

    public void updateItemsList() {
        list.setListData(model.getAllItems()
                .toArray(new DictionaryItem[0]));
    }

    public void selectCurrentItemTab() {
        tabbedPane.setSelectedIndex(0);
    }

    public boolean isTheListEmpty() {
        return list.getModel().getSize() == 0;
    }

    public void enableButtons() {
        for (int i = 1; i <= 3; i++)
            dictionaryPane.getComponent(i).setEnabled(true);
    }

    public void disableButtons() {
        for (int i = 1; i <= 3; i++)
            dictionaryPane.getComponent(i).setEnabled(false);
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(null,
                "Не удалось запустить программу :(",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
