package enemy;

import path.EnemyPath;

/**
 * The Skeleton class represents a standard ground-based enemy unit.
 * * As a concrete implementation of the {@link Enemy} class, it serves as a
 * fundamental hostile entity within the game. This class defines the visual
 * identity for the skeleton unit while inheriting all core movement and
 * combat behaviors from the base enemy template.
 *
 * @author Pun
 * @version 1.0
 */
public class Skeleton extends Enemy{

    /**
     * Constructs a Skeleton instance and assigns its specific visual assets.
     * * This constructor initializes the skeleton with provided pathing and
     * combat statistics, while setting the internal image path to the
     * corresponding skeleton sprite resource.
     *
     * @param enemyPath The navigation path the skeleton will follow
     * @param hp        Initial health points
     * @param damage    Damage dealt to the player's base
     * @param speed     Movement velocity along the ground path
     */
    public Skeleton(EnemyPath enemyPath, int hp, int damage, double speed) {
        super(enemyPath,hp,damage,speed);
        this.imagePath = "/image/enemy/Skeleton.png";
    }
}
