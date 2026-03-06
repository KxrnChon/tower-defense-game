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

public class FireBallTest {
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
    void testEnemyInsideRangeTakesDamage() {

        Enemy enemy = createEnemy(10, 10);

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);

        Pane pane = new Pane();

        FireBall fireBall = new FireBall(enemies, pane);

        fireBall.cast(0, 0);

        assertTrue(enemy.getHp() < 100);
    }

    @Test
    void testEnemyOutsideRangeNotDamaged() {

        Enemy enemy = createEnemy(500, 500);

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);

        Pane pane = new Pane();

        FireBall fireBall = new FireBall(enemies, pane);

        fireBall.cast(0, 0);

        assertEquals(100, enemy.getHp());
    }

    @Test
    void testDeadEnemyNotAffected() {

        Enemy enemy = createEnemy(10, 10);
        enemy.takeDamage(200); // ทำให้ตาย

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);

        Pane pane = new Pane();

        FireBall fireBall = new FireBall(enemies, pane);

        fireBall.cast(0, 0);

        assertFalse(enemy.isAlive());
    }

    @Test
    void testExplosionAddedToPane() {

        Enemy enemy = createEnemy(10, 10);

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);

        Pane pane = new Pane();

        FireBall fireBall = new FireBall(enemies, pane);

        fireBall.cast(0, 0);

        assertEquals(1, pane.getChildren().size());
    }
}