package tower;

import bullets.Bullet;
import enemy.Enemy;
import enemy.FlyingEnemy;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;
import path.WaypointTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AOETowerTest {

    static {
        new JFXPanel(); // start JavaFX
    }

    static class TestAOETower extends AOETower {

        public TestAOETower() {
            super(20, 20, 100, 1, 20);
        }

    }

    private EnemyPath createPath() {
        Waypoint waypoint = new Waypoint(0,0);
        List<Waypoint> waypoints = new ArrayList<Waypoint>();
        waypoints.add(waypoint);
        EnemyPath path = new EnemyPath(waypoints);
        path.addWaypoint(new Waypoint(200,200));
        return path;
    }

    @Test
    void testShootGasActivatesGas() {

        TestAOETower tower = new TestAOETower();

        tower.shootGas();

        assertTrue(tower.gasActive);
        assertEquals(0, tower.gasRadius);
        assertEquals(0.5, tower.gasOpacity);
    }

    @Test
    void testGasRadiusExpands() {

        TestAOETower tower = new TestAOETower();
        tower.shootGas();

        double oldRadius = tower.gasRadius;

        tower.updateGasEffect(0.05);

        assertTrue(tower.gasRadius > oldRadius);
    }

    @Test
    void testGasEventuallyStops() {

        TestAOETower tower = new TestAOETower();
        tower.shootGas();

        for (int i = 0; i < 100; i++) {
            tower.updateGasEffect(0.2);
        }

        assertFalse(tower.gasActive);
    }

    @Test
    void testFindTargetIgnoresFlyingEnemy() {

        TestAOETower tower = new TestAOETower();

        EnemyPath path = createPath();

        Enemy groundEnemy = new Enemy(path,100,10,50);
        FlyingEnemy flyingEnemy = new FlyingEnemy(path,100,10,50);

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(groundEnemy);
        enemies.add(flyingEnemy);

        List<Enemy> targets = tower.findTarget(enemies);

        assertTrue(targets.contains(groundEnemy));
        assertFalse(targets.contains(flyingEnemy));
    }

    @Test
    void testShootDamagesEnemy() {

        TestAOETower tower = new TestAOETower();

        EnemyPath path = createPath();
        Enemy enemy = new Enemy(path,100,10,50);

        int hpBefore = enemy.getHp();

        List<Bullet> bullets = new ArrayList<>();

        tower.shoot(enemy, bullets);

        assertTrue(enemy.getHp() < hpBefore);
    }
}