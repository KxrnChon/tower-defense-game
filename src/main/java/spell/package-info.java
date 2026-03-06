/**
 * Provides the framework and concrete implementations for the game's active ability system.
 * * This package encapsulates the "Spell" logic, which allows for player-driven
 * interventions in the game world. It includes an abstract base class that defines
 * common magical attributes and multiple concrete implementations ranging from
 * Area of Effect (AoE) damage to complex cinematic ultimate abilities.
 * * Key features within this package include:
 * - Inheritance-based Spell System: Standardized casting mechanics via the abstract Spell class.
 * - Status Effect Pipeline: Integration with enemy states for crowd control (e.g., FrostBite).
 * - Cinematic Management: Advanced visuals and game-state orchestration (e.g., XSnap).
 */
package spell;