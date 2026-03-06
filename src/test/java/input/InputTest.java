package input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InputTest {

    @BeforeEach
    void setup() {
        Input.reset();
        Input.update();
    }

    @Test
    void testKeyPressed() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.A,
                false,false,false,false
        );

        Input.onKeyPressed(event);

        assertTrue(Input.isPressed(KeyCode.A));
    }

    @Test
    void testUpdateClearsPressed() {
        KeyEvent event = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                KeyCode.B,
                false,false,false,false
        );

        Input.onKeyPressed(event);
        Input.update();

        assertFalse(Input.isPressed(KeyCode.B));
    }

    @Test
    void testMouseLeftClick() {
        Input.onMousePressed(MouseButton.PRIMARY);

        assertTrue(Input.isLeftClick());

        Input.update();

        assertFalse(Input.isLeftClick());
    }

    @Test
    void testMouseRightClick() {
        Input.onMousePressed(MouseButton.SECONDARY);

        assertTrue(Input.isRightClick());

        Input.update();

        assertFalse(Input.isRightClick());
    }

    @Test
    void testMousePosition() {
        Input.setMousePosition(100,200);

        assertEquals(100, Input.getMouseX());
        assertEquals(200, Input.getMouseY());
    }

    @Test
    void testMouseRelease() {
        Input.onMousePressed(MouseButton.PRIMARY);
        Input.onMouseReleased(MouseButton.PRIMARY);

        Input.update();

        assertFalse(Input.isLeftClick());
    }
}