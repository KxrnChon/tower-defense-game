/**
 * Input Management and Event Handling System
 *
 * This package provides a centralized utility for processing keyboard and mouse 
 * events within the JavaFX environment. It implements a non-blocking state-tracking 
 * mechanism that allows the game engine to query input states (such as whether 
 * a key was "just pressed" or is "being held") during the main update loop.
 *
 * By decoupling the raw JavaFX events from the game logic, this package 
 * facilitates smoother control and more responsive gameplay interactions.
 *
 * @author Pun
 * @version 1.0
 */
package input;