package com.q1_project.application;

import com.q1_project.frontend.CurrencyComboBox;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import com.q1_project.backend.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TransactionApplication extends Application {
    public static ArrayList<String>[] words;

    HashMap<String,Double> currentPriceTransactions;

    CurrencyComboBox currencyComboBox1;
    CurrencyComboBox currencyComboBox2;
    ObservableList<String> comboList;

    Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

    UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        if (validEditingState.matcher(text).matches()) {
            return c ;
        } else {
            return null ;
        }
    };

    StringConverter<Double> converter = new StringConverter<>() {

        @Override
        public Double fromString(String s) {
            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                return 0.0 ;
            } else {
                return Double.valueOf(s);
            }
        }


        @Override
        public String toString(Double d) {
            return d.toString();
        }
    };

    Label outputAmount;
    TextField inputAmount;



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TransactionApplication.class.getResource("transaction-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Exchange Rate Calculator");

        words = JsonManager.getJSONFrom("currencies.json");
        if(words==null)return;

        comboList = FXCollections.observableList(words[1]);
        currencyComboBox1 = new CurrencyComboBox(comboList,this::updateList);
        currencyComboBox2 = new CurrencyComboBox(comboList,this::updateOutput);

        TextFormatter<Double> onlyDoubles = new TextFormatter<>(converter, 0.0, filter);
        inputAmount = new TextField();
        inputAmount.setTextFormatter(onlyDoubles);
        inputAmount.setOnAction((e)->updateOutput(TransactionApplication.words[1].indexOf(currencyComboBox2.getValue())));
        outputAmount = new Label("input num");

        GridPane root = (GridPane)scene.getRoot();

        ColumnConstraints resizeConstraint = new ColumnConstraints();
        resizeConstraint.setPercentWidth(50);
        RowConstraints resizeConstraint2 = new RowConstraints();
        resizeConstraint2.setPercentHeight(50);
        root.getColumnConstraints().add(resizeConstraint);
        root.getRowConstraints().add(resizeConstraint2);

        root.add(currencyComboBox1,0,0);
        root.add(currencyComboBox2,0,3);
        root.add(inputAmount,0,1);
        root.add(outputAmount,0,2);

        stage.setScene(scene);
        stage.show();
    }

    public void updateList(int in){
        currentPriceTransactions = JsonManager.getDoubleJSONFrom("currencies/"+words[0].get(in)+".json");
        updateOutput(in);
    }

    public void updateOutput(int in){
        outputAmount.setText(Double.toString(Double.parseDouble(inputAmount.getText()) * currentPriceTransactions.get(words[0].get(in))));
    }
}
