package es.alvarogrlp.marvelsimu.backend.combat.ui;

import java.io.InputStream;
import java.util.List;

import es.alvarogrlp.marvelsimu.backend.combat.logic.CombatManager;
import es.alvarogrlp.marvelsimu.backend.model.PersonajeModel;
import es.alvarogrlp.marvelsimu.backend.util.AlertUtils;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class CombatUIManager {
    
    private AnchorPane rootPane;
    
    // UI elements references
    private ImageView playerCharacterImage;
    private ImageView aiCharacterImage;
    private Label playerHealthLabel;
    private Label aiHealthLabel;
    private ProgressBar playerHealthBar;
    private ProgressBar aiHealthBar;
    private Button attackButton;
    private Button changeButton;
    private Button backButton;
    private HBox attackContainer;
    private Button meleeAttackButton;
    private Button rangedAttackButton;
    private Button ability1Button;
    private Button ability2Button;
    private Label turnIndicator;
    private VBox playerTeamContainer;
    private VBox aiTeamContainer;
    
    // Character selection dialog
    private CharacterSelectionDialog selectionDialog;
    
    public CombatUIManager(AnchorPane rootPane) {
        this.rootPane = rootPane;
        initializeReferences();
        loadBackground();
    }
    
    private void initializeReferences() {
        try {
            playerCharacterImage = (ImageView) rootPane.lookup("#imgPersonajeJugador");
            aiCharacterImage = (ImageView) rootPane.lookup("#imgPersonajeIA");
            playerHealthLabel = (Label) rootPane.lookup("#lblVidaJugador");
            aiHealthLabel = (Label) rootPane.lookup("#lblVidaIA");
            playerHealthBar = (ProgressBar) rootPane.lookup("#barraVidaJugador");
            aiHealthBar = (ProgressBar) rootPane.lookup("#barraVidaIA");
            attackButton = (Button) rootPane.lookup("#btnAtacar");
            changeButton = (Button) rootPane.lookup("#btnCambiar");
            backButton = (Button) rootPane.lookup("#btnVolver");
            attackContainer = (HBox) rootPane.lookup("#contenedorAtaques");
            meleeAttackButton = (Button) rootPane.lookup("#btnAtaqueMelee");
            rangedAttackButton = (Button) rootPane.lookup("#btnAtaqueLejano");
            ability1Button = (Button) rootPane.lookup("#btnHabilidad1");
            ability2Button = (Button) rootPane.lookup("#btnHabilidad2");
            turnIndicator = (Label) rootPane.lookup("#lblTurno");
            playerTeamContainer = (VBox) rootPane.lookup("#equipoJugador");
            aiTeamContainer = (VBox) rootPane.lookup("#equipoIA");
            
            // Verificar que todos los elementos se han encontrado
            if (playerCharacterImage == null || aiCharacterImage == null ||
                playerHealthLabel == null || aiHealthLabel == null || 
                playerHealthBar == null || aiHealthBar == null ||
                attackButton == null || changeButton == null || backButton == null ||
                attackContainer == null || meleeAttackButton == null || 
                rangedAttackButton == null || ability1Button == null || ability2Button == null ||
                turnIndicator == null || playerTeamContainer == null || aiTeamContainer == null) {
                
                // Imprimir qué elementos son nulos para depuración
                System.err.println("Elementos nulos:");
                if (playerCharacterImage == null) System.err.println("- playerCharacterImage");
                if (aiCharacterImage == null) System.err.println("- aiCharacterImage");
                if (playerHealthLabel == null) System.err.println("- playerHealthLabel");
                if (aiHealthLabel == null) System.err.println("- aiHealthLabel");
                if (playerHealthBar == null) System.err.println("- playerHealthBar");
                if (aiHealthBar == null) System.err.println("- aiHealthBar");
                if (attackButton == null) System.err.println("- attackButton");
                if (changeButton == null) System.err.println("- changeButton");
                if (backButton == null) System.err.println("- backButton");
                if (attackContainer == null) System.err.println("- attackContainer");
                if (meleeAttackButton == null) System.err.println("- meleeAttackButton");
                if (rangedAttackButton == null) System.err.println("- rangedAttackButton");
                if (ability1Button == null) System.err.println("- ability1Button");
                if (ability2Button == null) System.err.println("- ability2Button");
                if (turnIndicator == null) System.err.println("- turnIndicator");
                if (playerTeamContainer == null) System.err.println("- playerTeamContainer");
                if (aiTeamContainer == null) System.err.println("- aiTeamContainer");
                
                throw new RuntimeException("No se pudieron localizar todos los elementos de la UI");
            }
            
            // Inicializar el diálogo de selección
            selectionDialog = new CharacterSelectionDialog(rootPane);
            
        } catch (Exception e) {
            System.err.println("Error inicializando referencias UI: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.mostrarError("Error", "No se pudo inicializar la interfaz de combate");
        }
    }
    
    private void loadBackground() {
        try {
            ImageView backgroundImage = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("images/Fondos/limbo.png")));
            backgroundImage.setFitWidth(896);
            backgroundImage.setFitHeight(810);
            backgroundImage.setPreserveRatio(false);
            
            rootPane.getChildren().add(0, backgroundImage);
        } catch (Exception e) {
            System.err.println("Error cargando fondo: " + e.getMessage());
        }
    }
    
    public void updateCharacterViews(
        PersonajeModel playerCharacter, 
        PersonajeModel aiCharacter, 
        List<PersonajeModel> playerTeam,
        List<PersonajeModel> aiTeam,
        int playerIndex,
        int aiIndex) {
        
        // Cargar imágenes basadas directamente en las rutas de los personajes
        Image playerImage = loadImageFromMultipleSources(playerCharacter.getImagenCombate());
        Image aiImage = loadImageFromMultipleSources(aiCharacter.getImagenCombate());
        
        // Configurar las imágenes
        playerCharacterImage.setImage(playerImage);
        playerCharacterImage.setScaleX(1);
        
        aiCharacterImage.setImage(aiImage);
        aiCharacterImage.setScaleX(-1);
        
        // Actualizar barras de vida
        updateCharacterHealth(playerCharacter, aiCharacter);
        
        // Actualizar miniaturas del equipo
        updateTeamThumbnails(playerTeam, aiTeam, playerIndex, aiIndex);
        
        // AÑADIR: Actualizar los botones de ataque con los nombres específicos
        updateAttackButtons(playerCharacter);
    }
    
    private String mapCharacterCodeToFile(String nombreCodigo) {
        if (nombreCodigo.contains("hulk")) return "hulk";
        if (nombreCodigo.contains("spider")) return "spiderman";
        if (nombreCodigo.contains("iron")) return "ironman";
        if (nombreCodigo.contains("cap") || nombreCodigo.contains("amer")) return "captainamerica";
        if (nombreCodigo.contains("strange") || nombreCodigo.contains("doctor")) return "doctorstrange";
        if (nombreCodigo.contains("magik")) return "magik";
        
        System.err.println("No se pudo mapear el código: " + nombreCodigo);
        return "default";
    }
    
    /**
     * Intenta cargar una imagen desde múltiples fuentes posibles
     */
    private Image loadImageFromMultipleSources(String imagePath) {
        // Si la ruta es nula, salir temprano
        if (imagePath == null || imagePath.isEmpty()) {
            return new WritableImage(100, 100);
        }
        
        try {
            // Intento 1: Cargar directamente con ClassLoader
            InputStream is = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (is != null) {
                return new Image(is);
            }
            
            // Intento 2: Probar con ruta relativa desde recursos
            is = getClass().getResourceAsStream("/" + imagePath);
            if (is != null) {
                return new Image(is);
            }
            
            // Intento 3: Intentar con un nombre de archivo directo basado en personajes conocidos
            String filename = imagePath.contains("/") ? 
                         imagePath.substring(imagePath.lastIndexOf('/') + 1) : 
                         imagePath;
                         
            // Construir rutas específicas basadas en el nombre del archivo
            String characterCode = null;
            if (filename.contains("hulk")) {
                characterCode = "hulk";
            } else if (filename.contains("spider") || filename.contains("spiderman")) {
                characterCode = "spiderman";
            } else if (filename.contains("iron") || filename.contains("ironman")) {
                characterCode = "ironman";
            } else if (filename.contains("capt") || filename.contains("america")) {
                characterCode = "captainamerica";
            } else if (filename.contains("strange") || filename.contains("doctor")) {
                characterCode = "doctorstrange";
            } else if (filename.contains("magik")) {
                characterCode = "magik";
            }
            
            if (characterCode != null) {
                // Intentar con imágenes de combate
                is = getClass().getClassLoader().getResourceAsStream("images/Ingame/" + characterCode + "-ingame.png");
                if (is != null) {
                    return new Image(is);
                }
                
                // Intentar con miniaturas
                is = getClass().getClassLoader().getResourceAsStream("images/Personajes/" + characterCode + ".png");
                if (is != null) {
                    return new Image(is);
                }
            }
            
            // Si todo falla, una imagen vacía
            return new WritableImage(100, 100);
        } catch (Exception e) {
            return new WritableImage(100, 100);
        }
    }
    
    public void updateCharacterHealth(PersonajeModel playerCharacter, PersonajeModel aiCharacter) {
        // Obtener referencias a los labels de nombre
        Label lblNombreJugador = (Label) rootPane.lookup("#lblNombreJugador");
        Label lblNombreIA = (Label) rootPane.lookup("#lblNombreIA");
        
        // Actualizar nombres
        if (lblNombreJugador != null) lblNombreJugador.setText(playerCharacter.getNombre());
        if (lblNombreIA != null) lblNombreIA.setText(aiCharacter.getNombre());
        
        // Obtener valores de vida
        int playerHealth = playerCharacter.getVidaActual();
        int playerMaxHealth = playerCharacter.getVida();
        int aiHealth = aiCharacter.getVidaActual();
        int aiMaxHealth = aiCharacter.getVida();
        
        // Calcular porcentajes
        double playerHealthPercent = (double) playerHealth / playerMaxHealth;
        double aiHealthPercent = (double) aiHealth / aiMaxHealth;
        
        // Actualizar textos de vida con formato mejorado
        playerHealthLabel.setText(String.format("%d / %d", playerHealth, playerMaxHealth));
        aiHealthLabel.setText(String.format("%d / %d", aiHealth, aiMaxHealth));
        
        // Actualizar barras de progreso con transición suave
        playerHealthBar.setProgress(playerHealthPercent);
        aiHealthBar.setProgress(aiHealthPercent);
        
        // Aplicar estilos para vida crítica (menos del 25%)
        if (playerHealthPercent < 0.25) {
            playerHealthBar.getStyleClass().add("hp-critical");
        } else {
            playerHealthBar.getStyleClass().removeAll("hp-critical");
        }
        
        if (aiHealthPercent < 0.25) {
            aiHealthBar.getStyleClass().add("hp-critical");
        } else {
            aiHealthBar.getStyleClass().removeAll("hp-critical");
        }
        
        // Opcional: Añadir animación sutil de parpadeo cuando la vida es crítica
        if (playerHealthPercent < 0.25) {
            addPulseEffect(playerHealthLabel);
        } else {
            removePulseEffect(playerHealthLabel);
        }
        
        if (aiHealthPercent < 0.25) {
            addPulseEffect(aiHealthLabel);
        } else {
            removePulseEffect(aiHealthLabel);
        }
    }
    
    // Método auxiliar para añadir efecto de pulsación
    private void addPulseEffect(Label label) {
        if (!label.getStyleClass().contains("pulse-effect")) {
            label.getStyleClass().add("pulse-effect");
            
            // Crear efecto de pulsación con opacidad
            FadeTransition fade = new FadeTransition(Duration.millis(700), label);
            fade.setFromValue(1.0);
            fade.setToValue(0.6);
            fade.setCycleCount(javafx.animation.Animation.INDEFINITE);
            fade.setAutoReverse(true);
            fade.play();
            
            // Guardar la animación en el userData para poder detenerla después
            label.setUserData(fade);
        }
    }
    
    // Método auxiliar para quitar efecto de pulsación
    private void removePulseEffect(Label label) {
        if (label.getStyleClass().contains("pulse-effect")) {
            label.getStyleClass().remove("pulse-effect");
            
            // Detener la animación si existe
            if (label.getUserData() instanceof FadeTransition) {
                FadeTransition fade = (FadeTransition) label.getUserData();
                fade.stop();
                label.setOpacity(1.0);
            }
        }
    }
    
    public void updateTeamThumbnails(
            List<PersonajeModel> playerTeam, 
            List<PersonajeModel> aiTeam, 
            int playerIndex, 
            int aiIndex) {
        
        playerTeamContainer.getChildren().clear();
        aiTeamContainer.getChildren().clear();
        
        // Miniaturas equipo jugador
        for (int i = 0; i < playerTeam.size(); i++) {
            PersonajeModel character = playerTeam.get(i);
            ImageView thumbnail = new ImageView(loadImageFromMultipleSources(character.getImagenMiniatura()));
            thumbnail.setFitWidth(50);
            thumbnail.setFitHeight(50);
            thumbnail.setPreserveRatio(true);
            
            VBox container = new VBox(thumbnail);
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("team-character");
            
            if (i == playerIndex) {
                container.getStyleClass().add("team-character-active");
            }
            
            if (character.isDerrotado()) {
                container.getStyleClass().add("character-defeated");
            }
            
            playerTeamContainer.getChildren().add(container);
        }
        
        // Miniaturas equipo IA
        for (int i = 0; i < aiTeam.size(); i++) {
            PersonajeModel character = aiTeam.get(i);
            ImageView thumbnail = new ImageView(loadImageFromMultipleSources(character.getImagenMiniatura()));
            thumbnail.setFitWidth(50);
            thumbnail.setFitHeight(50);
            thumbnail.setPreserveRatio(true);
            
            VBox container = new VBox(thumbnail);
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("enemy-team-character");
            
            if (i == aiIndex) {
                container.getStyleClass().add("enemy-character-active");
            }
            
            if (character.isDerrotado()) {
                container.getStyleClass().add("character-defeated");
            }
            
            aiTeamContainer.getChildren().add(container);
        }
    }
    
    /**
     * Actualiza los botones de ataques con los nombres correctos y valores del personaje actual
     */
    public void updateAttackButtons(PersonajeModel character) {
        // Configurar el botón de ataque melee con el nombre real
        if (meleeAttackButton != null) {
            meleeAttackButton.setText(character.getAtaqueMeleeNombre());
            meleeAttackButton.setTooltip(new Tooltip("Daño: " + character.getAtaqueMelee() + 
                                              "\nTipo: " + formatTipoAtaque(character.getAtaqueMeleeTipo())));
        }
        
        // Configurar el botón de ataque a distancia con el nombre real
        if (rangedAttackButton != null) {
            rangedAttackButton.setText(character.getAtaqueLejanoNombre());
            rangedAttackButton.setTooltip(new Tooltip("Daño: " + character.getAtaqueLejano() + 
                                                "\nTipo: " + formatTipoAtaque(character.getAtaqueLejanoTipo())));
        }
        
        // Configurar el botón de la primera habilidad con el nombre real
        if (ability1Button != null) {
            ability1Button.setText(character.getHabilidad1Nombre());
            int usosRestantes = character.getUsosRestantes("habilidad1");
            ability1Button.setTooltip(new Tooltip("Daño: " + character.getHabilidad1Poder() + 
                                           "\nTipo: " + formatTipoAtaque(character.getHabilidad1Tipo()) + 
                                           "\nUsos: " + usosRestantes));
            ability1Button.setDisable(usosRestantes <= 0);
        }
        
        // Configurar el botón de la segunda habilidad con el nombre real
        if (ability2Button != null) {
            ability2Button.setText(character.getHabilidad2Nombre());
            int usosRestantes = character.getUsosRestantes("habilidad2");
            ability2Button.setTooltip(new Tooltip("Daño: " + character.getHabilidad2Poder() + 
                                           "\nTipo: " + formatTipoAtaque(character.getHabilidad2Tipo()) + 
                                           "\nUsos: " + usosRestantes));
            ability2Button.setDisable(usosRestantes <= 0);
        }
    }
    
    /**
     * Formatea el tipo de ataque para mostrarlo más legible
     */
    private String formatTipoAtaque(String tipo) {
        if (tipo == null) return "Físico";
        
        switch (tipo.toLowerCase()) {
            case "fisico": return "Físico";
            case "magico": return "Mágico";
            case "fisico_penetrante": return "Físico Penetrante";
            case "magico_penetrante": return "Mágico Penetrante";
            case "daño_verdadero": return "Daño Verdadero";
            default: return tipo;
        }
    }
    
    public void showCharacterSelection(CombatManager manager) {
        selectionDialog.showDialog(manager.getPlayerCharacters(), manager.getPlayerCharacterIndex(), index -> {
            manager.changePlayerCharacter(index);
        });
    }
    
    public void showForceCharacterSelection(CombatManager manager) {
        selectionDialog.showForceDialog(manager.getPlayerCharacters(), manager.getPlayerCharacterIndex(), index -> {
            manager.changePlayerCharacter(index);
        });
    }
    
    public void showCombatEndScreen(boolean victory) {
        Rectangle darkBackground = new Rectangle(0, 0, rootPane.getWidth(), rootPane.getHeight());
        darkBackground.setFill(Color.rgb(0, 0, 0, 0.7));
        
        VBox endContainer = new VBox(30);
        endContainer.setAlignment(Pos.CENTER);
        
        Text endText = new Text(victory ? "¡VICTORIA!" : "¡DERROTA!");
        endText.setFont(Font.font("System", FontWeight.BOLD, 72));
        endText.setFill(victory ? Color.GOLD : Color.RED);
        endText.setTextAlignment(TextAlignment.CENTER);
        endText.setStroke(Color.BLACK);
        endText.setStrokeWidth(2);
        
        Button btnReturnToSelection = new Button("Volver a Selección de Personajes");
        btnReturnToSelection.getStyleClass().add("action-button");
        btnReturnToSelection.setPrefWidth(300);
        btnReturnToSelection.setOnAction(e -> {
            // Navegar a la pantalla de selección
            navigateToCharacterSelection();
        });
        
        endContainer.getChildren().addAll(endText, btnReturnToSelection);
        endContainer.setOpacity(0);
        
        rootPane.getChildren().addAll(darkBackground, endContainer);
        
        darkBackground.toFront();
        endContainer.toFront();
        
        double centerX = rootPane.getWidth() / 2;
        double centerY = rootPane.getHeight() / 2;
        endContainer.setLayoutX(centerX - 300);
        endContainer.setLayoutY(centerY - 150);
        
        FadeTransition fadeInBackground = new FadeTransition(Duration.millis(500), darkBackground);
        fadeInBackground.setFromValue(0);
        fadeInBackground.setToValue(1);
        
        FadeTransition fadeInContainer = new FadeTransition(Duration.millis(800), endContainer);
        fadeInContainer.setFromValue(0);
        fadeInContainer.setToValue(1);
        
        ScaleTransition pulse = new ScaleTransition(Duration.millis(800), endText);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        
        ParallelTransition entryAnimation = new ParallelTransition(fadeInBackground, fadeInContainer);
        entryAnimation.setOnFinished(e -> pulse.play());
        entryAnimation.play();
    }
    
    private void navigateToCharacterSelection() {
        try {
            // Crear una instancia del navegador y navegar a la página de selección
            // Esto es una simplificación - implementa la lógica real de navegación según tu estructura
            backButton.fire(); // Usa el botón existente para navegar
        } catch (Exception e) {
            System.err.println("Error navegando a selección de personajes: " + e.getMessage());
            e.printStackTrace();
            AlertUtils.mostrarError("Error", "No se pudo volver a la pantalla de selección");
        }
    }
    
    public void hidePlayerCharacter() {
        playerCharacterImage.setVisible(false);
    }
    
    public void showPlayerCharacter() {
        playerCharacterImage.setVisible(true);
    }
    
    public void hideAICharacter() {
        aiCharacterImage.setVisible(false);
    }
    
    public void showAICharacter() {
        aiCharacterImage.setVisible(true);
    }
    
    public void setPlayerTurnIndicator(boolean isPlayerTurn) {
        turnIndicator.setText(isPlayerTurn ? "TU TURNO" : "TURNO DE IA");
        
        if (isPlayerTurn) {
            turnIndicator.getStyleClass().remove("enemy-turn");
            turnIndicator.getStyleClass().add("player-turn");
        } else {
            turnIndicator.getStyleClass().remove("player-turn");
            turnIndicator.getStyleClass().add("enemy-turn");
        }
    }
    
    /**
     * Deshabilita todos los controles del jugador durante el turno de la IA
     */
    public void disablePlayerControls() {
        // Ocultar el contenedor de ataques si está visible
        attackContainer.setVisible(false);
        
        // Deshabilitar todos los botones de acción
        attackButton.setDisable(true);
        changeButton.setDisable(true);
        backButton.setDisable(true);
        
        // También deshabilitar los botones de ataque específicos para evitar cualquier interacción
        meleeAttackButton.setDisable(true);
        rangedAttackButton.setDisable(true);
        ability1Button.setDisable(true);
        ability2Button.setDisable(true);
    }
    
    /**
     * Habilita los controles principales del jugador durante su turno
     */
    public void enablePlayerControls() {
        // Forzar actualización en el hilo de UI
        Platform.runLater(() -> {
            // Habilitar los botones principales
            attackButton.setDisable(false);
            changeButton.setDisable(false);
            backButton.setDisable(false);
            
            // Mantener los botones de ataque deshabilitados hasta que se presione el botón de atacar
            attackContainer.setVisible(false);
            
            // Asegurarnos de que todos los botones de ataque están listos para usarse
            meleeAttackButton.setDisable(false);
            rangedAttackButton.setDisable(false);
            
            // Actualizar estado de botones de habilidad según disponibilidad
            PersonajeModel playerCharacter = getCurrentPlayerCharacter();
            if (playerCharacter != null) {
                ability1Button.setDisable(!playerCharacter.tieneUsosDisponibles("habilidad1"));
                ability2Button.setDisable(!playerCharacter.tieneUsosDisponibles("habilidad2"));
            }
            
            // Log para diagnosticar
            System.out.println("Controles del jugador habilitados");
        });
    }
    
    // Método para obtener el personaje actual del jugador
    private PersonajeModel getCurrentPlayerCharacter() {
        // Si tenemos acceso directo a través de CombatManager, úsalo
        if (rootPane.getUserData() instanceof CombatManager) {
            CombatManager manager = (CombatManager) rootPane.getUserData();
            return manager.getCurrentPlayerCharacter();
        }
        
        // Si no, buscamos en el rootPane si se almacenó como userData directamente
        if (rootPane.getUserData() instanceof PersonajeModel) {
            return (PersonajeModel) rootPane.getUserData();
        }
        
        return null;
    }
    
    /**
     * Muestra las opciones de ataque y prepara los botones
     */
    public void showAttackOptions() {
        // Primero verificar que es el turno del jugador
        if (rootPane.getUserData() instanceof CombatManager) {
            CombatManager manager = (CombatManager) rootPane.getUserData();
            if (!manager.getTurnManager().isPlayerTurn()) {
                return; // No mostrar opciones si no es el turno del jugador
            }
        }
        
        attackContainer.setVisible(true);
        attackButton.setDisable(true);
        changeButton.setDisable(true);
        backButton.setDisable(true);
        
        // Asegurarnos de habilitar los botones de ataque
        meleeAttackButton.setDisable(false);
        rangedAttackButton.setDisable(false);
        
        // Los botones de habilidad se actualizan según disponibilidad
        PersonajeModel playerCharacter = getCurrentPlayerCharacter();
        if (playerCharacter != null) {
            ability1Button.setDisable(!playerCharacter.tieneUsosDisponibles("habilidad1"));
            ability2Button.setDisable(!playerCharacter.tieneUsosDisponibles("habilidad2"));
        }
    }
    
    // Getters para acceder a los botones y conectarlos con los event handlers
    public Button getAttackButton() {
        return attackButton;
    }
    
    public Button getChangeButton() {
        return changeButton;
    }
    
    public Button getBackButton() {
        return backButton;
    }
    
    public Button getMeleeAttackButton() {
        return meleeAttackButton;
    }
    
    public Button getRangedAttackButton() {
        return rangedAttackButton;
    }
    
    public Button getAbility1Button() {
        return ability1Button;
    }
    
    public Button getAbility2Button() {
        return ability2Button;
    }
}