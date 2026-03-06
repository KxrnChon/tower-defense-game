package view;

import card.Card;
import card.SpellCard;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents the visual representation of a single collectible item, such as a Tower or a Spell.
 * * The {@code CardView} class is a specialized UI component that acts as a bridge
 * between the data model (Card) and the graphical interface. It utilizes a
 * {@link StackPane} to layer text information over a stylized background,
 * providing an interactive element for the player to select game assets.
 */
public class CardView extends StackPane {

    /** The data model associated with this view. */
    private final Card card;

    /** The background shape of the card, used for highlighting and feedback. */
    private final Rectangle bg;

    /**
     * Constructs a CardView with dynamic content based on the provided card type.
     * * The constructor performs the following architectural tasks:
     * 1. Dynamic Content Rendering: Differentiates between Spell cards and Tower cards
     * to display relevant statistics (e.g., hiding 'SPA' for spells).
     * 2. Interaction Logic: Uses a {@link Consumer} callback to notify external controllers
     * when this card is clicked.
     * 3. Hover Feedback: Implements mouse listeners to provide visual cues (color shifts)
     * when the user hovers over the card.
     *
     * @param card     The {@link Card} data to be displayed.
     * @param onSelect A callback function to be executed when the card is selected.
     */
    public CardView(Card card, Consumer<Card> onSelect) {

        this.card = card;

        bg = new Rectangle(120, 160);
        bg.setFill(Color.web("#2E3A3A"));
        bg.setArcWidth(20);
        bg.setArcHeight(20);

        Text name = new Text(card.getName());
        name.setFill(Color.WHITE);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text dmg = new Text("DMG: " + card.getDamage());
        Text spa = new Text("SPA: " + card.getSpa());
        Text range = new Text("RNG: " + card.getRange());
        Text special = new Text(card.getSpecial());

        dmg.setFill(Color.LIGHTGRAY);
        spa.setFill(Color.LIGHTGRAY);
        range.setFill(Color.LIGHTGRAY);
        special.setFill(Color.LIGHTGRAY);
        VBox content;
        if(card instanceof SpellCard){
            content = new VBox(5, name, dmg, range);
        }
        else {
            content = new VBox(5, name, spa, dmg, range);
        }
        if(!Objects.equals(special.getText(), "")){
            content.getChildren().add(special);
        }
        content.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        getChildren().addAll(bg, content);
        setMouseTransparent(false);
        setPickOnBounds(true);
        setOnMouseClicked(_ -> onSelect.accept(card));

        setOnMouseEntered(_ -> bg.setFill(Color.web("#3F5A5A")));
        setOnMouseExited(_ -> bg.setFill(Color.web("#2E3A3A")));
    }

    /**
     * Retrieves the card data model associated with this view.
     * @return The {@link Card} object.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Updates the visual state of the card to indicate whether it is currently selected.
     * * When selected, a golden stroke is applied to the background to provide
     * high-contrast visual feedback to the player.
     *
     * @param selected {@code true} to highlight the card, {@code false} to remove the highlight.
     */
    public void setSelected(boolean selected) {
        if (selected) {
            bg.setStroke(Color.GOLD);
            bg.setStrokeWidth(3);
        } else {
            bg.setStroke(null);
        }
    }
}