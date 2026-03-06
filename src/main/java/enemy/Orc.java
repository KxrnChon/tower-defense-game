package enemy;

import path.EnemyPath;

/**
 * The Orc class represents a heavily armored enemy unit.
 * * This class extends the base {@link Enemy} logic by introducing an armor system.
 * The Orc possesses an additional layer of protection that must be depleted
 * before its actual health points can be reduced. This class demonstrates
 * effective use of method overriding to modify core combat mechanics.
 *
 * @author Pun
 * @version 1.0
 */
public class Orc extends Enemy {

    /** The current durability of the unit's armor layer. */
    private int armor;

    /**
     * Constructs an Orc instance with health-proportional armor.
     * Initializes the armor value to be equal to its starting health, doubling
     * its effective survivability against standard attacks.
     *
     * @param enemyPath The navigation path assigned to the unit
     * @param hp        Initial health points
     * @param damage    Damage dealt to the player's base
     * @param speed     Movement velocity
     */
    public Orc(EnemyPath enemyPath, int hp, int damage, double speed) {
        super(enemyPath, hp, damage, speed);
        this.armor = hp;
        this.imagePath = "/image/enemy/Orc.png";
    }

    /**
     * Overrides the damage processing logic to implement the armor system.
     * * Incoming damage is first deducted from the armor pool. If the damage
     * exceeds the remaining armor, the surplus is then applied to the unit's
     * actual health points via the superclass's takeDamage method.
     *
     * @param damage The amount of raw damage received
     */
    @Override
    public void takeDamage(int damage) {
        if (!isAlive()) return;

        if (armor > 0) {
            armor -= damage;

            if (armor < 0) {
                super.takeDamage(-armor);
                armor = 0;
            }
        } else {
            super.takeDamage(damage);
        }
    }

    /**
     * Checks if the armor layer is still functional.
     * @return true if the unit currently has remaining armor, false otherwise
     */
    @Override
    public boolean hasArmor() { return armor > 0; }

    /**
     * Retrieves the current armor durability.
     * @return The integer value of the remaining armor
     */
    @Override
    public int getArmor() { return armor; }

}