package tower;

import bullets.Bullet;
import enemy.Enemy;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeslaTowerTest {

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
    void testFindTargetChoosesFarthestEnemy() {

        TeslaTower tower = new TeslaTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        Enemy e2 = new Enemy(createPath(),100,10,1);

        e1.setIndex(0);
        e2.setIndex(1); // เดินไกลกว่า

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(1,result.size());
        assertEquals(e2,result.get(0));
    }

    @Test
    void testFindTargetIgnoresDeadEnemy() {

        TeslaTower tower = new TeslaTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        e1.takeDamage(999); // ตาย

        Enemy e2 = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertTrue(result.contains(e2));
        assertFalse(result.contains(e1));
    }

    @Test
    void testShootCreatesBullet() {

        TeslaTower tower = new TeslaTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
    }

    @Test
    void testBulletTargetCorrect() {

        TeslaTower tower = new TeslaTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        Bullet bullet = bullets.get(0);

        assertEquals(enemy, bullet.getTarget());
    }

}