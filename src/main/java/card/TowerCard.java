package card;

import tower.Tower;
import java.util.function.Supplier;

/**
 * The TowerCard class is a concrete implementation of the Card template
 * specifically designed for managing and summoning defensive structures.
 * * This class uses a Supplier-based factory pattern to bridge the card data
 * with the actual Tower entities. It extracts essential combat statistics
 * from the tower instance during initialization to ensure that the card
 * attributes accurately reflect the behavior of the summoned tower.
 *
 * @author Pun
 * @version 1.0
 */
public class TowerCard extends Card {

    /** The functional interface used to generate new Tower instances upon summoning. */
    private final Supplier<Tower> towerFactory;

    /**
     * Constructs a TowerCard by mapping tower-specific statistics to the
     * shared attributes inherited from the Card base class.
     * * This constructor performs data synchronization by fetching properties
     * such as name, bullet damage, cooldown (SPA), and range directly from
     * the Tower instance provided by the factory.
     *
     * @param towerFactory The Supplier functional interface that produces Tower entities
     */
    public TowerCard(Supplier<Tower> towerFactory) {
        super();
        this.towerFactory = towerFactory;
        this.name = towerFactory.get().getName();
        this.damage = towerFactory.get().getBulletDamage();
        this.spa = towerFactory.get().getCooldown();
        this.range = towerFactory.get().getRange();
    }

    /**
     * Executes the summoning process for a defensive tower.
     * Overrides the base summon method to return a specific Tower object
     * that can be placed and managed within the game board.
     *
     * @return A new {@link Tower} instance produced by the internal factory
     */
    @Override
    public Tower summon() { return towerFactory.get(); }
}