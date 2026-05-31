package ua.knure.icq.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("/ua/knure/icq/client/view/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("ICQ Login");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}