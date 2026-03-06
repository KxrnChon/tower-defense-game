package spell;

import enemy.Enemy;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class XSnapTest {

    // start JavaFX
    static {
        new JFXPanel();
    }

    @Test
    void testConstructor() {

        List<Enemy> enemies = new ArrayList<>();
        Pane root = new Pane();

        XSnap snap = new XSnap(
                enemies,
                root,
                () -> {},
                () -> {}
        );

        assertEquals("X Snap", snap.getName());
        assertEquals(1000, snap.getDamage());
        assertEquals(20, snap.getRange());
    }

    @Test
    void testCastCallsPauseGame() {

        List<Enemy> enemies = new ArrayList<>();
        Pane root = new Pane();

        AtomicBoolean paused = new AtomicBoolean(false);

        XSnap snap = new XSnap(
                enemies,
                root,
                () -> paused.set(true),
                () -> {}
        );

        snap.cast(0,0);

        assertTrue(paused.get());
    }

    @Test
    void testCastDoesNotCrash() {

        List<Enemy> enemies = new ArrayList<>();
        Pane root = new Pane();

        XSnap snap = new XSnap(
                enemies,
                root,
                () -> {},
                () -> {}
        );

        assertDoesNotThrow(() -> snap.cast(10,10));
    }
}