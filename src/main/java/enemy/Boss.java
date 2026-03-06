package enemy;

import logic.GameLogic;
import path.EnemyPath;

/**
 * The Boss class represents a high-priority hostile unit with unique behavior
 * upon reaching the player's base.
 * * Unlike standard enemies that are removed from the game after completing
 * their path, the Boss remains active to perform repeated attacks against
 * the player's base. This class demonstrates specialized state management
 * by overriding movement and end-of-path logic.
 *
 * @author Pun
 * @version 1.0
 */
public class Boss extends Enemy {

    /** Internal timer used to manage the interval between base attacks. */
    private double attackTimer = 0;

    /**
     * Constructs a Boss instance and assigns its specific visual assets.
     * Inherits core attributes from the {@link Enemy} class while setting
     * a unique Boss-themed sprite.
     *
     * @param path   The navigation path the boss will traverse
     * @param hp     Total health points, typically significantly higher than standard units
     * @param damage Damage dealt per attack cycle to the player's base
     * @param speed  Movement velocity along the path
     */
    public Boss(EnemyPath path, int hp, int damage, double speed) {
        super(path, hp, damage, speed);
        this.imagePath = "/image/enemy/Boss.png";
    }

    /**
     * Executes a repeated attack cycle against the player's base.
     * This method is active only after the boss has reached the end of its path.
     * It uses a cooldown mechanism to ensure the base takes damage at
     * fixed intervals.
     *
     * @param deltaTime The time elapsed since the last frame update
     */
    public void attackBase(double deltaTime) {

        if (!hasReachedEnd()) return;

        attackTimer += deltaTime;

        double attackCooldown = 1.0;
        if (attackTimer >= attackCooldown) {
            attackTimer = 0;
            GameLogic.getInstance().damageBase(getDamage());
        }
    }

    /**
     * Updates the Boss state, prioritizing movement until the destination is reached.
     * Once the Boss reaches the end of the path, movement stops, but the entity
     * remains active to facilitate the base attack logic.
     *
     * @param deltaTime The time interval since the last update
     */
    @Override
    public void update(double deltaTime) {
        if (!hasReachedEnd()) {
            super.update(deltaTime);
        }
    }

    /**
     * Overrides the end-of-path logic to prevent the Boss from being removed.
     * Standard enemies call die() or are cleared upon arrival, but the Boss
     * overrides this to remain on the battlefield for a prolonged siege.
     */
    @Override
    public void onReachEnd() {}
}