package path;

import java.util.List;

/**
 * The EnemyPath class defines the navigational route for enemy entities and manages
 * spatial path-validation logic.
 * * This class stores a sequence of Waypoint objects and provides mathematical
 * utility to determine the proximity of game entities to the path segments. It implements
 * a "Point-to-Line Segment Distance" algorithm to facilitate collision detection and
 * placement validation, ensuring that game elements (such as towers) can interact
 * accurately with the defined road.
 *
 * @author Pun
 * @version 1.0
 */
public class EnemyPath {

    /**
     * The ordered collection of Waypoints defining the trajectory of the path.
     */
    private final List<Waypoint> waypoints;

    /**
     * Constructs an EnemyPath with a predefined list of waypoints.
     *
     * @param waypoints A list of {@link Waypoint} objects. Must not be null or empty.
     * @throws IllegalArgumentException if the waypoints list is null or empty.
     */
    public EnemyPath(List<Waypoint> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("Path have no waypoint");
        }
        this.waypoints = waypoints;
    }

    /**
     * Retrieves a specific waypoint based on its index in the sequence.
     *
     * @param index The position of the waypoint in the list.
     * @return The {@link Waypoint} at the specified index.
     */
    public Waypoint getWaypoint(int index) {
        return waypoints.get(index);
    }

    /**
     * Returns the total number of waypoints in the path.
     *
     * @return The size of the waypoint list.
     */
    public int length() {
        return waypoints.size();
    }

    /**
     * Retrieves the entire list of waypoints defining the path.
     *
     * @return A {@link List} of all {@link Waypoint} objects.
     */
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    /**
     * Checks if a specific coordinate (x, y) is within a defined proximity to the path.
     * * This method iterates through all segments of the path and calculates the
     * distance from the given point to each segment using a tolerance threshold.
     *
     * @param x The X-coordinate to check.
     * @param y The Y-coordinate to check.
     * @return {@code true} if the point is within the tolerance distance of any
     * path segment; {@code false} otherwise.
     */
    public boolean isOnPath(double x, double y) {

        double tolerance = 40;

        for (int i = 0; i < waypoints.size() - 1; i++) {

            Waypoint p1 = waypoints.get(i);
            Waypoint p2 = waypoints.get(i + 1);

            double distance = distanceToSegment(
                    x, y,
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY()
            );

            if (distance <= tolerance) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the shortest Euclidean distance between a point (px, py) and a
     * line segment defined by two endpoints (x1, y1) and (x2, y2).
     * * The algorithm uses scalar projection to find the closest point on the
     * segment, clamping the projection parameter 't' between 0 and 1 to
     * handle points beyond the segment's endpoints.
     *
     * @param px X-coordinate of the point.
     * @param py Y-coordinate of the point.
     * @param x1 X-coordinate of the first segment endpoint.
     * @param y1 Y-coordinate of the first segment endpoint.
     * @param x2 X-coordinate of the second segment endpoint.
     * @param y2 Y-coordinate of the second segment endpoint.
     * @return The minimum distance from the point to the line segment.
     */
    private double distanceToSegment(
            double px, double py,
            double x1, double y1,
            double x2, double y2) {

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0)
            return Math.hypot(px - x1, py - y1);

        double t = ((px - x1) * dx + (py - y1) * dy)
                / (dx * dx + dy * dy);

        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        return Math.hypot(px - closestX, py - closestY);
    }

    /**
     * Appends a new waypoint to the end of the existing path sequence.
     *
     * @param waypoint The {@link Waypoint} to be added.
     */
    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
    }
}
