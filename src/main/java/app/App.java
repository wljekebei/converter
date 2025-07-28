package app;
import service.*;
import model.*;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        TextField amountField = new TextField();
        amountField.setPrefWidth(150);
        amountField.setFont(new Font("System", 18));
        Label result = new Label("result");
        Label exception = new Label("");
        result.setFont(new Font("System", 22));
        exception.setFont(new Font("System", 20));

        ComboBox<String> fromCurrency = new ComboBox<>();
        fromCurrency.setPrefSize(115, 25);
        fromCurrency.setStyle("-fx-font-size: 18px;");
        ComboBox<String> toCurrency = new ComboBox<>();
        toCurrency.setPrefSize(115, 25);
        toCurrency.setStyle("-fx-font-size: 18px;");
        ExchangeRateResponse rates = ExchangeRateService.fetchRates("USD");

        for (String s : rates.conversion_rates.keySet()) {
            fromCurrency.getItems().add(s);
            toCurrency.getItems().addAll(s);
        }

        String [] firstCurrencies = {"UAH", "HUF", "EUR", "USD"};
        moveFirst(fromCurrency.getItems(), firstCurrencies);
        moveFirst(toCurrency.getItems(), firstCurrencies);

        Button button = new Button("Convert!");
        button.setDefaultButton(true);
        button.setPrefSize(135, 20);
        button.setFont(new Font("System", 20));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ExchangeRateResponse currentRates;
                double res = 0;
                try {
                    currentRates = ExchangeRateService.fetchRates(fromCurrency.getValue());
                    res = Conversion.convert(currentRates, toCurrency.getValue(), Double.parseDouble(amountField.getText()));
                    exception.setText("");
                } catch (IOException ex) {
                    exception.setText("Input/Output Exception");
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    exception.setText("API error, try again");
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    exception.setText("Enter the correct number");
                    ex.printStackTrace();
                } catch (NullPointerException ex) {
                    exception.setText("Choose currencies");
                    ex.printStackTrace();
                }
                result.setText(String.format("%.2f %s = %.2f %s", Double.parseDouble(amountField.getText()), fromCurrency.getValue(), res, toCurrency.getValue()));
                if ((String.valueOf((int)res)).length() > 5) result.setFont(new Font("System", 22 - (2.2 * ((String.valueOf((int)res)).length() / 5))));
                else result.setFont(new Font("System", 22));
            }
        });

        Label amount = new Label("Amount:");
        Label from = new Label("From:");
        Label to = new Label("To:");
        amount.setFont(new Font("System", 20));
        from.setFont(new Font("System", 20));
        to.setFont(new Font("System", 20));

        HBox amountBox = new HBox(amount, amountField);
        HBox fromBox = new HBox(from, fromCurrency);
        HBox toBox = new HBox(to, toCurrency);
        VBox root = new VBox(amountBox, fromBox, toBox, button, result, exception);

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

    public static void main(String[] args) {
        launch();
    }

    public static void moveFirst(ObservableList<String> list, String [] strings) {
        for (String s : strings) {
            list.remove(s);
            list.addFirst(s);
        }
    }
}