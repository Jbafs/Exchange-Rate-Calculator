package com.q1_project.frontend;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;


import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class DoubleInput extends TextField {

    //Variables for filtering input

    Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?E?([1-9][0-9]*)?");

    UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        if (validEditingState.matcher(text).matches())
            return c ;
        else
            return null;
    };

    StringConverter<Double> converter = new StringConverter<>() {
        @Override
        public Double fromString(String s) {
            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s) || s.equals("Infinity")) return 0.0 ;
            else return Double.valueOf(s);
        }
        @Override
        public String toString(Double d) {
            return d.toString();
        }
    };

    /**
     * Constructor to set filter
     */
    public DoubleInput(EventHandler<ActionEvent> e){
        TextFormatter<Double> onlyDoubles = new TextFormatter<>(converter, 0.0, filter);
        setTextFormatter(onlyDoubles);
        setOnAction(e);
    }

}
