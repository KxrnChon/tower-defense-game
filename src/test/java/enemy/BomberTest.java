package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BomberTest {
    Waypoint waypoint;
    List<Waypoint> waypoints;
    EnemyPath enemyPath;
    Bomber bomber;
    @BeforeEach
    void setup() {
        waypoint = new Waypoint(0,0);

        waypoints = new ArrayList<>();
        waypoints.add(waypoint);

        enemyPath = new EnemyPath(waypoints);
        bomber = new Bomber(enemyPath,10,55,2);
    }
    @Test
    void testConstructor() {
        assertEquals(10,bomber.getHp());
        assertEquals(55,bomber.getDamage());
        assertEquals(2,bomber.getBaseSpeed());
    }
}
