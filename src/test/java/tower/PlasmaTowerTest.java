package tower;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlasmaTowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    @Test
    void testConstructorValues() {

        PlasmaTower tower = new PlasmaTower(100,200);

        assertEquals("Plasma Tower", tower.getName());
        assertEquals(75, tower.getRange());
    }

    @Test
    void testShootGasActivatesGas() {

        PlasmaTower tower = new PlasmaTower(0,0);

        tower.shootGas();

        assertTrue(tower.gasActive);
        assertEquals(0, tower.gasRadius);
    }

    @Test
    void testGasRadiusExpands() {

        PlasmaTower tower = new PlasmaTower(0,0);

        tower.shootGas();

        double oldRadius = tower.gasRadius;

        tower.updateGasEffect(0.05);

        assertTrue(tower.gasRadius > oldRadius);
    }

    @Test
    void testDrawGasDoesNotCrash() {

        PlasmaTower tower = new PlasmaTower(100,100);

        tower.shootGas();

        Canvas canvas = new Canvas(300,300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        assertDoesNotThrow(() -> tower.drawGas(gc));
    }

}