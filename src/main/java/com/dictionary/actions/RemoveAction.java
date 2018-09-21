package com.dictionary.actions;

import com.dictionary.controller.ControllerInterface;
import java.awt.event.ActionEvent;

public class RemoveAction extends WordAction {
    private ControllerInterface controller;

    public RemoveAction(ControllerInterface controller) {
         this.controller = controller;
     }

    public void actionPerformed(ActionEvent event) {
        int optionIndex = showInputDialog(event.getActionCommand());
        if (isCancelled(optionIndex)) return;

        controller.removeWord(textField.getText());
        textField.setText(null);
    }
}
