package tower;

import enemy.Enemy;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TornadoTowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    private EnemyPath createPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0,0));
        points.add(new Waypoint(100,0));
        points.add(new Waypoint(200,0));
        return new EnemyPath(points);
    }

    @Test
    void testFindTargetReturnsEnemiesInRange() {

        TornadoTower tower = new TornadoTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        Enemy e2 = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertTrue(result.contains(e1));
        assertTrue(result.contains(e2));
    }

    @Test
    void testFindTargetIgnoresDeadEnemy() {

        TornadoTower tower = new TornadoTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        e1.takeDamage(999); // ตาย

        Enemy e2 = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertFalse(result.contains(e1));
        assertTrue(result.contains(e2));
    }

    @Test
    void testFindTargetReturnsMultipleEnemies() {

        TornadoTower tower = new TornadoTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        Enemy e2 = new Enemy(createPath(),100,10,1);
        Enemy e3 = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2,e3);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(3, result.size());
    }
}