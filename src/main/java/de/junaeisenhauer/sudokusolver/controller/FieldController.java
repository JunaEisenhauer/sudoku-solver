package de.junaeisenhauer.sudokusolver.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * The field controller handles a single field of the sudoku grid.
 */
public class FieldController {

    @FXML
    private TextField number;

    @FXML
    public void initialize() {
        number.setTextFormatter(new TextFormatter<String>(change -> {
            // only one character in field
            if (!number.getText().isEmpty() && !change.getText().isEmpty()) {
                return null;
            }

            // only numbers from 1-9
            if (change.getText().matches("[1-9]*")) {
                return change;
            }

            return null;
        }));
    }

    int getValue() {
        if (number.getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(number.getText());
    }

    void setValue(int value) {
        if (value == 0) {
            number.setText("");
        } else {
            number.setText(value + "");
        }
    }

    void lock() {
        number.setEditable(false);
    }

    void unlock() {
        number.setEditable(true);
    }

}
