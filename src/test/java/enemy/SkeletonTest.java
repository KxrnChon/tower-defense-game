package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkeletonTest {
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
    void testConstructor() {
        Skeleton skeleton = new Skeleton(enemyPath,10,55,2);
        assertEquals(10,skeleton.getHp());
        assertEquals(55,skeleton.getDamage());
        assertEquals(2,skeleton.getBaseSpeed());
    }
}
