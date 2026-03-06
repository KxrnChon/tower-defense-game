package spell;

import enemy.Enemy;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrostBiteTest {

    static {
        new JFXPanel(); // start JavaFX safely
    }
    private Enemy createEnemy(double x, double y) {

        List<Waypoint> w = new ArrayList<>();
        w.add(new Waypoint(x, y));

        EnemyPath path = new EnemyPath(w);

        Enemy e = new Enemy(path, 100, 0, 1);
        e.setPosition(new Point2D(x, y));

        return e;
    }

    @Test
    void testFrostBiteSlowsEnemyInRange() {

        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        Enemy enemy = createEnemy(100,100); // example constructor
        enemies.add(enemy);

        FrostBite frost = new FrostBite(enemies, pane);

        frost.cast(100,100);

        // ตรวจว่า enemy โดน slow
        assertTrue(enemy.isSlowed());
    }

    @Test
    void testFrostBiteDoesNotAffectEnemyOutOfRange() {

        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        Enemy enemy = createEnemy(0,0);
        enemies.add(enemy);

        FrostBite frost = new FrostBite(enemies, pane);

        frost.cast(100,100);

        assertFalse(enemy.isSlowed());
    }

    @Test
    void testEffectAddedToPane() {

        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        FrostBite frost = new FrostBite(enemies, pane);

        frost.cast(100,100);

        assertEquals(1, pane.getChildren().size());
    }
}