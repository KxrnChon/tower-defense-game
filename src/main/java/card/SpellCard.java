package card;

import spell.Spell;
import java.util.function.Supplier;

/**
 * The SpellCard class is a concrete implementation of the Card template
 * specifically designed for spell-based actions.
 * * This class utilizes a functional approach by employing a Supplier factory
 * to instantiate Spell entities. It synchronizes card attributes directly
 * with the underlying spell data to ensure consistency between the card
 * interface and game world effects.
 *
 * @author Pun
 * @version 1.0
 */
public class SpellCard extends Card {

    /** The functional interface used to produce new Spell instances. */
    private final Supplier<Spell> spellFactory;

    /**
     * Constructs a SpellCard by extracting attributes from a provided Spell factory.
     * This constructor initializes the inherited fields such as name, damage,
     * range, and special abilities based on the initial state of the spell.
     *
     * @param spellFactory The Supplier functional interface that generates Spell entities
     */
    public SpellCard(Supplier<Spell> spellFactory) {
        super();
        this.spellFactory = spellFactory;
        this.name = spellFactory.get().getName();
        this.damage = spellFactory.get().getDamage();
        this.range = spellFactory.get().getRange();
        this.special = spellFactory.get().getSpecial();
    }

    /**
     * Executes the summoning process by invoking the spell factory.
     * Overrides the base summon method to return a specific Spell instance
     * ready for deployment in the game environment.
     *
     * @return A new {@link Spell} object produced by the factory
     */
    @Override
    public Spell summon() { return spellFactory.get(); }
}