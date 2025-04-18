package es.alvarogrlp.marvelsimu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import es.alvarogrlp.marvelsimu.backend.config.ConfigManager;

import java.io.IOException;

public class PrincipalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar las propiedades si no existen
        if (ConfigManager.ConfigProperties.getProperty("theme") == null) {
            ConfigManager.ConfigProperties.setProperty("theme", "dark");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 410, 810);
        stage.setTitle("Pantalla Principal");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}