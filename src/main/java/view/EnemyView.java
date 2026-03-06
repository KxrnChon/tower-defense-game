package view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import enemy.Enemy;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Provides the graphical representation and real-time status tracking for enemy entities.
 * * The {@code EnemyView} class is a dynamic UI component that renders an enemy's
 * sprite, health bar, and status effects (such as armor and slow debuffs). It acts
 * as the visual layer for the {@link enemy.Enemy} model, ensuring that changes
 * in the game logic are immediately visible to the player.
 */
public class EnemyView extends StackPane {

    /** The underlying enemy data model being observed. */
    private final Enemy enemy;

    /** Text display for current health points. */
    private final Text hpText;

    /** Foreground health bar that scales based on remaining health. */
    private final Rectangle hpBar;

    /** Visual indicator for the "Slow" status effect. */
    private final Circle slowIcon;

    /** Text display for remaining armor points. */
    private final Text armorText;

    /** Constant defining the base width of the health bar background. */
    private final double BAR_WIDTH = 40;

    /**
     * Constructs an EnemyView and initializes its visual components.
     * * This constructor sets up the layering of graphical elements:
     * 1. Sprite: Loads the enemy image, flips it on the X-axis for direction, and
     * disables smoothing for a "pixel-perfect" or sharp aesthetic.
     * 2. Health HUD: Initializes the health bar background (DARKRED) and foreground (LIMEGREEN).
     * 3. Status Indicators: Pre-positions armor text and a slow icon above the sprite.
     *
     * @param enemy The {@link enemy.Enemy} instance to be visualized.
     * @param size  The size (width and height) of the enemy sprite.
     */
    public EnemyView(Enemy enemy, double size) {

        this.enemy = enemy;
        double halfSize = size / 2.0;

        setPrefSize(size, size);

        Image img = new Image(Objects.requireNonNull(getClass().getResource(enemy.getImagePath())).toExternalForm());

        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.setScaleX(-1);

        // ===== HP BAR =====

        double BAR_HEIGHT = 12.5;
        Rectangle hpBarBg = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        hpBarBg.setFill(Color.DARKRED);
        hpBarBg.setTranslateY(-halfSize - 15);

        hpBar = new Rectangle(BAR_WIDTH, BAR_HEIGHT);
        hpBar.setFill(Color.LIMEGREEN);
        hpBar.setTranslateY(-halfSize - 15);

        hpText = new Text();
        hpText.setFill(Color.WHITE);
        hpText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        hpText.setTranslateY(-halfSize - 17);

        armorText = new Text();
        armorText.setStyle("-fx-font-weight: bold;");
        armorText.setFill(Color.YELLOW);
        armorText.setTranslateY(-halfSize - 25);

        slowIcon = new Circle(6);
        slowIcon.setFill(Color.LIGHTBLUE);
        slowIcon.setTranslateY(-halfSize - 8);

        getChildren().addAll(imageView, hpBarBg, hpBar, hpText, armorText, slowIcon);
    }

    /**
     * Synchronizes the view's properties with the current state of the enemy model.
     * * This method is called during each frame of the game loop to update:
     * 1. Position: Aligns the layout coordinates with the enemy's spatial data.
     * 2. Health Bar: Calculates the health percentage and scales the {@code hpBar} width.
     * 3. Dynamic Coloring: Changes health bar color (Green -> Orange -> Red) based on
     * health thresholds.
     * 4. Visibility Logic: Toggles the display of the slow icon and armor text based
     * on active status effects.
     */
    public void sync() {

        setLayoutX(enemy.getPosition().getX() - getWidth() / 2);
        setLayoutY(enemy.getPosition().getY() - getHeight() / 2);
        hpText.setText(enemy.getHp() + " / " + enemy.getMaxHp());

        double hpPercent = (double) enemy.getHp() / enemy.getMaxHp();
        double newWidth = BAR_WIDTH * hpPercent;

        hpBar.setWidth(newWidth);

        hpBar.setTranslateX(-(BAR_WIDTH - newWidth) / 2);

        if (hpPercent > 0.6) {
            hpBar.setFill(Color.LIMEGREEN);
        } else if (hpPercent > 0.3) {
            hpBar.setFill(Color.ORANGE);
        } else {
            hpBar.setFill(Color.RED);
        }

        slowIcon.setVisible(enemy.isSlowed());

        if (enemy.hasArmor() && enemy.getArmor() > 0) {
            armorText.setText("A: " + enemy.getArmor());
            armorText.setVisible(true);
        } else {
            armorText.setVisible(false);
        }
    }

    /** @return The {@link enemy.Enemy} model associated with this view. */
    public Enemy getEnemy() {
        return enemy;
    }
}