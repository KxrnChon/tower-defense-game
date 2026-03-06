/**
 * Provides the core logic and specialized implementations for defensive structures.
 * * This package contains the hierarchical structure of the tower system, ranging from
 * basic elemental units to advanced fusion structures. It manages target acquisition
 * algorithms, combat state updates, and the integration of visual effects.
 * * The system is designed around two main pillars:
 * 1. Inheritance: Utilizing a base Tower class and AOETower specialization to
 * standardize behaviors like cooldowns, stunning, and area damage.
 * 2. Fusion Logic: Implementing the Mergeable interface to allow dynamic creation
 * of hybrid towers, combining different elemental properties into elite units.
 */
package tower;