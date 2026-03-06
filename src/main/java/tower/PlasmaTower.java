package tower;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * An elite fusion tower that combines Area-of-Effect (AoE) capabilities with high-frequency attacks.
 * * The {@code PlasmaTower} is the advanced result of merging a Gas Tower and a Zap Tower.
 * By inheriting the wide-area damage of the Gas element and the rapid firing rate of
 * the Zap element, this tower functions as a high-DPS "energy pulse" generator.
 * It deals consistent, rapid damage to all enemies within its immediate vicinity,
 * making it exceptionally effective against dense swarms.
 * @author Pun
 * @version 1.0
 */
public class PlasmaTower extends AOETower {

    /**
     * Constructs a PlasmaTower with high-performance combat statistics:
     * - Range: 75 units (Focused AoE radius)
     * - Cooldown: 0.7 seconds (Very fast for an AoE tower)
     * - Damage: 10 per energy pulse
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public PlasmaTower(double x, double y) {
        super(x, y, 75, 0.7, 10);
        this.name = "Plasma Tower";
        this.imagePath = "/image/tower/PlasmaTower.png";
        this.placeSoundPath = "/sound/zapTowerSound.mp3";
    }

    /**
     * Renders a specialized "Plasma Pulse" visual effect.
     * * This override replaces the standard gas aesthetic with a vibrant purple
     * and magenta color scheme (RGB: 0.7, 0.2, 1.0) to represent high-energy
     * ionized gas. The pulse expands and fades rapidly, reflecting the tower's
     * high attack speed.
     *
     * @param gc The GraphicsContext used for drawing on the game canvas.
     */
    @Override
    public void drawGas(GraphicsContext gc) {
        if (gasActive && gasOpacity > 0) {

            Color gasColor = Color.color(0.7, 0.2, 1.0, gasOpacity * 0.55);
            gc.setFill(gasColor);
            gc.fillOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);

            gc.setStroke(Color.color(0.9, 0.6, 1.0, gasOpacity));
            gc.setLineWidth(2.5);
            gc.strokeOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);
        }
    }
}