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

class ZapTowerTest {

    static {
        new JFXPanel();
    }

    private EnemyPath createPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0,0));
        points.add(new Waypoint(100,0));
        points.add(new Waypoint(200,0));
        return new EnemyPath(points);
    }

    @Test
    void testShootCreatesBullet() {

        ZapTower tower = new ZapTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
    }

    @Test
    void testShootTargetCorrect() {

        ZapTower tower = new ZapTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        Bullet bullet = bullets.get(0);

        assertEquals(enemy, bullet.getTarget());
    }

    @Test
    void testFusionWithRangeTower() {

        ZapTower zap = new ZapTower(50,50);
        RangeTower range = new RangeTower(10,10);

        Tower result = zap.getFusionResult(range);

        assertTrue(result instanceof TeslaTower);
        assertEquals(50,result.getX());
        assertEquals(50,result.getY());
    }

    @Test
    void testFusionWithGasTower() {

        ZapTower zap = new ZapTower(50,50);
        GasTower gas = new GasTower(10,10);

        Tower result = zap.getFusionResult(gas);

        assertTrue(result instanceof PlasmaTower);
    }

    @Test
    void testFusionWithIceTower() {

        ZapTower zap = new ZapTower(50,50);
        IceTower ice = new IceTower(10,10);

        Tower result = zap.getFusionResult(ice);

        assertTrue(result instanceof SuperconductTower);
    }

    @Test
    void testFusionInvalidTower() {

        ZapTower zap = new ZapTower(50,50);
        TeslaTower tesla = new TeslaTower(10,10);

        Tower result = zap.getFusionResult(tesla);

        assertNull(result);
    }
}