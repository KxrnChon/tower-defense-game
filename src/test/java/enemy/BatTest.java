package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BatTest {
    Waypoint waypoint;
    List<Waypoint> waypoints;
    EnemyPath enemyPath;
    Bat bat;
    @BeforeEach
    void setup() {
        waypoint = new Waypoint(0,0);

        waypoints = new ArrayList<>();
        waypoints.add(waypoint);

        enemyPath = new EnemyPath(waypoints);
        bat = new Bat(enemyPath,10,55,2);
    }
    @Test
    void testConstructor() {
        assertEquals(10,bat.getHp());
        assertEquals(55,bat.getDamage());
        assertEquals(2,bat.getBaseSpeed());
    }
    @Test
    void testInheritance() {
        assertInstanceOf(FlyingEnemy.class,bat);
    }
}
