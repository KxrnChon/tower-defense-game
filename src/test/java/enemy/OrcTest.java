package enemy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrcTest {
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
    void testOrcConstructor() {
        Orc newOrc = new Orc(enemyPath,20,-5,0);

        assertEquals(20,newOrc.getHp());
        assertEquals(0,newOrc.getDamage());
        assertEquals(0,newOrc.getBaseSpeed());
        assertTrue(newOrc.hasArmor());
        assertEquals(20,newOrc.getArmor());
    }
    @Test
    void testOrcArmorBreak() {
        Orc newOrc = new Orc(enemyPath,20,-5,0);
        newOrc.takeDamage(20);
        assertFalse(newOrc.hasArmor());
    }
}
