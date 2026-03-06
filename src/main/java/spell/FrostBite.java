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
 * Represents a crowd-control spell that inflicts a significant slow effect on enemies.
 * * The {@code FrostBite} spell is a tactical ability that deals zero direct damage
 * but drastically reduces the movement speed of all enemies within its radius for
 * a sustained duration. This is essential for managing large waves and giving
 * towers more time to attack.
 * @author Pun
 * @version 1.0
 */
public class FrostBite extends Spell {

    /**
     * A reference to the global list of active enemies to check for collision.
     */
    private final List<Enemy> enemies;

    /**
     * The visual layer where the frost effect will be rendered.
     */
    private final Pane gameLayer;

    /**
     * The sound effect associated with the frost impact, loaded as a static resource.
     */
    private static final AudioClip frostBiteSound;

    static {
        frostBiteSound = new AudioClip(
                Objects.requireNonNull(FrostBite.class.getResource("/sound/frostBiteSound.mp3")).toExternalForm()
        );
    }

    /**
     * Constructs a FrostBite spell with a large range and a specialized slow attribute.
     *
     * @param enemies   The current list of enemies present in the game.
     * @param gameLayer The JavaFX Pane used for adding visual animation nodes.
     */
    public FrostBite(List<Enemy> enemies, Pane gameLayer) {
        this.name = "Frost Bite";
        this.damage = 0;
        this.range = 90;
        this.enemies = enemies;
        this.gameLayer = gameLayer;
        this.special = "Slow 80% 10 Sec";
    }

    /**
     * Executes the frost logic at the target location.
     * * The casting process involves:
     * 1. Euclidean Range Check: Identifies enemies within the circular cast radius.
     * 2. Status Effect Application: Triggers the {@code applySlow} method on target
     * enemies, reducing their speed by 80% for 10 seconds.
     * 3. Audio Feedback: Plays the frost impact sound.
     * 4. Visual Feedback: Renders a temporary light-blue semi-transparent {@link Circle}
     * representing the frozen zone, which is removed via a {@link Timeline}.
     *
     * @param x The horizontal coordinate where the frost zone is created.
     * @param y The vertical coordinate where the frost zone is created.
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
                double slowDuration = 10;
                double slowFactor = 0.8;
                e.applySlow(slowFactor, slowDuration);
            }
        }

        Circle frost = new Circle(x, y, range);
        frost.setFill(Color.rgb(150, 220, 255, 0.35));
        frost.setStroke(Color.LIGHTBLUE);
        frost.setStrokeWidth(3);

        frostBiteSound.play();
        gameLayer.getChildren().add(frost);

        Timeline t = new Timeline(
                new KeyFrame(Duration.seconds(0.4),
                        _ -> gameLayer.getChildren().remove(frost))
        );
        t.play();
    }
}