package enemy;

import logic.GameLogic;
import path.EnemyPath;

/**
 * The Bomber class is a specialized enemy unit that triggers a tactical
 * effect upon its destruction.
 * * This class demonstrates advanced polymorphism by overriding the {@link #die()}
 * method. In addition to standard death logic, it interacts with the
 * {@link GameLogic} to apply a stun effect to nearby towers,
 * adding a strategic layer to the game's combat mechanics.
 *
 * @author Pun
 * @version 1.0
 */
public class Bomber extends Enemy {

    /**
     * Constructs a Bomber instance and initializes its visual representation.
     * Inherits core movement and health attributes from the base {@link Enemy} class.
     *
     * @param enemyPath The navigation path the Bomber will follow
     * @param hp        Initial health points
     * @param damage    Damage dealt to the player upon reaching the end
     * @param speed     Movement velocity
     */
    public Bomber(EnemyPath enemyPath, int hp, int damage, double speed) {
        super(enemyPath,hp,damage,speed);
        this.imagePath = "/image/enemy/Bomber.png";
    }

    /**
     * Overrides the death logic to implement an "On Death" explosion effect.
     * * When this unit is destroyed, it calculates a stun radius and communicates
     * with the GameLogic singleton to disable nearby towers temporarily and
     * trigger visual feedback for the explosion.
     * * This implementation ensures the effect is only triggered once by
     * checking the current life state before execution.
     */
    @Override
    public void die() {
        if (!isAlive()) return;
        super.die();
        GameLogic logic = GameLogic.getInstance();
        double stunRadius = 120;
        double stunDuration = 2.0;
        logic.stunNearbyTowers(getPosition(), stunRadius, stunDuration);
        logic.showStunEffect(getPosition(), stunRadius, stunDuration);
    }
}