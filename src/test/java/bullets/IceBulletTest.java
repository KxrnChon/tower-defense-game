package bullets;

import enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IceBulletTest {

    Waypoint waypoint;
    List<Waypoint> waypoints;
    EnemyPath enemyPath;
    Enemy enemy;

    @BeforeEach
    void setup() {
        waypoint = new Waypoint(0,0);

        waypoints = new ArrayList<>();
        waypoints.add(waypoint);

        enemyPath = new EnemyPath(waypoints);

        enemy = new Enemy(enemyPath,50,5,1);
    }

    @Test
    void testConstructor() {

        IceBullet bullet = new IceBullet(10,20,enemy,100,10);

        assertEquals(10, bullet.getX());
        assertEquals(20, bullet.getY());
        assertEquals(10, bullet.getDamage());
        assertFalse(bullet.hasHit());
    }

    @Test
    void testTargetNull() {

        IceBullet bullet = new IceBullet(0,0,null,100,10);

        bullet.update(0.016);

        assertTrue(bullet.hasHit());
    }

    @Test
    void testBulletMovesTowardEnemy() {

        IceBullet bullet = new IceBullet(-50,0,enemy,100,10);

        double oldX = bullet.getX();

        bullet.update(0.016);

        assertTrue(bullet.getX() > oldX);
        assertFalse(bullet.hasHit());
    }

    @Test
    void testBulletHitsEnemy() {

        IceBullet bullet = new IceBullet(20,20,enemy,100,10);

        int hpBefore = enemy.getHp();

        for(int i=0;i<20;i++){
            bullet.update(0.016);
        }

        assertTrue(bullet.hasHit());
        assertTrue(enemy.getHp() < hpBefore);
        assertTrue(enemy.isSlowed());
    }
}