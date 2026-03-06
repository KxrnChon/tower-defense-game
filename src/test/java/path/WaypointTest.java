package path;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaypointTest {

    @Test
    void testConstructorSetsPosition() {

        Waypoint w = new Waypoint(10, 20);

        assertEquals(10, w.getX());
        assertEquals(20, w.getY());
    }

    @Test
    void testGetPosition() {

        Waypoint w = new Waypoint(5, 7);

        Point2D pos = w.getPosition();

        assertEquals(5, pos.getX());
        assertEquals(7, pos.getY());
    }

    @Test
    void testSetPosition() {

        Waypoint w = new Waypoint(0, 0);

        w.setPosition(new Point2D(50, 80));

        assertEquals(50, w.getX());
        assertEquals(80, w.getY());
    }

    @Test
    void testGetX() {

        Waypoint w = new Waypoint(15, 25);

        assertEquals(15, w.getX());
    }

    @Test
    void testGetY() {

        Waypoint w = new Waypoint(15, 25);

        assertEquals(25, w.getY());
    }
}