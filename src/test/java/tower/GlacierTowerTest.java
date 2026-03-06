package tower;

import bullets.Bullet;
import enemy.Enemy;
import path.EnemyPath;
import path.Waypoint;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlacierTowerTest {

    private EnemyPath createPath() {
        List<Waypoint> waypoints = new ArrayList<>();
        waypoints.add(new Waypoint(0,0));
        waypoints.add(new Waypoint(100,0));
        waypoints.add(new Waypoint(200,0));

        return new EnemyPath(waypoints);
    }

    @Test
    void testFindTargetSelectsHighestIndexEnemy() {

        GlacierTower tower = new GlacierTower(0,0);

        EnemyPath path = createPath();

        Enemy e1 = new Enemy(path,100,10,1);
        Enemy e2 = new Enemy(path,100,10,1);

        e1.setIndex(0);
        e2.setIndex(2);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(1,result.size());
        assertEquals(e2,result.get(0));
    }

    @Test
    void testFindTargetChoosesClosestToNextWaypointWhenSameIndex() {

        GlacierTower tower = new GlacierTower(0,0);

        EnemyPath path = createPath();

        Enemy e1 = new Enemy(path,100,10,1);
        Enemy e2 = new Enemy(path,100,10,1);

        e1.setIndex(1);
        e2.setIndex(1);

        e1.setPosition(new Point2D(10,0));
        e2.setPosition(new Point2D(90,0)); // closer to waypoint (100,0)

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(e2,result.get(0));
    }

    @Test
    void testShootCreatesIceBullet() {

        GlacierTower tower = new GlacierTower(0,0);

        EnemyPath path = createPath();
        Enemy enemy = new Enemy(path,100,10,1);

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertEquals(1, bullets.size());
        assertEquals("IceBullet", bullets.get(0).getClass().getSimpleName());
    }
}