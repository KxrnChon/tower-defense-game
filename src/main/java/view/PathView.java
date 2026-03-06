package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import path.EnemyPath;
import path.Waypoint;

import java.util.List;

/**
 * Provides the visual rendering of the enemy navigational route on the game map.
 * * The {@code PathView} class transforms a logical {@link path.EnemyPath} into a
 * series of graphical shapes. It uses a layered stroke technique to create a
 * "Road" effect, consisting of a wide dark outline and a narrower inner core,
 * ensuring the path is clearly visible against the game background.
 */
public class PathView extends Group {

    /**
     * Constructs a PathView based on the provided logical path coordinates.
     * * The rendering process follows a multi-layered approach:
     * 1. Path Construction: Iterates through {@link Waypoint} objects to build
     * {@link MoveTo} and {@link LineTo} commands for the JavaFX Path API.
     * 2. Outline Layer: Renders a wide (40px) black stroke to act as the border
     * of the road.
     * 3. Road Surface Layer: Renders a slightly narrower (30px) beige stroke
     * over the outline to represent the actual walking surface.
     * 4. Connectivity Styling: Uses {@link StrokeLineCap#ROUND} and
     * {@link StrokeLineJoin#ROUND} to ensure smooth transitions at path corners
     * and endpoints.
     *
     * @param path The {@link EnemyPath} containing the logical coordinates to be rendered.
     */
    public PathView(EnemyPath path) {
        List<Waypoint> waypoints = path.getWaypoints();
        if (waypoints.size() < 2) return;

        // JavaFX Paths (visual only)
        Path outline = new Path();
        Path road = new Path();

        Waypoint first = waypoints.getFirst();

        outline.getElements().add(new MoveTo(first.getX(), first.getY()));
        road.getElements().add(new MoveTo(first.getX(), first.getY()));

        for (int i = 1; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);

            outline.getElements().add(new LineTo(wp.getX(), wp.getY()));
            road.getElements().add(new LineTo(wp.getX(), wp.getY()));
        }

        // OUTLINE (behind)
        outline.setStroke(Color.BLACK);
        outline.setStrokeWidth(40);
        outline.setFill(null);
        outline.setStrokeLineCap(StrokeLineCap.ROUND);
        outline.setStrokeLineJoin(StrokeLineJoin.ROUND);

        // ROAD (front)
        road.setStroke(Color.BEIGE);
        road.setStrokeWidth(30);
        road.setFill(null);
        road.setStrokeLineCap(StrokeLineCap.ROUND);
        road.setStrokeLineJoin(StrokeLineJoin.ROUND);

        getChildren().addAll(outline, road);
    }
}
