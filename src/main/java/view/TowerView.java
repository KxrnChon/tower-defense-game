package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import tower.Tower;

import java.util.Objects;

/**
 * The TowerView class provides the graphical representation for a specific tower instance.
 * * This class acts as the visual container for a tower's sprite, extending {@link StackPane}
 * to allow for potential future layering.
 * It handles image loading, scaling, and precise spatial positioning within the game world
 * based on the underlying tower model's coordinates.
 */
public class TowerView extends StackPane {

    /** The data model representing the tower's logical state and position. */
    private final Tower tower;

    /**
     * Constructs a new TowerView and initializes its visual sprite.
     * * The construction process involves:
     * 1. Resource Loading: Retrieving the image from the specified path using the
     * class loader to ensure compatibility with JAR packaging.
     * 2. Scaling: Configuring the {@link ImageView} to fit a uniform size while
     * preserving the original aspect ratio of the asset.
     * 3. Spatial Centering: Translating the view's position so that the center of
     * the image aligns perfectly with the tower's (x, y) logical coordinates.
     *
     * @param tower     The {@link tower.Tower} model associated with this view.
     * @param imagePath The resource path to the tower's sprite image.
     * @param size      The target width and height for rendering the tower.
     */
    public TowerView(Tower tower, String imagePath, double size) {
        this.tower = tower;

        Image img = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        ImageView imageView = new ImageView(img);

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);

        getChildren().add(imageView);

        setTranslateX(tower.getX() - size/2);
        setTranslateY(tower.getY() - size/2);
    }

    public Tower getTower() {
        return tower;
    }
}