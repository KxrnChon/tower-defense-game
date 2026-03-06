package tower;

import enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * An advanced fusion tower that combines Area-of-Effect (AoE) damage with expanded environmental reach.
 * * The {@code TornadoTower} is the elite result of merging a Gas Tower and a Range Tower.
 * By adopting the superior detection capabilities of the Range element, it doubles
 * the effective AoE radius of standard gas-based towers. It generates high-velocity
 * air currents (visualized as white mist or tornadoes) that damage all types of
 * enemies—including flying units—caught within its massive 150-unit radius.
 * @author Pun
 * @version 1.0
 */
public class TornadoTower extends AOETower {

    /**
     * Constructs a TornadoTower with enhanced territorial control statistics:
     * - Range: 150 units (Double the radius of a standard Gas Tower)
     * - Cooldown: 1.5 seconds
     * - Damage: 10 per atmospheric pulse
     *
     * @param x Horizontal position on the map.
     * @param y Vertical position on the map.
     */
    public TornadoTower(double x, double y) {
        super(x, y, 150, 1.5, 10);
        this.name = "Tornado Tower";
        this.imagePath = "/image/tower/TornadoTower.png";
        this.placeSoundPath = "/sound/gasTowerSound.mp3";
    }

    /**
     * Renders a specialized "Vortex Pulse" visual effect.
     * * This override replaces the toxic green aesthetic with a bright white and
     * light gray color palette (RGB: 1.0, 1.0, 1.0) to simulate high-speed air
     * or a localized tornado. The effect uses a slightly thinner stroke width (2px)
     * to represent the sharp, cutting nature of wind.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void drawGas(GraphicsContext gc) {
        if (gasActive && gasOpacity > 0) {

            Color gasColor = Color.color(1, 1, 1.0, gasOpacity * 0.6);
            gc.setFill(gasColor);
            gc.fillOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);

            gc.setStroke(Color.color(1, 1, 1.0, gasOpacity));
            gc.setLineWidth(2);
            gc.strokeOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);
        }
    }

    /**
     * Identifies all valid enemies within the expanded atmospheric zone.
     * * Critically, the {@code TornadoTower} overrides the standard AoE behavior
     * by removing the exclusion of {@link enemy.FlyingEnemy}. This allows the
     * wind currents to hit both ground and air units simultaneously, provided
     * they are within the 150-unit radius.
     *
     * @param enemies List of all active enemies in the battlefield.
     * @return A list of all enemies (ground and air) currently inside the range.
     */
    @Override
    protected List<Enemy> findTarget(List<Enemy> enemies) {
        List<Enemy> result = new ArrayList<>();
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            if (notInRange(e)) continue;
            result.add(e);
        }
        return result;
    }
}