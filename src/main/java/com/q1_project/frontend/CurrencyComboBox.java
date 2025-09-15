package com.q1_project.frontend;



import com.q1_project.application.TransactionApplication;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;


public class CurrencyComboBox extends ComboBox<String> {
    ObservableList<String> list;
    Consumer<Integer> func;

    public CurrencyComboBox(ObservableList<String> a, Consumer<Integer> f){
        super(a);
        list = a;
        func = f;
        setEditable(true);
        getSelectionModel().selectFirst();
        setOnAction(this::updateOptions);
    }

    //TODO - MAKE THIS WORK lol
    public void updateOptions(ActionEvent e){
        String curVal = getValue();
        int curIn = TransactionApplication.words[1].indexOf(curVal);
        Collections.sort(list, Comparator.comparingInt(o -> o.compareTo(curVal)));
        if(curIn != -1)
            func.accept(curIn);
        else {

        }
    }
}
