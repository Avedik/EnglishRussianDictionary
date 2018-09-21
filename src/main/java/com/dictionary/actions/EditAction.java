package com.dictionary.actions;

import com.dictionary.controller.ControllerInterface;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class EditAction extends WordAction {
    private ControllerInterface controller;

    public EditAction(ControllerInterface controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        int optionIndex = showInputDialog(event.getActionCommand());
        if (isCancelled(optionIndex)) return;

        String word = textField.getText();
        textField.setText(null);

        try {
            showTranslation(word);

            optionIndex = showInputDialog("Перевод");
            if (isCancelled(optionIndex)) return;

            String translation = textField.getText();
            textField.setText(null);

            controller.editWord(word, translation);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
