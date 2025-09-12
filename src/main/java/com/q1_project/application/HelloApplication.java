package com.q1_project.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import com.q1_project.backend.*;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");

        ArrayList<String>[] words = JsonManager.getJSONFrom("currencies.json");
        System.out.println(JsonManager.getDoubleJSONFrom("currencies/"+words[0].get(112)+".json").get("usd"));
        System.out.println(words[1].get(112));

        ComboBox<String> topMenu = new ComboBox<String>(FXCollections.observableList(words[1]));

        stage.setScene(scene);
        stage.show();
    }
}
