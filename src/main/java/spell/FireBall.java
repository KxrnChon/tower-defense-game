package spell;

import enemy.Enemy;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

/**
 * Represents a concrete implementation of a fire-based offensive spell.
 * * The {@code FireBall} spell deals high area-of-effect (AoE) damage to all enemies
 * within a specific circular radius. It utilizes visual cues and sound effects
 * to provide feedback to the player upon successful casting.
 * @author Pun
 * @version 1.0
 */
public class FireBall extends Spell {

    /**
     * A reference to the global list of active enemies to check for collision.
     */
    private final List<Enemy> enemies;

    /**
     * The visual layer where the explosion effect will be rendered.
     */
    private final Pane gameLayer;

    /**
     * The sound effect associated with the fireball impact, loaded as a static resource
     * for efficiency.
     */
    private static final AudioClip fireballSound;

    static {
        fireballSound = new AudioClip(
                Objects.requireNonNull(FireBall.class.getResource("/sound/fireballSound.mp3")).toExternalForm()
        );
    }

    /**
     * Constructs a FireBall spell with specific damage, range, and environmental references.
     *
     * @param enemies   The current list of enemies present in the game.
     * @param gameLayer The JavaFX Pane used for adding visual animation nodes.
     */
    public FireBall(List<Enemy> enemies, Pane gameLayer) {
        this.name = "Fire Ball";
        this.damage = 50;
        this.range = 80;
        this.enemies = enemies;
        this.gameLayer = gameLayer;
    }

    /**
     * Executes the fireball logic at the target location.
     * * The casting process follows these steps:
     * 1. Proximity Check: Iterates through the enemy list and calculates Euclidean
     * distance to apply damage if they are within the {@code range}.
     * 2. Audio Feedback: Plays the static {@code fireballSound}.
     * 3. Visual Feedback: Renders a temporary semi-transparent {@link Circle} to
     * represent the explosion area.
     * 4. Resource Cleanup: Uses a {@link Timeline} to remove the visual node after
     * a short duration.
     *
     * @param x The horizontal coordinate where the fireball impacts.
     * @param y The vertical coordinate where the fireball impacts.
     */
    @Override
    public void cast(double x, double y) {

        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;

            double dx = e.getPosition().getX() - x;
            double dy = e.getPosition().getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= range) {
                e.takeDamage(damage);
            }
        }

        fireballSound.play();
        Circle explosion = new Circle(x, y, range);
        explosion.setFill(Color.rgb(255, 80, 0, 0.4));
        explosion.setStroke(Color.ORANGE);
        explosion.setStrokeWidth(3);

        gameLayer.getChildren().add(explosion);

        Timeline t = new Timeline(new KeyFrame(Duration.seconds(0.3), _ -> gameLayer.getChildren().remove(explosion)));
        t.play();
    }
}