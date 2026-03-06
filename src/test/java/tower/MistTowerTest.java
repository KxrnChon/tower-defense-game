package tower;

import bullets.Bullet;
import enemy.Enemy;
import path.EnemyPath;
import path.Waypoint;
import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MistTowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    private Enemy createEnemy() {

        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(0,0));
        waypoints.add(new Waypoint(100,0));

        EnemyPath path = new EnemyPath(waypoints);

        return new Enemy(path,100,10,1);
    }

    @Test
    void testConstructorValues() {

        MistTower tower = new MistTower(50,60);

        assertEquals("Mist Tower", tower.getName());
        assertEquals(75, tower.getRange());
    }

    @Test
    void testShootDamagesEnemy() {

        MistTower tower = new MistTower(0,0);

        Enemy enemy = createEnemy();

        double oldHP = enemy.getHp();

        tower.shoot(enemy, new ArrayList<Bullet>());

        assertTrue(enemy.getHp() < oldHP);
    }

    @Test
    void testDrawGasDoesNotCrash() {

        MistTower tower = new MistTower(100,100);

        tower.shootGas(); // activate gas

        Canvas canvas = new Canvas(300,300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        assertDoesNotThrow(() -> tower.drawGas(gc));
    }
}