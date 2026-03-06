/**
 * Card System and Summoning Framework
 * * This package defines the core infrastructure for the card-based mechanics within
 * the game. It provides an abstract framework for representing various game
 * actions—such as building defenses or casting spells—through a unified
 * card interface.
 * * The system is built upon a hierarchical architecture using the Card base class,
 * which ensures consistency across different card types. By integrating with
 * a functional factory pattern (Supplier), this package enables the dynamic
 * instantiation of game entities, decoupling the card-based user interface
 * from the underlying game world logic.
 * * @author Pun
 * @version 1.0
 */
package card;