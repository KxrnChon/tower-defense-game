package input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * The Input class acts as a global manager for keyboard and mouse states.
 * * It stores and manages the lifecycle of input events, differentiating between
 * continuous states (held down) and discrete events (just pressed/released).
 * This design prevents multiple triggers for a single action and ensures
 * synchronized input handling across the game update cycle.
 *
 * @author Pun
 * @version 1.0
 */
public class Input {

    /** Tracks keys that are currently being held down. */
    private static final Set<KeyCode> keysDown = new HashSet<>();

    /** Tracks keys that were pressed during the current frame interval. */
    private static final Set<KeyCode> keysPressed = new HashSet<>();

    /** Tracks keys that were released during the current frame interval. */
    private static final Set<KeyCode> keysReleased = new HashSet<>();
    private static boolean leftPressed = false;
    private static boolean leftJustPressed = false;
    private static boolean rightPressed = false;
    private static boolean rightJustPressed = false;

    private static double mouseX;
    private static double mouseY;

    /**
     * Processes key press events and updates internal state buffers.
     * Marks a key as "pressed" only if it was not already being held in
     * the previous frame.
     *
     * @param e The KeyEvent captured from the JavaFX scene
     */
    public static void onKeyPressed(KeyEvent e) {
        if (!keysDown.contains(e.getCode())) {
            keysPressed.add(e.getCode()); // JUST pressed
        }
        keysDown.add(e.getCode());
    }

    /**
     * Processes key release events and removes them from the active set.
     *
     * @param e The KeyEvent captured from the JavaFX scene
     */
    public static void onKeyReleased(KeyEvent e) {
        keysDown.remove(e.getCode());
        keysReleased.add(e.getCode());
    }

    /**
     * Checks if a specific key was pressed during the current frame.
     *
     * @param key The {@link KeyCode} to check
     * @return true if the key transition to pressed occurred this frame
     */
    public static boolean isPressed(KeyCode key) {
        return keysPressed.contains(key);
    }

    /**
     * Clears the discrete event buffers (pressed/released states).
     * This should be called at the end of every frame to reset transient
     * input states.
     */
    public static void update() {
        keysPressed.clear();
        keysReleased.clear();
        leftJustPressed = false;
        rightJustPressed = false;
    }

    /**
     * Updates the global mouse coordinates.
     *
     * @param x The horizontal coordinate in the game window
     * @param y The vertical coordinate in the game window
     */
    public static void setMousePosition(double x, double y){
        mouseX = x;
        mouseY = y;
    }

    /** @return The current X-coordinate of the mouse. */
    public static double getMouseX() {
        return mouseX;
    }

    /** @return The current Y-coordinate of the mouse. */
    public static double getMouseY() {
        return mouseY;
    }

    /** Resets all key buffers, typically used when changing scenes or focus. */
    public static void reset() {
        keysPressed.clear();
        keysDown.clear();
    }

    /**
     * Processes mouse button press events.
     * Tracks both primary (left) and secondary (right) clicks while
     * identifying the initial frame of the click.
     *
     * @param button The MouseButton type that was pressed
     */
    public static void onMousePressed(javafx.scene.input.MouseButton button) {
        if (button == javafx.scene.input.MouseButton.PRIMARY) {
            if (!leftPressed) leftJustPressed = true;
            leftPressed = true;
        }

        if (button == javafx.scene.input.MouseButton.SECONDARY) {
            if (!rightPressed) rightJustPressed = true;
            rightPressed = true;
        }
    }

    /**
     * Processes mouse button release events and resets button state.
     *
     * @param button The MouseButton type that was released
     */
    public static void onMouseReleased(javafx.scene.input.MouseButton button) {
        if (button == javafx.scene.input.MouseButton.PRIMARY) {
            leftPressed = false;
        }

        if (button == javafx.scene.input.MouseButton.SECONDARY) {
            rightPressed = false;
        }
    }

    /** @return true if the left mouse button was clicked in the current frame. */
    public static boolean isLeftClick() {
        return leftJustPressed;
    }

    /** @return true if the right mouse button was clicked in the current frame. */
    public static boolean isRightClick() {
        return rightJustPressed;
    }
}
