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

class RangeTowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    private EnemyPath createSimplePath() {

        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0,0));
        points.add(new Waypoint(100,0));
        points.add(new Waypoint(200,0));

        return new EnemyPath(points);
    }

    @Test
    void testConstructor() {

        RangeTower tower = new RangeTower(50,50);

        assertEquals("Range Tower", tower.getName());
        assertEquals(250, tower.getRange());
    }

    @Test
    void testShootCreatesBullet() {

        RangeTower tower = new RangeTower(0,0);

        EnemyPath path = createSimplePath();
        Enemy enemy = new Enemy(path,100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
    }

    @Test
    void testFindTargetReturnsBestEnemy() {

        RangeTower tower = new RangeTower(0,0);

        EnemyPath path = createSimplePath();

        Enemy e1 = new Enemy(path,100,10,1);
        Enemy e2 = new Enemy(path,100,10,1);

        e1.setIndex(0);
        e2.setIndex(1); // อยู่หน้ากว่า

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(e1);
        enemies.add(e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(1, result.size());
        assertEquals(e2, result.get(0));
    }

    @Test
    void testFusionWithZapTower() {

        RangeTower range = new RangeTower(0,0);
        ZapTower zap = new ZapTower(0,0);

        Tower result = range.getFusionResult(zap);

        assertTrue(result instanceof TeslaTower);
    }

    @Test
    void testFusionWithGasTower() {

        RangeTower range = new RangeTower(0,0);
        GasTower gas = new GasTower(0,0);

        Tower result = range.getFusionResult(gas);

        assertTrue(result instanceof TornadoTower);
    }

    @Test
    void testFusionWithIceTower() {

        RangeTower range = new RangeTower(0,0);
        IceTower ice = new IceTower(0,0);

        Tower result = range.getFusionResult(ice);

        assertTrue(result instanceof GlacierTower);
    }

}