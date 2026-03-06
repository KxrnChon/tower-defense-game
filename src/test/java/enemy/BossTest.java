package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BossTest {
    Waypoint waypoint;
    List<Waypoint> waypoints;
    EnemyPath enemyPath;
    Boss boss;
    @BeforeEach
    void setup() {
        waypoint = new Waypoint(0,0);

        waypoints = new ArrayList<>();
        waypoints.add(waypoint);

        enemyPath = new EnemyPath(waypoints);
        boss = new Boss(enemyPath,10,55,2);
    }
    @Test
    void testConstructor() {
        assertEquals(10, boss.getHp());
        assertEquals(55, boss.getDamage());
        assertEquals(2, boss.getBaseSpeed());
    }
}
