package path;

import javafx.geometry.Point2D;

/**
 * Represents a specific spatial coordinate on the game map used to define
 * enemy navigation paths.
 * * Each Waypoint acts as a node in a linear sequence that enemies follow.
 * By utilizing the {@link Point2D} class, it provides high-precision
 * floating-point coordinates necessary for smooth movement interpolation.
 * @author Pun
 * @version 1.0
 */
public class Waypoint {

    /**
     * The 2D spatial coordinate of this waypoint.
     */
    private Point2D position;

    /**
     * Constructs a new Waypoint at the specified X and Y coordinates.
     *
     * @param x The horizontal position on the game map.
     * @param y The vertical position on the game map.
     */
    public Waypoint(double x, double y) {
        setPosition(new Point2D(x,y));
    }

    /**
     * Retrieves the position of this waypoint as a Point2D object.
     *
     * @return The {@link Point2D} representation of this coordinate.
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Updates the position of this waypoint.
     *
     * @param position The new {@link Point2D} coordinate.
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * Helper method to retrieve the X-coordinate directly.
     *
     * @return The double value of the X-coordinate.
     */
    public double getX() {
        return position.getX();
    }

    /**
     * Helper method to retrieve the Y-coordinate directly.
     *
     * @return The double value of the Y-coordinate.
     */
    public double getY(){
        return position.getY();
    }
}
