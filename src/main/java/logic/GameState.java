package logic;

/**
 * The GameState enum defines the primary operational states of the game application.
 * * This enumeration is used by the central {@link GameLogic} and UI controllers
 * to manage transitions between active gameplay and end-game scenarios. By centralizing
 * these states, the system can efficiently toggle game loop updates, input processing,
 * and UI overlays.
 *
 * @author Pun
 * @version 1.0
 */
public enum GameState {

    /** * Indicates that the game is currently in progress.
     * In this state, the game loop updates entities, processes player input,
     * and advances the waves.
     */
    PLAYING,

    /** * Indicates that the game has concluded, typically due to the player's
     * base health reaching zero.
     * In this state, normal game updates are halted, and the game over
     * screen/results are displayed.
     */
    GAME_OVER
}