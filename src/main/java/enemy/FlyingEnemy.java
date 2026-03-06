package enemy;

import path.EnemyPath;

/**
 * The FlyingEnemy class represents a specialized type of enemy unit
 * capable of aerial movement.
 * * While inheriting all core functionalities from the base {@link Enemy} class,
 * this class provides a specific identity for air-based units. This
 * classification allows the game system to distinguish between ground and
 * aerial targets, enabling specialized interactions with towers that
 * can target air units.
 *
 * @author Pun
 * @version 1.0
 */
public class FlyingEnemy extends Enemy {

    /**
     * Constructs a new FlyingEnemy by invoking the superclass constructor.
     * Initializes the aerial unit with specific pathing, health, and speed statistics.
     *
     * @param enemyPath The navigation path assigned to this aerial unit
     * @param hp        Initial health points for the flying unit
     * @param damage    Damage dealt to the player's health upon completion
     * @param speed     Movement velocity across the waypoints
     */
    public FlyingEnemy(EnemyPath enemyPath, int hp, int damage, double speed) {
        super(enemyPath, hp, damage, speed);
    }
}