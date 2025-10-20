package com.q1_project.application;

import com.q1_project.frontend.CurrencyComboBox;
import com.q1_project.frontend.DoubleInput;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import com.q1_project.backend.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionApplication extends Application {

    //Variables for importing info
    public static ArrayList<String>[] words;
    HashMap<String,Double> currentPriceTransactions;

    //Combo Boxes
    CurrencyComboBox currencyComboBox1;
    CurrencyComboBox currencyComboBox2;
    ObservableList<String> comboList;

    //In and output
    Label outputAmount;
    DoubleInput inputAmount;

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


        inputAmount = new DoubleInput((e)->updateOutput(TransactionApplication.words[1].indexOf(currencyComboBox2.getValue())));
        outputAmount = new Label("input num");

        setUpScreen(scene);

        updateList(words[1].indexOf(currencyComboBox1.getValue()));

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set up the screen and resizes
     * @param scene : screen to set up
     */
    private void setUpScreen(Scene scene){
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
    }

    public void updateList(int in){
        currentPriceTransactions = JsonManager.getDoubleJSONFrom("currencies/"+words[0].get(in)+".json");
        updateOutput(in);
    }

    public void updateOutput(int in){
        outputAmount.setText(Double.toString(Double.parseDouble(inputAmount.getText()) * currentPriceTransactions.get(words[0].get(words[1].indexOf(currencyComboBox2.getValue())))));
    }
}
