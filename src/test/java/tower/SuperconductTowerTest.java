package tower;

import bullets.Bullet;
import bullets.IceBullet;
import enemy.Enemy;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuperconductTowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    private EnemyPath createPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0,0));
        points.add(new Waypoint(100,0));
        return new EnemyPath(points);
    }

    @Test
    void testConstructor() {

        SuperconductTower tower = new SuperconductTower(50,50);

        assertEquals(180, tower.getRange());
        assertEquals(0.4, tower.getCooldown());
    }

    @Test
    void testShootCreatesIceBullet() {

        SuperconductTower tower = new SuperconductTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
        assertTrue(bullets.get(0) instanceof IceBullet);
    }

    @Test
    void testBulletTargetsCorrectEnemy() {

        SuperconductTower tower = new SuperconductTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        Bullet bullet = bullets.get(0);

        assertEquals(enemy, bullet.getTarget());
    }
}