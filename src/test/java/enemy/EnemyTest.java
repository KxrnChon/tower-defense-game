package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EnemyTest {

    Waypoint waypoint;
    List<Waypoint> waypoints;
    EnemyPath enemyPath;
    @BeforeEach
    void setup() {
        waypoint = new Waypoint(0,0);

        waypoints = new ArrayList<>();
        waypoints.add(waypoint);

        enemyPath = new EnemyPath(waypoints);

    }

    @Test
    void TestConstructor() {
        Enemy newEnemy = new Enemy(enemyPath,20,5,0);

        assertEquals(20,newEnemy.getHp());
        assertEquals(5,newEnemy.getDamage());
        assertEquals(0,newEnemy.getBaseSpeed());
    }
    @Test
    void TestNegativeConstructor() {
        Enemy newEnemy = new Enemy(enemyPath,-20,-5,-50);

        assertEquals(0,newEnemy.getHp());
        assertEquals(0,newEnemy.getDamage());
        assertEquals(0,newEnemy.getBaseSpeed());
    }
    @Test
    void testTakeDamageReducesHP() {
        Enemy enemy = new Enemy(enemyPath,20,5,10);
        enemy.takeDamage(5);
        assertEquals(15, enemy.getHp());
    }

    @Test
    void testEnemyDiesWhenHPZero() {
        Enemy enemy = new Enemy(enemyPath,20,5,10);
        enemy.takeDamage(10000);
        assertFalse(enemy.isAlive());
    }
}