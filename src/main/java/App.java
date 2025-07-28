import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        TextField amountField = new TextField();
        Label label = new Label("Enter amount and choose currencies");
        Label result = new Label("result");

        ComboBox<String> fromCurrency = new ComboBox<>();
        ComboBox<String> toCurrency = new ComboBox<>();
        Response rates = Rates.getRates("USD");

        for (String s : rates.conversion_rates.keySet()) {
            fromCurrency.getItems().add(s);
            toCurrency.getItems().addAll(s);
        }

        Button button = new Button("Convert!");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Response currentRates;
                try {
                    currentRates = Rates.getRates(fromCurrency.getValue());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                double res = Conversion.convert(currentRates, toCurrency.getValue(), Double.parseDouble(amountField.getText()));
                result.setText(String.format("%.2f", res));
            }
        });

        HBox amountBox = new HBox(new Label("Amount:"), amountField);
        HBox fromBox = new HBox(new Label("From:"), fromCurrency);
        HBox toBox = new HBox(new Label("To:"), toCurrency);
        VBox root = new VBox(label, amountBox, fromBox, toBox, button, result);

        amountBox.setSpacing(20);
        fromBox.setSpacing(20);
        toBox.setSpacing(20);
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER);
        amountBox.setAlignment(Pos.CENTER);
        fromBox.setAlignment(Pos.CENTER);
        toBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setTitle("Currency converter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}