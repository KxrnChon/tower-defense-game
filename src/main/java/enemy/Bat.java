package enemy;

import path.EnemyPath;

/**
 * The Bat class represents a concrete implementation of an aerial enemy unit.
 * * As a subclass of {@link FlyingEnemy}, it inherits all flight capabilities
 * while defining its own unique visual identity through a specific image sprite.
 * This class is used to instantiate standard flying units within the game's
 * early to mid-game waves.
 *
 * @author Pun
 * @version 1.0
 */
public class Bat extends FlyingEnemy {

    /**
     * Constructs a Bat instance and assigns its specific visual assets.
     * Inherits movement and combat statistics from the FlyingEnemy class while
     * initializing the imagePath directed to the bat's graphical resource.
     *
     * @param enemyPath The navigation path the bat will follow
     * @param hp        The health points of the bat
     * @param damage    The damage dealt to the player upon reaching the end
     * @param speed     The flight velocity of the bat
     */
    public Bat(EnemyPath enemyPath, int hp, int damage, double speed) {
        super(enemyPath, hp, damage, speed);
        this.imagePath = "/image/enemy/Bat.png";
    }
}