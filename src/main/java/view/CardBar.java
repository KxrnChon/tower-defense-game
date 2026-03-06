package view;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * The {@code CardBar} class represents the interactive toolbar for selecting
 * towers or spells within the game's interface.
 * * It extends {@link HBox} to provide a horizontally aligned container for card
 * elements. This class implements a high-fidelity visual design using CSS-style
 * properties and graphical effects to create a modern, translucent HUD (Heads-Up Display)
 * that hovers over the game world.
 */
public class CardBar extends HBox {

    /**
     * Constructs a new CardBar with a predefined aesthetic and layout configuration.
     * * The initialization process defines:
     * 1. Layout: Center alignment with specific spacing between card items.
     * 2. Aesthetics: A semi-transparent dark background with rounded corners and border.
     * 3. Effects: A subtle {@link DropShadow} to provide depth and visual separation
     * from the game layer.
     * 4. Interaction: Configured to capture mouse events while allowing transparency
     * for non-bounded clicks.
     */
    public CardBar() {
        setSpacing(20);
        setAlignment(Pos.CENTER);
        setPrefHeight(180);
        setStyle("""
        -fx-background-color: rgba(40, 50, 60, 0.75);
        -fx-background-radius: 20;
        -fx-border-color: rgba(255,255,255,0.4);
        -fx-border-radius: 20;
        -fx-padding: 10;
        """);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);
        shadow.setColor(Color.rgb(0,0,0,0.25));
        setEffect(shadow);
        setSpacing(20);
        setMouseTransparent(false);
        setPickOnBounds(false);
    }
}