package spell;

/**
 * The {@code Spell} class serves as the abstract base for all magical abilities
 * in the game. It defines the core attributes and the contract for execution
 * through the {@code cast} method.
 * * Spells are designed to be triggered by the player, typically resulting in
 * area-of-effect (AoE) damage, debuffs, or structural repairs. By extending this
 * class, specific spell types can implement unique behaviors while sharing
 * common properties like range and damage.
 * @author Pun
 * @version 1.0
 */
public abstract class Spell {

    /**
     * The display name of the spell.
     */
    protected String name;

    /**
     * The primary magnitude of the spell's effect, often representing health reduction.
     */
    protected int damage;

    /**
     * The radius or reach within which the spell's effect is active.
     */
    protected double range;

    /**
     * A description or identifier for unique attributes of the spell
     * (e.g., "Slow", "Heal").
     */
    protected String special = "";

    /**
     * Executes the spell's unique logic at the specified spatial coordinates.
     * * Subclasses must implement this method to define how the spell interacts
     * with the game environment (e.g., damaging enemies or healing the base).
     *
     * @param x The horizontal coordinate where the spell is cast.
     * @param y The vertical coordinate where the spell is cast.
     */
    public abstract void cast(double x, double y);

    /** @return The name of the spell. */
    public String getName() { return name; }

    /** @return The damage or primary intensity of the spell. */
    public int getDamage() { return damage; }

    /** @return The operational range of the spell. */
    public double getRange() { return range; }

    /** @return The special effect string associated with the spell. */
    public String getSpecial() {return special; }
}