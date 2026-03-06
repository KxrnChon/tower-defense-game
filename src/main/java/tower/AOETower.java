package tower;

import bullets.Bullet;
import enemy.Enemy;
import enemy.FlyingEnemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract specialization of {@link Tower} designed for Area of Effect (AoE) attacks.
 * * The {@code AOETower} does not fire traditional projectiles; instead, it triggers
 * an expanding environmental effect (visualized as gas) that deals instantaneous
 * damage to all valid targets within its range. It manages the lifecycle of this
 * visual effect, including expansion speed, radius limits, and opacity fading.
 * @author Pun
 * @version 1.0
 */
public abstract class AOETower extends Tower {

    /** Current radius of the expanding gas effect. */
    protected double gasRadius = 0;

    /** The maximum size the gas effect can reach, typically equal to the tower's range. */
    protected double maxGasRadius;

    /** The speed at which the gas expands per frame. */
    protected double gasExpandSpeed;

    /** Transparency level of the gas effect for visual fading. */
    protected double gasOpacity = 1.0;

    /** Tracks how long the gas has been expanding. */
    protected double gasExpandTime = 0;

    /** Duration threshold after which the gas begins to fade away. */
    protected static final double GAS_FADE_TIME = 0.1;

    /** State flag indicating if the AoE effect is currently being animated. */
    protected boolean gasActive = false;

    /**
     * Constructs an AOETower with specific AoE attributes.
     *
     * @param x        Horizontal position.
     * @param y        Vertical position.
     * @param range    Attack and gas expansion radius.
     * @param cooldown Time between attacks.
     * @param damage   Damage applied to all enemies in range.
     */
    public AOETower(double x, double y,double range,double cooldown,int damage) {
        super(x, y, range,cooldown,damage);
        this.maxGasRadius = range;
        this.gasExpandSpeed = range/8;
    }

    /**
     * Updates the tower logic and the state of its active AoE effects.
     * * In addition to standard tower updates, this method handles the expansion
     * and fading logic of the gas effect if {@code gasActive} is true.
     */
    public void update(double deltaTime, List<Enemy> enemies, List<Bullet> bullets) {
        if(gasActive) {
            updateGasEffect(deltaTime);
        }
        if (stunned) {
            stunTimer -= deltaTime;

            if (stunTimer <= 0) {
                stunned = false;
            }

            return;
        }

        timer += deltaTime;

        if (timer < cooldown) return;

        List<Enemy> targets = findTarget(enemies);
        if(targets.isEmpty()) return;
        shootGas();
        for(Enemy target : targets){
            shoot(target, bullets);
        }
        timer = 0;

    }

    /**
     * Manages the animation properties of the gas effect.
     * * Handles the expansion of {@code gasRadius} and the gradual reduction of
     * {@code gasOpacity} before resetting the effect state.
     *
     * @param deltaTime Time elapsed since the last update.
     */
    protected void updateGasEffect(double deltaTime) {
        gasRadius = Math.min(maxGasRadius, gasRadius + gasExpandSpeed);

        gasExpandTime += deltaTime;
        if (gasExpandTime > GAS_FADE_TIME) {
            gasOpacity = Math.max(0, gasOpacity - deltaTime * 1);
        }

        if (gasOpacity <= 0) {
            gasActive = false;
            gasRadius = 0;
            gasExpandTime = 0;
            gasOpacity = 1.0;
        }
    }

    /**
     * Renders the AoE gas effect onto the game canvas.
     * * Uses {@link GraphicsContext} to draw a semi-transparent, expanding oval
     * that represents the toxic or explosive area.
     *
     * @param gc The GraphicsContext used for drawing.
     */
    public void drawGas(GraphicsContext gc) {
        if (gasActive && gasOpacity > 0) {
            Color gasColor = Color.color(0, 0.8, 0, gasOpacity * 0.6);
            gc.setFill(gasColor);
            gc.fillOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);

            gc.setStroke(Color.color(0, 0.6, 0, gasOpacity));
            gc.setLineWidth(2.5);
            gc.strokeOval(x - gasRadius, y - gasRadius, gasRadius * 2, gasRadius * 2);
        }
    }

    /**
     * Identifies all valid enemies within the tower's range.
     * * Unlike standard towers that return a single "best" target, this implementation
     * collects all non-flying, living enemies within the engagement radius.
     *
     * @param enemies List of all active enemies.
     * @return A list of all enemies currently inside the AoE radius.
     */
    @Override
    protected List<Enemy> findTarget(List<Enemy> enemies) {
        List<Enemy> result = new ArrayList<>();
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            if(e instanceof FlyingEnemy) continue;
            if (notInRange(e)) continue;
            result.add(e);
        }
        return result;
    }

    /**
     * Triggers the visual expansion of the gas effect.
     */
    protected void shootGas() {
        gasActive = true;
        gasRadius = 0;
        gasExpandTime = 0;
        gasOpacity = 0.5;
    }

    /**
     * Applies instantaneous damage to the specified target.
     * * In AOETower, this is called for every target identified in
     * {@link #findTarget} during the update cycle.
     */
    @Override
    protected void shoot(Enemy target, List<Bullet> bullets) {
        target.takeDamage(getBulletDamage());
    }

}