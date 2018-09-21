package com.dictionary.actions;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

abstract class WordAction extends AbstractAction {
    final JTextField textField;
    private JButton okay;
    private JButton cancel;

    WordAction() {
        textField = new JTextField();
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent event) {
                if (event.getDocument().getLength() > 0)
                    okay.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent event) {
                if (event.getDocument().getLength() == 0)
                    okay.setEnabled(false);
            }

            public void changedUpdate(DocumentEvent event) {}
        });

        okay = new JButton("Выполнить");
        okay.addActionListener(createActionListener(okay));
        okay.setEnabled(false);

        cancel = new JButton("Отмена");
        cancel.addActionListener(createActionListener(cancel));
    }

    static void showTranslation(String word) throws IOException {
        try {
            // Открыть браузер по умолчанию
            Desktop.getDesktop().browse(new URI("https://translate.yandex.ru/?lang=en-ru&text=" + word));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Входная строка не является словом."); // в строке есть пробел(ы)
        }
    }

    static boolean isCancelled(int input) {
        if (input != 0) {
            JOptionPane.showMessageDialog(null,
                    "Действие было отменено пользователем.",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private ActionListener createActionListener(Object value) {
        return event -> {
            Component component = (Component) event.getSource();
            while (!(component instanceof JOptionPane))
                component = component.getParent();

            ((JOptionPane) component).setValue(value);
        };
    }

    int showInputDialog(String title) {
        return JOptionPane.showOptionDialog(null,
                textField,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new JButton[]{okay, cancel},
                okay);
    }
}
