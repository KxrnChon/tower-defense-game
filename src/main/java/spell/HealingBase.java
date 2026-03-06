package spell;

import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import logic.GameLogic;

import java.util.Objects;

/**
 * Represents a supportive utility spell designed to restore the player's base health.
 * * Unlike offensive area-of-effect spells, the {@code HealingBase} spell interacts
 * directly with the global game state to repair structural damage. It is a
 * non-positional spell, meaning its primary effect scales based on the
 * game's logic rather than the spatial coordinates of the cast.
 * @author Pun
 * @version 1.0
 */
public class HealingBase extends Spell {

    /**
     * A reference to the core game engine to modify the base health status.
     */
    private final GameLogic gameLogic;

    /**
     * The sound effect associated with the healing process, loaded as a static resource.
     */
    private static final AudioClip healingSound;

    static {
        healingSound = new AudioClip(
                Objects.requireNonNull(HealingBase.class.getResource("/sound/healingSound.mp3")).toExternalForm()
        );
    }

    /**
     * Constructs a HealingBase spell that references the central game logic.
     *
     * @param gameLogic The {@link GameLogic} instance controlling the game state.
     * @param gameLayer The visual pane (provided for consistency with the Spell interface).
     */
    public HealingBase(GameLogic gameLogic, Pane gameLayer) {
        this.name = "Healing Base";
        this.damage = 0;
        this.range = 10;
        this.gameLogic = gameLogic;
        this.special = "Heal Hp 50 Point";
    }

    /**
     * Executes the healing logic to restore base health points.
     * * The casting process follows these steps:
     * 1. Audio Feedback: Plays the {@code healingSound} to confirm the action.
     * 2. State Modification: Increases the base HP by a fixed amount (50).
     * 3. Boundary Validation: Uses {@link Math#min} to ensure the health
     * does not exceed the maximum threshold of 100 points.
     *
     * @param x The horizontal coordinate (not used for logic, but required by override).
     * @param y The vertical coordinate (not used for logic, but required by override).
     */
    @Override
    public void cast(double x, double y) {
        int healAmount = 50;
        healingSound.play();
        gameLogic.setBaseHP(Math.min(100,gameLogic.getBaseHP() + healAmount));
    }
}