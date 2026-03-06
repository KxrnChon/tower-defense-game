package application;

import bullets.Bullet;
import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.GameLogic;

import java.util.Objects;

/**
 * The Main class serves as the central orchestrator and entry point for the JavaFX application.
 * * It is responsible for initializing the primary Stage, managing global scene transitions
 * (such as switching between the Menu and Gameplay), and establishing the top-level
 * user interface hierarchy. This class also implements the high-level rendering loop
 * for dynamic projectiles and bridges hardware input events to the game's internal systems.
 *
 * @author Pun
 * @version 1.0
 */
public class Main extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Stage primaryStage;

    /**
     * The primary entry point for the JavaFX application.
     * * This class is responsible for:
     * 1. Window Initialization: Setting up the primary stage and global window constraints.
     * 2. Scene Orchestration: Managing transitions between the Main Menu and the active Gameplay scene.
     * 3. Global Input Routing: Binding low-level JavaFX events (Keyboard/Mouse) to the internal Input system.
     * 4. Graphics Context Management: Providing a high-performance Canvas for real-time projectile rendering.
     */
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        stage.setTitle("CETD03");
        stage.setScene(createMenuScene());
        primaryStage.setResizable(false);
        stage.show();
    }

    // ---------------- MENU ----------------
    /**
     * Constructs the Main Menu interface.
     * * Utilizes a {@link VBox} layout to align brand assets and navigation buttons.
     * Implements responsive UI feedback via localized styling and hover animations.
     *
     * @return A {@link Scene} object representing the game's splash and menu screen.
     */
    private Scene createMenuScene() {

        StackPane root = new StackPane();
        root.setPrefSize(WIDTH, HEIGHT);
        root.setStyle("-fx-background-color: black;");

        VBox menu = new VBox(25);
        menu.setAlignment(Pos.CENTER);

        // -------- TITLE IMAGE --------
        ImageView titleImage = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/cetd03.png")))
        );

        titleImage.setFitWidth(450);
        titleImage.setPreserveRatio(true);

        // -------- BUTTONS --------
        Button startBtn = new Button("START GAME");
        Button exitBtn = new Button("EXIT");

        styleButton(startBtn);
        styleButton(exitBtn);

        exitBtn.setTranslateY(-10);

        startBtn.setOnAction(_ -> primaryStage.setScene(createGameScene()));
        exitBtn.setOnAction(_ -> primaryStage.close());

        menu.getChildren().addAll(titleImage, startBtn, exitBtn);
        root.getChildren().add(menu);

        return new Scene(root, WIDTH, HEIGHT);
    }

    private void styleButton(Button btn) {

        btn.setStyle("""
        -fx-background-color: #ff6a00;
        -fx-text-fill: white;
        -fx-font-size: 20px;
        -fx-font-weight: bold;
        -fx-background-radius: 20;
        -fx-padding: 12 50 12 50;
    """);

        // Glow effect
        Glow glow = new Glow(0.0);
        btn.setEffect(glow);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), btn);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), btn);
        scaleDown.setToX(1);
        scaleDown.setToY(1);

        btn.setOnMouseEntered(_ -> {
            glow.setLevel(0.8);
            scaleUp.playFromStart();
        });

        btn.setOnMouseExited(_ -> {
            glow.setLevel(0.0);
            scaleDown.playFromStart();
        });
    }

    // ---------------- GAME ----------------
    /**
     * Initializes the gameplay environment and its core loop subsystems.
     * * Key procedures include:
     * 1. Layer Separation: Initializing the {@link GameLogic} with a dedicated visual layer.
     * 2. Canvas Rendering: Starting a secondary {@link AnimationTimer} specifically
     * for high-frequency bullet draws to optimize performance.
     * 3. Input Mapping: Attaching event filters for mouse movement, clicks, and ESCAPE key navigation.
     *
     * @return A {@link Scene} object configured with the game layer and rendering context.
     */
    private Scene createGameScene() {

        StackPane root = new StackPane();
        root.setPrefSize(WIDTH, HEIGHT);

        Pane gameLayer = new Pane();
        gameLayer.setPrefSize(WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setMouseTransparent(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        GameLogic gameLogic = new GameLogic(gameLayer, () -> primaryStage.setScene(createMenuScene()));

        AnimationTimer bulletRenderer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gc.clearRect(0, 0, WIDTH, HEIGHT);

                for (Bullet bullet : gameLogic.getBullets()) {
                    bullet.draw(gc);
                }
            }
        };
        bulletRenderer.start();

        gameLogic.startGame();

        gameLayer.getChildren().add(canvas);
        root.getChildren().add(gameLayer);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            input.Input.onKeyPressed(e);

            if (e.getCode() == KeyCode.ESCAPE && gameLogic.getSelectedCard() == null) {
                gameLogic.stopGame();
                bulletRenderer.stop();
                primaryStage.setScene(createMenuScene());
            }
        });

        scene.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> input.Input.onMousePressed(e.getButton()));

        scene.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_RELEASED, e -> input.Input.onMouseReleased(e.getButton()));

        scene.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, input.Input::onKeyReleased);
        scene.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_MOVED, e -> input.Input.setMousePosition(e.getX(), e.getY()));
        return scene;
    }

    /**
     * The standard Java entry point that launches the JavaFX application lifecycle.
     * * This method calls {@link #launch(String...)}, which internalizes the
     * initialization of the JavaFX Runtime, creates the Application thread,
     * and eventually triggers the {@link #start(Stage)} method.
     *
     * @param args Command-line arguments passed to the application.
     */
    static void main(String[] args) {
        launch(args);
    }
}