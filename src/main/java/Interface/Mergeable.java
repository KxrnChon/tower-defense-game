package Interface;

import tower.Tower;

/**
 * The Mergeable interface defines a contract for entities that can be
 * combined to create a more advanced or specialized unit.
 * * It is primarily used by Tower subclasses to facilitate the fusion
 * mechanic, where two compatible units merge to produce a superior
 * defensive structure based on predefined fusion recipes.
 *
 * @author Pun
 * @version 1.0
 */
public interface Mergeable {

    /**
     * Determines and returns the resulting tower produced by merging
     * this entity with another compatible unit.
     *
     * @param other The secondary {@link Tower} unit involved in the fusion process
     * @return A new {@link Tower} instance representing the fusion result,
     * or null if the units are not compatible for merging.
     */
    Tower getFusionResult(Tower other);
}
