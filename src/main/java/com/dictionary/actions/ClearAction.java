package com.dictionary.actions;

import com.dictionary.controller.ControllerInterface;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class ClearAction extends AbstractAction {
    private ControllerInterface controller;

    public ClearAction(ControllerInterface controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.clearDictionary();
    }
}
