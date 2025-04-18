package es.alvarogrlp.marvelsimu.backend.combat.model;

/**
 * Modelo para representar mensajes del combate con metadatos
 */
public class CombatMessage {
    
    // Tipos de mensaje
    public enum MessageType {
        ACTION,      // Acción normal (ataque, etc.)
        RESULT,      // Resultado de una acción (daño, etc.)
        TURN,        // Cambio de turno
        EVASION,     // Ataque evadido
        CRITICAL,    // Golpe crítico
        REDUCTION,   // Daño reducido
        DEFEAT,      // Personaje derrotado
        ABILITY,     // Uso de habilidad
        REGENERATION,// Regeneración de vida
        VICTORY,     // Victoria en combate
        WARNING      // Mensaje de advertencia o error
    }
    
    private String text;
    private MessageType type;
    private boolean isPlayerAction;
    private int relatedValue;
    private String additionalInfo;
    
    /**
     * Constructor para mensajes simples
     */
    public CombatMessage(String text, MessageType type, boolean isPlayerAction) {
        this.text = text;
        this.type = type;
        this.isPlayerAction = isPlayerAction;
        this.relatedValue = 0;
        this.additionalInfo = null;
    }
    
    /**
     * Constructor completo con información adicional
     */
    public CombatMessage(String text, MessageType type, boolean isPlayerAction, int relatedValue, String additionalInfo) {
        this.text = text;
        this.type = type;
        this.isPlayerAction = isPlayerAction;
        this.relatedValue = relatedValue;
        this.additionalInfo = additionalInfo;
    }
    
    /**
     * Crea un mensaje de acción
     */
    public static CombatMessage createActionMessage(String text, boolean isPlayerAction) {
        return new CombatMessage(text, MessageType.ACTION, isPlayerAction);
    }
    
    /**
     * Crea un mensaje de resultado de daño
     */
    public static CombatMessage createDamageMessage(String text, boolean isPlayerAction, int damage) {
        return new CombatMessage(text, MessageType.RESULT, isPlayerAction, damage, null);
    }
    
    /**
     * Crea un mensaje de turno
     */
    public static CombatMessage createTurnMessage(boolean isPlayerTurn) {
        String text = isPlayerTurn ? "¡Tu turno!" : "Turno de la IA";
        return new CombatMessage(text, MessageType.TURN, isPlayerTurn);
    }
    
    /**
     * Crea un mensaje de evasión
     */
    public static CombatMessage createEvasionMessage(String characterName, boolean isPlayerAction) {
        String text = characterName + " evade el ataque";
        return new CombatMessage(text, MessageType.EVASION, isPlayerAction);
    }
    
    /**
     * Crea un mensaje de golpe crítico
     */
    public static CombatMessage createCriticalMessage(String attackName, boolean isPlayerAction, int damage) {
        String text = "¡CRÍTICO! " + attackName;
        return new CombatMessage(text, MessageType.CRITICAL, isPlayerAction, damage, null);
    }
    
    /**
     * Crea un mensaje de reducción de daño
     */
    public static CombatMessage createReductionMessage(String characterName, boolean isPlayerAction) {
        String text = characterName + " reduce el daño recibido";
        return new CombatMessage(text, MessageType.REDUCTION, isPlayerAction);
    }
    
    /**
     * Crea un mensaje de derrota
     */
    public static CombatMessage createDefeatMessage(String characterName, boolean isPlayerAction) {
        String text = characterName + " ha sido derrotado";
        return new CombatMessage(text, MessageType.DEFEAT, isPlayerAction);
    }
    
    /**
     * Crea un mensaje de uso de habilidad
     */
    public static CombatMessage createAbilityMessage(String characterName, String abilityName, boolean isPlayerAction) {
        String text = characterName + " usa " + abilityName;
        return new CombatMessage(text, MessageType.ABILITY, isPlayerAction, 0, abilityName);
    }
    
    /**
     * Crea un mensaje de regeneración
     */
    public static CombatMessage createRegenerationMessage(String characterName, boolean isPlayerAction, int healthRecovered) {
        String text = characterName + " regenera " + healthRecovered + " puntos de vida";
        return new CombatMessage(text, MessageType.REGENERATION, isPlayerAction, healthRecovered, null);
    }
    
    /**
     * Crea un mensaje de victoria
     */
    public static CombatMessage createVictoryMessage(boolean playerWon) {
        String text = playerWon ? "¡Victoria!" : "¡Derrota!";
        return new CombatMessage(text, MessageType.VICTORY, playerWon);
    }
    
    /**
     * Crea un mensaje de advertencia
     */
    public static CombatMessage createWarningMessage(String text, boolean isPlayerAction) {
        return new CombatMessage(text, MessageType.WARNING, isPlayerAction);
    }
    
    // Getters
    
    public String getText() {
        return text;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public boolean isPlayerAction() {
        return isPlayerAction;
    }
    
    public int getRelatedValue() {
        return relatedValue;
    }
    
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    
    @Override
    public String toString() {
        return "CombatMessage{" +
                "text='" + text + '\'' +
                ", type=" + type +
                ", isPlayerAction=" + isPlayerAction +
                ", relatedValue=" + relatedValue +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}