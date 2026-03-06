package view;

/**
 * Defines the set of primitive geometric shapes used for visual identification
 * of game entities.
 * * This enumeration is primarily utilized by the rendering engine to determine
 * which graphical representation to apply to projectiles, icons, or effect
 * placeholders. It provides a standardized way to categorize entities based
 * on their visual archetype without relying on hardcoded strings.
 */
public enum VisualShape {

    /**
     * Represents a circular geometry, often used for standard projectiles
     * or area-of-effect (AoE) indicators.
     */
    CIRCLE,
    SQUARE,
    TRIANGLE,
    HEXAGON
}
