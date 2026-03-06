package card;

/**
 * The Card class serves as an abstract blueprint for all playable cards within the game.
 * It defines the shared attributes and core behaviors that every card must possess,
 * such as combat statistics and summoning capabilities.
 * * As an abstract class, it provides a consistent interface for the game logic
 * to interact with various card types, ensuring modularity and extensibility
 * in the tower defense system.
 *
 * @author Pun
 * @version 1.0
 */
public abstract class Card {
    /** The display name used to identify the card in the user interface. */
    protected String name;

    /** The base offensive power or damage dealt by the summoned entity. */
    protected int damage;

    /** * Speed Per Attack (SPA).
     * Defines the time interval in seconds between consecutive attack actions.
     */
    protected double spa;

    /** The operational radius or maximum distance the summoned entity can reach. */
    protected double range;

    /** A descriptive text detailing the card's unique abilities or special traits. */
    protected String special = "";

    /** * The factory object responsible for instantiating the game entity
     * associated with this card.
     */
    protected Object summonFactory;

    /**
     * Default constructor for initializing a new card instance.
     * Intended to be invoked by concrete subclasses.
     */
    public Card() {}

    /** @return The name of the card. */
    public String getName() { return name; }

    /** @return The integer value of the card's damage. */
    public int getDamage() { return damage; }

    /** @return The time interval (SPA) between attacks. */
    public double getSpa() { return spa; }

    /** @return The attack or effect range of the card. */
    public double getRange() { return range; }

    /** @return The string description of the card's special effect. */
    public String getSpecial() { return special; }

    /**
     * Triggers the summoning mechanism defined for this card.
     * This method returns the factory object required to spawn the entity
     * into the game world.
     * * @return The factory or entity object used for summoning.
     */
    public Object summon() { return summonFactory; }
}