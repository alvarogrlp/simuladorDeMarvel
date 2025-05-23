package es.alvarogrlp.marvelsimu.backend.controller;

import java.sql.SQLException;

import es.alvarogrlp.marvelsimu.PrincipalApplication;
import es.alvarogrlp.marvelsimu.backend.config.ConfigManager;
import es.alvarogrlp.marvelsimu.backend.model.UsuarioModel;
import es.alvarogrlp.marvelsimu.backend.controller.abstracts.AbstractController;
import es.alvarogrlp.marvelsimu.backend.config.ThemeManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegistroController extends AbstractController {
    
    @FXML
    private Text textUsuario;
    @FXML
    private TextField textFiledUsuario;
    @FXML
    private Text textEmail;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Text textContrasenia;
    @FXML
    PasswordField textFieldPassword;
    @FXML
    private Text textRepetirContrasenia;
    @FXML
    private PasswordField textFieldPasswordRepit;
    @FXML
    private Text textMensaje;
    @FXML
    private Button onRegistrarButton;
    @FXML
    private Button onVolverButton;

    /**
     * Inicializa el controlador de registro.
     * Configura los textos y los placeholders de los campos según el idioma seleccionado.
     */
    @FXML
    public void initialize() {
        // Aplicar el tema actual utilizando el método unificado del AbstractController
        applyCurrentTheme(textFiledUsuario, null, null);
        
        // Configurar los textos según el idioma
        setIdioma();
    }

    /**
     * Maneja el evento de clic en el botón de registro.
     * Valida los datos ingresados por el usuario y registra un nuevo usuario si los datos son válidos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    @FXML
    protected void onClickRegistar() throws SQLException {
        if (textFieldPassword == null || textFieldPassword.getText().isEmpty()
                || textFieldPasswordRepit == null || textFieldPasswordRepit.getText().isEmpty()) {
            textMensaje.setText("❌ " + ConfigManager.ConfigProperties.getProperty("mensajePasswordVacio") + " ❌");
            textMensaje.setStyle("-fx-fill: red;"); // Cambiar el color a rojo
            return;
        }

        if (textFieldPassword.getText().equals(textFieldPasswordRepit.getText())) {
            textMensaje.setText("✅ " + ConfigManager.ConfigProperties.getProperty("mensajePasswordCorrecto") + " ✅");
            textMensaje.setStyle("-fx-fill: green;"); // Cambiar el color a verde
        }

        UsuarioModel usuarioNuevo = new UsuarioModel(textFieldEmail.getText(), textFiledUsuario.getText(),
                textFieldPassword.getText());

        if (!getUsuarioServiceModel().agregarUsuario(usuarioNuevo)) {
            textMensaje.setText("❌ " + ConfigManager.ConfigProperties.getProperty("mensajeUsuarioYaRegistrado") + " ❌");
            textMensaje.setStyle("-fx-fill: red;"); // Cambiar el color a rojo
            return;
        } else {
            textMensaje.setText("✅ " + ConfigManager.ConfigProperties.getProperty("mensajeUsuarioRegistrado") + " ✅");
            textMensaje.setStyle("-fx-fill: green;"); // Cambiar el color a verde
            openVolverClick();
            return;
        }
    }

    /**
     * Abre la pantalla de login.
     * Cambia la escena actual a la pantalla de login.
     */
    @FXML
    protected void openVolverClick() {
        abrirVentana(onVolverButton, "login.fxml");
    }

    @FXML
    protected void setIdioma() {
        textUsuario.setText(ConfigManager.ConfigProperties.getProperty("textUsuario"));
        textEmail.setText(ConfigManager.ConfigProperties.getProperty("textEmail"));
        textContrasenia.setText(ConfigManager.ConfigProperties.getProperty("textContrasenia"));
        textRepetirContrasenia.setText(ConfigManager.ConfigProperties.getProperty("textRepetirContrasenia"));
        onRegistrarButton.setText(ConfigManager.ConfigProperties.getProperty("onRegistrarButton"));
        onVolverButton.setText(ConfigManager.ConfigProperties.getProperty("onVolverButton"));
        
        textFiledUsuario.setPromptText(ConfigManager.ConfigProperties.getProperty("promptUsuario"));
        textFieldEmail.setPromptText(ConfigManager.ConfigProperties.getProperty("promptEmail"));
        textFieldPassword.setPromptText(ConfigManager.ConfigProperties.getProperty("promptContrasenia"));
        textFieldPasswordRepit.setPromptText(ConfigManager.ConfigProperties.getProperty("promptRepetirContrasenia"));
    }
}