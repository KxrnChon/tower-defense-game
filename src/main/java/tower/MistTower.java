package tower;

import bullets.Bullet;
import enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * An advanced fusion tower that combines Area-of-Effect (AoE) damage with debilitating cold effects.
 * * The {@code MistTower} is the result of merging a Gas Tower with an Ice Tower.
 * Instead of toxic fumes, it emits a freezing mist that deals instantaneous
 * damage to all nearby enemies. Furthermore, it applies a significant "Slow"
 * debuff to any target caught within its radius, making it a premier
 * defensive structure for choking high-traffic paths.
 * @author Pun
 * @version 1.0
 */
public class MistTower extends AOETower {

    /**
     * Constructs a MistTower with specialized hybrid statistics:
     * - Range: 75 units (Focused AoE radius)
     * - Cooldown: 1.5 seconds
     * - Damage: 10 per pulse
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public MistTower(double x, double y) {
        super(x, y, 75, 1.5, 10);
        this.name = "Mist Tower";
        this.imagePath = "/image/tower/MistTower.png";
        this.placeSoundPath = "/sound/iceTowerSound.mp3";
    }

    /**
     * Applies both damage and a cryogenic status effect to the target.
     * * This override enhances the standard {@link AOETower#shoot} behavior
     * by invoking {@link Enemy#applySlow} on every enemy caught in the mist.
     *
     * @param target  The enemy currently within the AoE radius.
     * @param bullets The global projectile list (unused here as damage is instantaneous).
     */
    @Override
    protected void shoot(Enemy target, List<Bullet> bullets) {
        target.takeDamage(getBulletDamage());
        target.applySlow(0.3, 5);
    }

    /**
     * Renders a specialized "Freezing Mist" visual effect.
     * * Overrides the default gas rendering to use a light blue/cyan color palette
     * that represents sub-zero temperatures. The effect utilizes procedural
     * expansion and opacity fading to simulate a sudden burst of cold air.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void drawGas(GraphicsContext gc) {
        if (gasActive && gasOpacity > 0) {

            Color gasColor = Color.color(0.4, 0.8, 1.0, gasOpacity * 0.6);
            gc.setFill(gasColor);
            gc.fillOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);

            gc.setStroke(Color.color(0.5, 0.9, 1.0, gasOpacity));
            gc.setLineWidth(2);
            gc.strokeOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);
        }
    }
}