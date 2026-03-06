package path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyPathTest {

    private EnemyPath path;

    @BeforeEach
    void setup() {
        List<Waypoint> waypoints = List.of(
                new Waypoint(0, 0),
                new Waypoint(100, 0),
                new Waypoint(200, 0)
        );

        path = new EnemyPath(waypoints);
    }

    @Test
    void testConstructorRejectsEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> {
            new EnemyPath(List.of());
        });
    }

    @Test
    void testLength() {
        assertEquals(3, path.length());
    }

    @Test
    void testGetWaypoint() {
        Waypoint w = path.getWaypoint(1);

        assertEquals(100, w.getX());
        assertEquals(0, w.getY());
    }

    @Test
    void testGetWaypoints() {
        List<Waypoint> list = path.getWaypoints();

        assertEquals(3, list.size());
    }

    @Test
    void testIsOnPathTrue() {

        // อยู่บนเส้น
        boolean result = path.isOnPath(50, 5);

        assertTrue(result);
    }

    @Test
    void testIsOnPathFalse() {

        // ไกลจาก path
        boolean result = path.isOnPath(50, 200);

        assertFalse(result);
    }

}