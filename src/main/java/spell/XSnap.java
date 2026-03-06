package spell;

import enemy.Enemy;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the ultimate "X Snap" spell, a high-impact ability that triggers a
 * cinematic sequence and deals massive damage to all active enemies.
 * * The {@code XSnap} spell is unique as it interacts with the game's temporal flow,
 * pausing all logical updates to execute a frame-based visual animation. It utilizes
 * a sequence of pre-loaded images to simulate a video-like effect, providing a
 * "theatrical" experience before applying its mechanical effects.
 * @author Pun
 * @version 1.0
 */
public class XSnap extends Spell {

    /** Flag to prevent multiple concurrent executions of the ultimate ability. */
    private boolean isCasting;

    /** Reference to the global enemy list for mass damage application. */
    private final List<Enemy> enemies;

    /** The root container for overlaying the cinematic UI components. */
    private final Pane root;

    /** Callback to suspend the game engine's logic updates. */
    private final Runnable pauseGame;

    /** Callback to resume the game engine's logic updates after the animation. */
    private final Runnable resumeGame;

    /** Static cache for animation frames to optimize memory usage across instances. */
    private static final List<Image> frames = new ArrayList<>();

    /** Guard flag to ensure cinematic assets are loaded into memory only once. */
    private static boolean framesLoaded = false;

    /** The signature sound effect triggered at the start of the cast. */
    private static final AudioClip snapSound;

    static {
        snapSound = new AudioClip(
                Objects.requireNonNull(XSnap.class.getResource("/sound/xsnap.mp3")).toExternalForm()
        );
    }

    /**
     * Constructs the XSnap spell and initializes the cinematic frame cache.
     * * This constructor implements a "Lazy Loading" pattern for assets; it checks
     * if the static {@code frames} list is populated and, if not, loads the 17-frame
     * animation sequence from the resources folder.
     *
     * @param enemies    List of enemies to be affected by the ultimate damage.
     * @param root       The main UI Pane for rendering the full-screen cinematic overlay.
     * @param pauseGame  A Runnable interface to pause the game logic.
     * @param resumeGame A Runnable interface to resume the game logic.
     */
    public XSnap(List<Enemy> enemies,
                 Pane root,
                 Runnable pauseGame,
                 Runnable resumeGame) {

        this.name = "X Snap";
        this.damage = 1000;
        this.range = 20;

        this.enemies = enemies;
        this.root = root;
        this.pauseGame = pauseGame;
        this.resumeGame = resumeGame;
        if (!framesLoaded) {
            for (int i = 1; i <= 17; i++) {
                String path = String.format(
                        "/video/xsnap_frames/frame_%03d.png", i);

                frames.add(new Image(
                        Objects.requireNonNull(getClass().getResource(path)).toExternalForm()
                ));
            }
            framesLoaded = true;
        }
    }

    /**
     * Executes the cinematic ultimate ability sequence.
     * * The execution pipeline follows a sophisticated multi-stage process:
     * 1. Synchronization: Pauses the game logic to prevent entity movement during the scene.
     * 2. Visual Setup: Creates a black background overlay and an {@link ImageView}
     * bound to the window size.
     * 3. Fade-In: Uses {@link FadeTransition} to transition smoothly into the cinematic state.
     * 4. Frame Animation: Uses a {@link Timeline} to cycle through images at 25 FPS
     * (40ms intervals).
     * 5. Resolution and Cleanup: Fades out the overlay, removes cinematic nodes,
     * applies massive damage to all living enemies, and resumes the game engine.
     *
     * @param x The horizontal coordinate (not functionally used, but part of Spell contract).
     * @param y The vertical coordinate (not functionally used, but part of Spell contract).
     */
    @Override
    public void cast(double x, double y) {

        if (isCasting) return;
        isCasting = true;

        pauseGame.run();
        snapSound.play();
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.fitWidthProperty().bind(root.widthProperty());
        view.fitHeightProperty().bind(root.heightProperty());
        Rectangle blackBg = new Rectangle();
        blackBg.widthProperty().bind(root.widthProperty());
        blackBg.heightProperty().bind(root.heightProperty());
        blackBg.setFill(Color.BLACK);

        StackPane layer = new StackPane(blackBg, view);
        layer.setOpacity(0);
        layer.setMouseTransparent(true);

        root.getChildren().add(layer);
        layer.toFront();

        FadeTransition fadeIn =
                new FadeTransition(Duration.seconds(0.3), layer);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        Timeline timeline = new Timeline();

        for (int i = 0; i < frames.size(); i++) {
            final int index = i;

            KeyFrame kf = new KeyFrame(
                    Duration.millis(i * 40), // ~25 FPS
                    _ -> view.setImage(frames.get(index))
            );

            timeline.getKeyFrames().add(kf);
        }

        timeline.setOnFinished(_ -> {

            FadeTransition fadeOut =
                    new FadeTransition(Duration.seconds(0.3), layer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(_ -> {

                root.getChildren().remove(layer);

                for (Enemy en : enemies) {
                    if (en.isAlive()) {
                        en.takeDamage(damage);
                    }
                }

                resumeGame.run();
                isCasting = false;
            });

            fadeOut.play();
        });

        timeline.play();
    }
}