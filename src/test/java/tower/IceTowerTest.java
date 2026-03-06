package tower;

import bullets.Bullet;
import enemy.Enemy;
import path.EnemyPath;
import path.Waypoint;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IceTowerTest {

    private Enemy createEnemy() {

        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(0,0));
        waypoints.add(new Waypoint(100,0));

        EnemyPath path = new EnemyPath(waypoints);

        return new Enemy(path,100,10,1);
    }

    @Test
    void testShootCreatesIceBullet() {

        IceTower tower = new IceTower(50,50);
        Enemy enemy = createEnemy();

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
        assertEquals("IceBullet", bullets.get(0).getClass().getSimpleName());
    }

    @Test
    void testFusionWithZapTower() {

        IceTower ice = new IceTower(100,100);
        ZapTower zap = new ZapTower(0,0);

        Tower result = ice.getFusionResult(zap);

        assertTrue(result instanceof SuperconductTower);
    }

    @Test
    void testFusionWithRangeTower() {

        IceTower ice = new IceTower(100,100);
        RangeTower range = new RangeTower(0,0);

        Tower result = ice.getFusionResult(range);

        assertTrue(result instanceof GlacierTower);
    }

    @Test
    void testFusionWithGasTower() {

        IceTower ice = new IceTower(100,100);
        GasTower gas = new GasTower(0,0);

        Tower result = ice.getFusionResult(gas);

        assertTrue(result instanceof MistTower);
    }

    @Test
    void testFusionWithUnknownTowerReturnsNull() {

        IceTower ice = new IceTower(100,100);
        Tower other = new GlacierTower(0,0);

        Tower result = ice.getFusionResult(other);

        assertNull(result);
    }
}