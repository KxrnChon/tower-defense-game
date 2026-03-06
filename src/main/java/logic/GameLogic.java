package logic;

import java.util.Iterator;
import bullets.Bullet;
import Interface.Mergeable;
import card.Card;
import card.SpellCard;
import card.TowerCard;
import enemy.*;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import input.Input;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import path.EnemyPath;
import path.Waypoint;
import spell.*;
import tower.*;
import view.*;

import java.util.*;
import java.util.function.Supplier;

/**
 * The GameLogic class serves as the central controller and engine for the game.
 * * It implements the Singleton pattern to provide a global point of access and
 * coordinates interactions between various game subsystems including enemy waves,
 * tower management, bullet physics, and UI rendering.
 * * Key responsibilities include:
 * 1. Orchestrating the main game loop via {@link AnimationTimer}.
 * 2. Managing the lifecycle of game entities (Spawn, Update, Cleanup).
 * 3. Handling card-based interactions (Tower placement and Spell casting).
 * 4. Managing game state transitions (Playing, Wave Clear, Game Over).
 * 5. Synchronizing the Model data with View representations.
 *
 * @author Pun
 * @version 1.0
 */
public class GameLogic {

    /** JavaFX layers for organizing visual elements. */
    private final Pane gameLayer = new Pane();
    private final Pane uiLayer = new Pane();

    /** Visual indicator showing the effective radius of a selected tower or spell. */
    private Circle previewRange;

    /** The main driving force of the game, executing updates every frame. */
    private AnimationTimer gameLoop;

    /** Specialized drawing surface for rendering area-of-effect (AOE) gas clouds. */
    private Canvas gasCanvas = null;

    /** Flags for managing the current operational state of the game session. */
    private boolean cardBarOpen = true;
    private boolean waveActive = false;
    private boolean paused = false;

    /** Audio assets for feedback on gameplay events and background atmosphere. */
    private AudioClip hpLossSound;
    private AudioClip cardSound;
    private AudioClip mergeSound;
    private AudioClip winSound;
    private AudioClip gameOverSound;
    private MediaPlayer backgroundMusic;

    /** Stores the timestamp of the previous frame to calculate deltaTime. */
    private long lastTime;

    /** Current wave controller and its numerical progression index. */
    private Wave currentWave;
    private int currentWaveNum = 1;

    /** Pools for random card generation. */
    private final List<Supplier<TowerCard>> towerCardPool = new ArrayList<>();
    private final List<Supplier<SpellCard>> spellCardPool = new ArrayList<>();

    /** Core collections for managing active game entities. */
    private final List<Tower> towers = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();

    /** Visual representations (Views) of the game entities. */
    private final List<EnemyView> enemyViews = new ArrayList<>();
    private final List<TowerView> towerViews = new ArrayList<>();

    /** Management of the currently held card and the UI components of the card system. */
    private Card selectedCard = null;
    private final CardBar cardBar;
    private final List<CardView> cardViews = new ArrayList<>();

    /** * Floating visual elements for the 'Ghost' preview of a tower
     * before it is placed on the map.
     */
    private final ImageView previewImageView;
    private final double previewSize = 50.0;

    /** Graphical elements for the background environment and the player's health HUD. */
    private ImageView backgroundImageView;
    private Rectangle hpBarForeground;
    private Text hpTextLabel;

    /** Callback function to handle the transition back to the main menu scene. */
    private final Runnable onExitToMenu;

    /** Predefined sequence of waypoints that all ground and air enemies must follow. */
    private EnemyPath enemyPath;

    /** Player's base durability and the current state (Playing or Game Over). */
    private int baseHP = 100;
    private GameState gameState = GameState.PLAYING;

    /** The single active instance of GameLogic for global access. */
    private static GameLogic instance;

    /**
     * Provides a global access point to the GameLogic instance.
     * @return The active {@link GameLogic} singleton instance.
     */
    public static GameLogic getInstance() {
        return instance;
    }

    /**
     * Retrieves the current list of active projectiles in the game.
     * @return A list of {@link Bullet} objects currently being processed.
     */
    public List<Bullet> getBullets() {
        return bullets;
    }

    /**
     * principal constructor for GameLogic that initializes the entire game engine.
     * * Core responsibilities include:
     * 1. Layer Management: Separates the 'gameLayer' (entities) from the 'uiLayer' (HUD).
     * 2. Asset Loading: Asynchronously loads background textures and initializes audio buffers.
     * 3. Card System: Populates a pool of card suppliers using the Factory Pattern for randomized drafting.
     * 4. User Interface: Configures the ScrollPane for the card bar with specialized event filtering.
     * 5. State Initialization: Sets up navigation paths, enemy waves, and the master AnimationTimer.
     *
     * @param root The main container (Pane) where all game visual layers are attached.
     * @param onExitToMenu A callback (Runnable) to handle transitions back to the main menu.
     */
    public GameLogic(Pane root, Runnable onExitToMenu) {
        instance = this;
        this.onExitToMenu = onExitToMenu;

        root.getChildren().addAll(gameLayer, uiLayer);
        uiLayer.setPickOnBounds(false);

        String bgPath = "/image/BG.png";
        try {
            Image bgImg = new Image(Objects.requireNonNull(getClass().getResource(bgPath)).toExternalForm());
            backgroundImageView = new ImageView(bgImg);

            backgroundImageView.setFitWidth(800);
            backgroundImageView.setFitHeight(600);
            backgroundImageView.setPreserveRatio(false);
            backgroundImageView.setMouseTransparent(true);

            gameLayer.getChildren().addFirst(backgroundImageView);
            System.out.println("Background picture loaded successfully: " + bgPath);
        } catch (Exception e) {
            System.err.println("Cannot load background picture: " + bgPath);
        }

        cardBar = new CardBar();
        //add TowerCard to Pool
        for(int i = 1 ; i <= 30 ; i++) {
            towerCardPool.add(() -> new TowerCard(() -> new ZapTower(0,0)));
            towerCardPool.add(() -> new TowerCard(() -> new RangeTower(0,0)));
            if(i <= 20){
                towerCardPool.add(() -> new TowerCard(() -> new GasTower(0,0)));
                towerCardPool.add(() -> new TowerCard(() -> new IceTower(0,0)));
            }
        }
        //add SpellCard to Pool
        for(int i = 1 ; i <= 33 ; i++){
            spellCardPool.add(() -> new SpellCard(() -> new FireBall(enemies, gameLayer)));
            spellCardPool.add(() -> new SpellCard(() -> new FrostBite(enemies, gameLayer)));
            spellCardPool.add(() -> new SpellCard(() -> new HealingBase(this,gameLayer)));
        }
        spellCardPool.add(() -> new SpellCard(() -> new XSnap(enemies, root, this::pauseGame, this::resumeGame)));

        ScrollPane scrollPane = new ScrollPane(cardBar);

        scrollPane.setPrefHeight(180);
        scrollPane.setPrefWidth(800);
        scrollPane.setFocusTraversable(false);
        scrollPane.setStyle("""
        -fx-background-color: transparent;
        -fx-background: transparent;
        """);
        scrollPane.setFitToHeight(true);

        scrollPane.setLayoutY(600 - 180);
        uiLayer.getChildren().add(scrollPane);
        scrollPane.setPannable(false);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setFitToWidth(false);

        scrollPane.setFocusTraversable(false);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, e -> {

            double delta = e.getDeltaY();

            double contentWidth = cardBar.getBoundsInLocal().getWidth();
            double viewportWidth = scrollPane.getViewportBounds().getWidth();

            if (contentWidth <= viewportWidth) {
                return;
            }

            double scrollAmount = delta / (contentWidth - viewportWidth);

            double newValue = scrollPane.getHvalue() - scrollAmount;

            newValue = Math.max(0, Math.min(1, newValue));

            scrollPane.setHvalue(newValue);

            e.consume();
        });
        scrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {

            Node node = (Node) e.getTarget();

            while (node != null && !(node instanceof CardView)) {
                node = node.getParent();
            }

            if (node != null) {
                e.consume();
            }
            else {
                MouseEvent newEvent = e.copyFor(root, root);
                root.fireEvent(newEvent);
                e.consume();
            }
        });
        Button toggleBtn = new Button("Cards");
        toggleBtn.setLayoutX(20);
        toggleBtn.setLayoutY(550);
        toggleBtn.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #5f9cff, #2d6cdf);
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 30;
            -fx-padding: 8 18 8 18;
            -fx-cursor: hand;
        """);
        StackPane.setAlignment(toggleBtn, Pos.BOTTOM_RIGHT);
        toggleBtn.setTranslateY(-190);
        toggleBtn.setTranslateX(-20);
        root.getChildren().add(toggleBtn);
        toggleBtn.setOnAction(_ -> {

            TranslateTransition tt = new TranslateTransition(Duration.millis(300), scrollPane);

            if (cardBarOpen) {
                tt.setToY(180);
                cardBarOpen = false;
            } else {
                tt.setToY(0);
                cardBarOpen = true;
            }

            tt.play();
        });
        //test
        createPath();
        createWave(currentWaveNum);
        createTower();
        setupLoop();

        previewImageView = new ImageView();
        previewImageView.setFitWidth(previewSize);
        previewImageView.setFitHeight(previewSize);
        previewImageView.setPreserveRatio(true);
        previewImageView.setOpacity(0);
        previewImageView.setManaged(false);
        previewImageView.setMouseTransparent(true);

        gameLayer.getChildren().add(previewImageView);
        previewImageView.toFront();
        if (backgroundImageView != null) {
            backgroundImageView.toBack();
        }
        loadSounds();
        playBackgroundMusic();
    }

    /**
     * Initiates the game by starting the animation timer and activating wave logic.
     */
    public void startGame() {
        System.nanoTime();
        waveActive = true;
        gameLoop.start();
    }

    /**
     * Initializes the {@link AnimationTimer} which drives the game's temporal progression.
     * * This method sets up the core loop that calculates {@code deltaTime}—the time
     * elapsed between frames—to ensure frame-rate independent movement and logic
     * processing.
     */
    private void setupLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime =
                        (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(deltaTime);
            }
        };
    }


    /**
     * The central processing hub executed every frame.
     * * This method orchestrates the following game phases:
     * 1. State Validation: Checks for Pause or Game Over states.
     * 2. Input Processing: Updates tower placement previews based on mouse coordinates.
     * 3. Entity Simulation: Updates AI, movement, and combat for Towers, Enemies, and Projectiles.
     * 4. Visual Synchronization: Synchronizes Model data with View components (EnemyView).
     * 5. Maintenance: Handles cleanup of dead entities and checks win/loss conditions.
     *
     * @param deltaTime The time in seconds passed since the last frame.
     */
    void update(double deltaTime) {
        if(paused) return;
        if (gameState == GameState.GAME_OVER) return;
        if (waveActive) {
            currentWave.update(deltaTime);
        }

        //keyboard input
        updateInput();

        updateTowers(deltaTime, enemies);
        updateEnemies(deltaTime);
        updateBullets(deltaTime);
        drawGasEffects();
        double mouseX = Input.getMouseX();
        double mouseY = Input.getMouseY();

        if (selectedCard != null) {
            previewImageView.setTranslateX(mouseX - (previewSize / 2));
            previewImageView.setTranslateY(mouseY - (previewSize / 2));
            previewImageView.setOpacity(0.6);
            previewImageView.setVisible(true);
            previewImageView.toFront();

            canPlace(mouseX, mouseY);
            DropShadow glow = new DropShadow();
            glow.setRadius(20);
            glow.setSpread(0.7);
            List<String> cardName = new ArrayList<>(
                    List.of("Gas Tower", "Ice Tower", "Zap Tower", "Range Tower")
            );
            if(selectedCard instanceof SpellCard) {
                glow.setColor(Color.LIME);
                previewRange.setStroke(Color.LIME);
            }
            else if(canPlace(mouseX,mouseY)){
                glow.setColor(Color.LIME);
                previewRange.setStroke(Color.LIME);
            }
            else if(getTowerAt(mouseX,mouseY) instanceof Mergeable && cardName.contains(selectedCard.getName()) && !Objects.requireNonNull(getTowerAt(mouseX, mouseY)).getName().equals(selectedCard.getName())){
                glow.setColor(Color.LIGHTBLUE);
                previewRange.setStroke(Color.LIGHTBLUE);
            }
            else {
                glow.setColor(Color.RED);
                previewRange.setStroke(Color.RED);
            }
            previewImageView.setEffect(glow);
            previewImageView.toFront();
        }

        // checkCollisions();
        // checkEndRound();

        //ui
        for(EnemyView ev : enemyViews){
            ev.sync();
        }

        checkWaveEnd();

        cleanupEnemies();
        drawHP();
    }

    /**
     * Validates whether a tower can be placed at the specified coordinates.
     * * The method performs two primary checks:
     * 1. Path Collision: Ensures the location does not overlap with the enemy navigation path.
     * 2. Entity Overlap: Ensures the location is not already occupied by another tower.
     *
     * @param x The horizontal coordinate for placement.
     * @param y The vertical coordinate for placement.
     * @return true if the location is valid for a new tower; false otherwise.
     */
    public boolean canPlace(double x,double y) {
        return (!enemyPath.isOnPath(x,y)  && !isOverlappingTower(x, y));
    }

    /**
     * Processes user interactions for gameplay actions such as selection, placement, and fusion.
     * * This method acts as the bridge between the {@link input.Input} system and game world changes.
     * * Key functionalities include:
     * 1. Selection Cancellation: Deselects cards and clears previews on a Right-Click.
     * 2. Dynamic Preview: Updates the tower/spell range visualization based on mouse movement.
     * 3. Fusion Triggering: Initiates tower merging if a card is dropped over a {@link Interface.Mergeable} unit.
     * 4. Entity Deployment: Spawns towers or casts spells upon a valid Left-Click.
     * 5. Inventory Management: Removes the utilized card from the UI upon successful action.
     */
    public void updateInput(){

        if (Input.isRightClick()) {
            selectedCard = null;

            previewImageView.setOpacity(0);
            previewImageView.setEffect(null);
            previewImageView.setImage(null);

            hideRangePreview();

            for (CardView cv : cardViews) {
                cv.setSelected(false);
            }
        }
        if (selectedCard != null) {

            double x = Input.getMouseX();
            double y = Input.getMouseY();

            showRangePreview(x, y, selectedCard.getRange());

            if (Input.isLeftClick()) {

                Tower existing = getTowerAt(x, y);

                if (existing instanceof Mergeable && selectedCard instanceof TowerCard tc) {
                    Tower ingredient = tc.summon();
                    performFusion(existing, ingredient);
                    if(!Objects.equals(existing.getName(), ingredient.getName())){
                        removeSelectedCard();
                        selectedCard = null;
                        previewImageView.setOpacity(0);
                        previewImageView.setEffect(null);
                        previewImageView.setImage(null);
                        hideRangePreview();
                    }
                    return;
                }

                if (selectedCard instanceof TowerCard && canPlace(x, y)) {

                    Tower tower = (Tower) selectedCard.summon();
                    tower.setPosition(x, y);
                    String imagePath = getImagePathForTower(tower);
                    spawnTower(tower, imagePath, previewSize);

                    CardView toRemove = null;
                    for (Node node : cardBar.getChildren()) {
                        if (node instanceof CardView cv && cv.getCard() == selectedCard) {
                            toRemove = cv;
                            break;
                        }
                    }

                    if (toRemove != null) {
                        cardBar.getChildren().remove(toRemove);
                        cardViews.remove(toRemove);
                    }

                    selectedCard = null;
                    previewImageView.setOpacity(0);
                    previewImageView.setEffect(null);
                    previewImageView.setImage(null);
                    hideRangePreview();

                    for (CardView cv : cardViews) {
                        cv.setSelected(false);
                    }
                }
                if (selectedCard instanceof SpellCard sc) {

                    if (Input.isLeftClick()) {
                        Spell spell = sc.summon();
                        spell.cast(Input.getMouseX(), Input.getMouseY());

                        removeSelectedCard();
                    }

                    return;
                }
            }
        }

        Input.update();
    }

    /**
     * Registers a new tower into the game engine and initializes its visual component.
     * * This method handles the synchronization between the Model (Tower) and the View (TowerView).
     * It adds the tower to the logic list, creates its graphical representation on the
     * game layer, and triggers the associated placement sound effect.
     *
     * @param tower     The {@link Tower} model instance to be spawned.
     * @param imagePath The file path to the tower's sprite resource.
     * @param size      The display size of the tower in pixels.
     */
    public void spawnTower(Tower tower, String imagePath, double size) {
        towers.add(tower);

        TowerView view = new TowerView(tower, imagePath, size);
        towerViews.add(view);

        gameLayer.getChildren().add(view);
        if (tower.getPlaceSoundPath() != null) {playSound(tower.getPlaceSoundPath());}
    }

    /**
     * Instantiates an enemy unit and attaches it to the game world.
     * * Utilizing the {@link EnemyViewFactory}, this method ensures that the correct
     * visual representation is created based on the enemy type. It maintains
     * consistency between the active enemy list and the JavaFX scene graph.
     *
     * @param enemy The {@link Enemy} model instance produced by a wave spawner.
     * @param spawn The {@link EnemySpawn} configuration defining the unit's attributes.
     */
    public void spawnEnemy(Enemy enemy, EnemySpawn spawn) {
        enemies.add(enemy);

        EnemyView view = EnemyViewFactory.createView(enemy,spawn);
        enemyViews.add(view);

        gameLayer.getChildren().add(view);
    }

    /**
     * Updates the logic for all active towers on the field.
     * * Each tower processes its own AI, including target acquisition within the
     * enemy list and projectile instantiation into the bullet list.
     *
     * @param deltaTime Time elapsed since the last frame.
     * @param enemies   List of current potential targets.
     */
    public void updateTowers(double deltaTime, List<Enemy> enemies){
        for (Tower tower : towers) {
            tower.update(deltaTime, enemies, bullets);
        }
    }

    /**
     * Manages the behavior, health checks, and goal-reaching logic for all enemies.
     * * This method performs several critical checks:
     * 1. Death Cleanup: Removes enemies whose health has dropped to zero.
     * 2. Boss Special Logic: Triggers persistent base attacks for Boss-type units.
     * 3. Goal Detection: Deducts base HP and triggers visual/audio feedback when
     * enemies reach the end of the path.
     * 4. Game Over Condition: Monitors base health and transitions the engine
     * to the GAME_OVER state when HP reaches zero.
     *
     * @param deltaTime Time elapsed since the last frame.
     */
    public void updateEnemies(double deltaTime){

        Iterator<Enemy> iterator = enemies.iterator();

        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(deltaTime);
            if (!enemy.isAlive()) {
                iterator.remove();
                continue;
            }
            if (enemy instanceof Boss boss) {
                boss.attackBase(deltaTime);
            }
            else if (enemy.hasReachedEnd()) {
                baseHP -= enemy.getDamage();
                if (hpLossSound != null) hpLossSound.play();
                playBaseHitEffect();
                enemy.die();
                iterator.remove();
            }
        }

        if (baseHP <= 0) {
            gameState = GameState.GAME_OVER;
            waveActive = false;
            bullets.clear();
            enemies.clear();
            if (gameOverSound != null) gameOverSound.play();
            showGameOverUI();
        }
    }

    /**
     * Removes visual representations of deceased enemies from the game scene.
     * * This method synchronizes the View layer with the Model layer by iterating
     * through the {@code enemyViews} collection and removing nodes from the
     * {@code gameLayer} if their corresponding {@link Enemy} model is no longer alive.
     */
    private void cleanupEnemies() {
        Iterator<EnemyView> it = enemyViews.iterator();

        while (it.hasNext()) {
            EnemyView ev = it.next();

            if (!ev.getEnemy().isAlive()) {
                gameLayer.getChildren().remove(ev);
                it.remove();
            }
        }
    }

    /**
     * Updates the trajectory of all active projectiles and removes those that have collided.
     * * It performs a two-step process:
     * 1. Iterates through all {@link Bullet} instances to calculate new positions.
     * 2. Utilizes a predicate-based removal to clear bullets that have flagged
     * a successful hit from memory.
     *
     * @param deltaTime Time elapsed since the last frame update.
     */
    public void updateBullets(double deltaTime){
        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
        }

        bullets.removeIf(b -> b.hasHit());
    }

    /**
     * Performs a proximity check to prevent towers from being placed too close to one another.
     * * It calculates the Euclidean distance between the proposed coordinates and
     * all existing tower positions. A safety margin is applied to ensure
     * visual clarity and prevent overlapping sprites.
     *
     * @param x The proposed X-coordinate.
     * @param y The proposed Y-coordinate.
     * @return true if the position overlaps with an existing tower; false otherwise.
     */
    private boolean isOverlappingTower(double x, double y) {
        for (Tower tower : towers) {
            double dx = x - tower.getX();
            double dy = y - tower.getY();
            double distance = Math.hypot(dx, dy);

            double safetyMargin = previewSize / 2.0;

            if (distance < safetyMargin + (double) 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a new gameplay wave and replenishes the player's card hand.
     * * This method orchestrates the difficulty progression by:
     * 1. Card Drafting: Randomly selecting tower and spell cards from the pools.
     * 2. Difficulty Scaling: Calculating enemy HP and damage based on the wave number.
     * 3. Wave Composition: Building a randomized sequence of enemies (Skeletons,
     * Bombers, Orcs, Bats) followed by a specialized Boss unit.
     * 4. Pacing: Implementing a temporal delay between spawns to ensure balanced flow.
     *
     * @param waveNum The current wave index used to scale enemy difficulty attributes.
     */
    private void createWave(int waveNum) {

        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(towerCardPool.size());
            Card card = towerCardPool.get(index).get();
            cardBar.getChildren().add(new CardView(card, this::selectCard));
        }
        int index = random.nextInt(spellCardPool.size());
        Card card = spellCardPool.get(index).get();
        cardBar.getChildren().add(new CardView(card, this::selectCard));

        currentWave = new Wave();
        double delay = 0;



        List<Integer> enemyTypes = new ArrayList<>();

        for (int i = 0; i < 10; i++) enemyTypes.add(0);

        for (int i = 0; i < 5; i++) enemyTypes.add(1);

        if (waveNum >= 3) {

            for (int i = 0; i < 4; i++) enemyTypes.add(2);

            for (int i = 0; i < 4; i++) enemyTypes.add(3);
        }

        Collections.shuffle(enemyTypes);

        for (int type : enemyTypes) {

            switch (type) {

                case 0 -> currentWave.addSpawn(new EnemySpawn(
                        this,
                        () -> new Skeleton(enemyPath, 10 * waveNum, 10 * waveNum, 90),
                        delay,
                        1,
                        0,
                        20,
                        Color.RED,
                        VisualShape.CIRCLE
                ));

                case 1 -> currentWave.addSpawn(new EnemySpawn(
                        this,
                        () -> new Bomber(enemyPath, 5 * waveNum, 5 * waveNum, 120),
                        delay,
                        1,
                        0,
                        15,
                        Color.LIGHTSEAGREEN,
                        VisualShape.SQUARE
                ));

                case 2 -> currentWave.addSpawn(new EnemySpawn(
                        this,
                        () -> new Orc(enemyPath, 20 * waveNum, 20 * waveNum, 60),
                        delay,
                        1,
                        0,
                        30,
                        Color.CORAL,
                        VisualShape.HEXAGON
                ));

                case 3 -> currentWave.addSpawn(new EnemySpawn(
                        this,
                        () -> new Bat(enemyPath, 5 * waveNum, 5 * waveNum, 45),
                        delay,
                        1,
                        0,
                        20,
                        Color.LIGHTBLUE,
                        VisualShape.CIRCLE
                ));
            }

            delay += 0.5;
        }


        currentWave.addSpawn(new EnemySpawn(
                this,
                () -> new Boss(enemyPath, 100 * waveNum, 20 * waveNum, 40),
                delay + 1,
                1,
                0,
                100,
                Color.SPRINGGREEN,
                VisualShape.HEXAGON
        ));
    }

    /**
     * Sets the currently active card for the player and prepares the visual ghost preview.
     * * Upon selecting a card, the system:
     * 1. Provides auditory feedback via {@code cardSound}.
     * 2. Loads the corresponding tower sprite for the placement preview.
     * 3. Sets visual properties (opacity and Z-order) for the "ghost" image
     * following the cursor.
     *
     * @param card The {@link Card} object selected by the player from the UI.
     */
    private void selectCard(Card card) {
        if (cardSound != null) cardSound.play();
        this.selectedCard = card;
        if (card instanceof TowerCard tc) {
            Tower dummy = tc.summon();
            String path = getImagePathForTower(dummy);
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
                previewImageView.setImage(img);
                previewImageView.setOpacity(0.5);
                previewImageView.toFront();
            } catch (Exception e) {
                System.err.println("cannot load image in selectCard: " + path);
            }
        }
    }

    /**
     * Initializes the enemy navigation route and renders the visual path textures.
     * * This method defines a series of {@link Waypoint} coordinates that dictate
     * enemy movement. It then procedurally populates the segments between waypoints
     * with tiled textures to provide a visual representation of the road on
     * the {@code gameLayer}.
     */
    private void createPath() {
        enemyPath = new EnemyPath(List.of(
                new Waypoint(-10, 350),
                new Waypoint(300, 350),
                new Waypoint(300, 150),
                new Waypoint(600, 150),
                new Waypoint(600, 450),
                new Waypoint(810, 450)
        ));

        PathView pathView = new PathView(enemyPath);
        gameLayer.getChildren().addFirst(pathView); // behind everything

        Image stoneTile = new Image(Objects.requireNonNull(getClass().getResource("/image/Path.png")).toExternalForm());
        double tileSize = 40;

        for (int i = 0; i < enemyPath.getWaypoints().size() - 1; i++) {
            Waypoint start = enemyPath.getWaypoints().get(i);
            Waypoint end = enemyPath.getWaypoints().get(i + 1);

            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();
            double distance = Math.hypot(dx, dy);

            int stonesNeeded = (int) (distance / (tileSize * 0.8));

            for (int j = 0; j <= stonesNeeded; j++) {
                double t = (double) j / stonesNeeded;
                double posX = start.getX() + (dx * t);
                double posY = start.getY() + (dy * t);

                ImageView stone = new ImageView(stoneTile);
                stone.setFitWidth(tileSize);
                stone.setFitHeight(tileSize);
                stone.setLayoutX(posX - tileSize / 2);
                stone.setLayoutY(posY - tileSize / 2);
                stone.setMouseTransparent(true);

                gameLayer.getChildren().add(1, stone);
            }
        }
    }

    private void createTower() {
    }

    /**
     * Applies a stun status effect to towers within a specific circular area.
     * * This is typically triggered by enemy abilities (e.g., Boss or Bomber).
     * It iterates through active towers and calculates distance to determine
     * if they fall within the effect radius.
     *
     * @param position The center point of the effect.
     * @param radius   The maximum range of the stun effect.
     * @param duration How long the towers will remain inactive.
     */
    public void stunNearbyTowers(Point2D position, double radius, double duration) {

        for (Tower tower : towers) {

            double distance = tower.getPosition().distance(position);

            if (distance <= radius) {
                tower.stun(duration);
            }
        }
    }

    /**
     * Renders a visual feedback animation for the stun area-of-effect (AOE).
     * * Creates a semi-transparent orange circle that gradually fades away using
     * a {@link javafx.animation.FadeTransition}, providing clear visual cues
     * for the duration of the status effect.
     *
     * @param position The coordinate to display the effect.
     * @param radius   The size of the visual circle.
     * @param duration The lifespan of the animation in seconds.
     */
    public void showStunEffect(Point2D position, double radius, double duration) {

        Circle stunCircle = new Circle(position.getX(), position.getY(), radius);
        stunCircle.setFill(Color.rgb(255, 165, 0, 0.3));
        stunCircle.setStroke(Color.ORANGE);
        stunCircle.setStrokeWidth(3);

        gameLayer.getChildren().add(stunCircle);
        stunCircle.toFront();

        javafx.animation.FadeTransition fade =
                new javafx.animation.FadeTransition(Duration.seconds(duration), stunCircle);

        fade.setFromValue(0.8);
        fade.setToValue(0.0);

        fade.setOnFinished(_ -> gameLayer.getChildren().remove(stunCircle));

        fade.play();
    }

    /**
     * Terminate the game session and performs a complete resource cleanup.
     * * This method ensures the application returns to a clean state by:
     * 1. Halting the {@link javafx.animation.AnimationTimer} loop.
     * 2. Clearing all entity collections and visual nodes from the scene graph.
     * 3. Resetting input buffers and logical timestamps.
     */
    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }

        lastTime = 0;

        towers.clear();
        enemies.clear();
        enemyViews.clear();
        towerViews.clear();

        gasCanvas = null;
        gameLayer.getChildren().clear();

        Input.reset();
    }

    /**
     * Renders a dashed-line radius indicator at the target coordinates.
     * * Provides visual feedback for the effective range of a selected card.
     * If the indicator does not exist, it is initialized with a dashed stroke
     * pattern for a blueprint-like appearance.
     *
     * @param x     Center X-coordinate for the range circle.
     * @param y     Center Y-coordinate for the range circle.
     * @param range The radius of the circle representing attack range.
     */
    private void showRangePreview(double x, double y, double range) {
        if (previewRange == null) {
            previewRange = new Circle(x, y, range);
            previewRange.setStroke(Color.RED);
            previewRange.setStrokeWidth(1.5);
            previewRange.setFill(Color.TRANSPARENT);
            previewRange.getStrokeDashArray().addAll(5d, 5d);
            gameLayer.getChildren().add(previewRange);
        } else {
            previewRange.setCenterX(x);
            previewRange.setCenterY(y);
            previewRange.setRadius(range);
        }
    }

    /**
     * Removes the range indicator from the game scene and nullifies its reference.
     */
    private void hideRangePreview() {
        if (previewRange != null) {
            gameLayer.getChildren().remove(previewRange);
            previewRange = null;
        }
    }

    /**
     * Orchestrates the rendering of area-of-effect (AOE) gas clouds on a dedicated Canvas.
     * * This method implements a high-performance drawing routine that:
     * 1. Initializes a {@link javafx.scene.canvas.Canvas} layer if not present.
     * 2. Clears the drawing surface every frame to prevent visual trailing.
     * 3. Delegates drawing tasks to towers that implement {@link tower.AOETower}.
     */
    private void drawGasEffects() {

        if (gasCanvas == null) {
            gasCanvas = new Canvas(800, 600);
            gasCanvas.setMouseTransparent(true);
            gameLayer.getChildren().add(1, gasCanvas);
            gasCanvas.toFront();
            backgroundImageView.toBack();
        }

        GraphicsContext gc = gasCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 800, 600);

        for (Tower tower : towers) {
            if(tower instanceof AOETower){
                ((AOETower) tower).drawGas(gc);
            }
        }
    }

    /**
     * Retrieves the currently active card selected by the player from the UI.
     * * This method is used to verify player intent during the update loop,
     * particularly for rendering placement previews and handling fusion logic.
     *
     * @return The currently selected {@link card.Card} object.
     */
    public Object getSelectedCard() {
        return selectedCard;
    }

    /**
     * Maps a specific tower instance to its corresponding graphical asset path.
     * * This helper method facilitates the decoupling of the tower's logical state
     * from its visual representation by fetching the internal image path
     * defined within the {@link tower.Tower} class.
     *
     * @param tower The tower instance whose image path is required.
     * @return A string representing the relative resource path to the image file.
     */
    private String getImagePathForTower(Tower tower) {
        return tower.getImagePath();
    }

    /**
     * Evaluates the conditions required to conclude the current wave.
     * * A wave is considered finished only when:
     * 1. The game is currently in the {@code PLAYING} state.
     * 2. The {@link Wave} object has finished spawning all scheduled units.
     * 3. All active enemies have been cleared from the battlefield.
     * * Upon success, it triggers the wave-clear audio and displays the victory UI.
     */
    private void checkWaveEnd() {

        if (gameState != GameState.PLAYING) return;

        if (waveActive && enemies.isEmpty() && currentWave.isFinished()) {
            waveActive = false;
            if (winSound != null) winSound.play();
            showWaveEndUI();
        }
    }

    /**
     * Constructs and displays a modal overlay upon successful wave completion.
     * * This method programmatically creates a JavaFX UI containing:
     * - A semi-transparent backdrop to dim the game world.
     * - Status text showing the upcoming wave number.
     * - Interactive buttons to progress to the next wave or return to the menu.
     * - A {@link javafx.animation.FadeTransition} for a polished visual entry.
     */
    private void showWaveEndUI() {
        StackPane overlay = new StackPane();
        overlay.setPrefSize(800, 600);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setMaxSize(400, 350);

        container.setStyle("""
        -fx-background-color: rgba(30, 30, 30, 0.9);
        -fx-background-radius: 30;
        -fx-border-color: rgba(255, 255, 255, 0.2);
        -fx-border-width: 2;
        -fx-border-radius: 30;
    """);

        Text title = new Text("WAVE CLEAR!");
        title.setFill(Color.web("#ff6a00"));
        title.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-font-family: 'Arial Black';");

        Text subtitle = new Text("Next Wave: " + (currentWaveNum + 1));
        subtitle.setFill(Color.WHITE);
        subtitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button nextBtn = new Button("NEXT WAVE");
        Button exitBtn = new Button("EXIT TO MENU");

        applyMainButtonStyle(nextBtn);
        applyMainButtonStyle(exitBtn);

        nextBtn.setOnAction(_ -> {
            uiLayer.getChildren().remove(overlay);
            currentWaveNum++;
            createWave(currentWaveNum);
            waveActive = true;
        });

        exitBtn.setOnAction(_ -> {
            stopGame();
            if (onExitToMenu != null) {
                onExitToMenu.run();
            }
        });

        container.getChildren().addAll(title, subtitle, nextBtn, exitBtn);
        overlay.getChildren().add(container);

        // Fade In Effect
        overlay.setOpacity(0);
        uiLayer.getChildren().add(overlay);
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(400), overlay);
        ft.setToValue(1.0);
        ft.play();

    }

    /**
     * Constructs and displays the modal overlay when the player's health reaches zero.
     * * This method creates a high-contrast 'Game Over' screen that:
     * 1. Dims the game layer using a dark semi-transparent backdrop.
     * 2. Provides options to {@code RESTART} (resetting the state) or {@code EXIT} (returning to menu).
     * 3. Utilizes a {@link javafx.animation.FadeTransition} for a dramatic visual entrance.
     */
    private void showGameOverUI() {
        StackPane overlay = new StackPane();
        overlay.setPrefSize(800, 600);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setMaxSize(400, 300);

        container.setStyle("""
        -fx-background-color: rgba(30, 0, 0, 0.9);
        -fx-background-radius: 30;
        -fx-border-color: red;
        -fx-border-width: 2;
        -fx-border-radius: 30;
    """);

        Text title = new Text("GAME OVER");
        title.setFill(Color.RED);
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        Button restartBtn = new Button("RESTART");
        Button exitBtn = new Button("EXIT TO MENU");

        applyMainButtonStyle(restartBtn);
        applyMainButtonStyle(exitBtn);

        restartBtn.setOnAction(_ -> {
            uiLayer.getChildren().remove(overlay);
            resetGame();
        });

        exitBtn.setOnAction(_ -> {
            stopGame();
            if (onExitToMenu != null) {
                onExitToMenu.run();
            }
        });

        container.getChildren().addAll(title, restartBtn, exitBtn);
        overlay.getChildren().add(container);

        overlay.setOpacity(0);
        uiLayer.getChildren().add(overlay);

        javafx.animation.FadeTransition ft =
                new javafx.animation.FadeTransition(Duration.millis(400), overlay);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Applies a consistent visual theme and interactive animations to game buttons.
     * * Enhancements include:
     * - CSS-style gradients and border radii for a modern look.
     * - Hover effects using {@link javafx.scene.effect.Glow} for immediate visual feedback.
     * - {@link javafx.animation.ScaleTransition} to provide tactile-like 'pop' on hover.
     *
     * @param btn The JavaFX {@link Button} to be styled.
     */
    private void applyMainButtonStyle(Button btn) {
        btn.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #ffb400, #ff6a00);
        -fx-text-fill: white;
        -fx-font-size: 20px;
        -fx-font-weight: bold;
        -fx-background-radius: 25;
        -fx-padding: 12 50 12 50;
        -fx-border-color: #ffffff;
        -fx-border-width: 1;
        -fx-border-radius: 25;
        -fx-cursor: hand;
    """);

        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.0);
        btn.setEffect(glow);

        javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(Duration.millis(200), btn);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(Duration.millis(200), btn);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        btn.setOnMouseEntered(_ -> {
            glow.setLevel(0.8);
            scaleUp.playFromStart();
        });

        btn.setOnMouseExited(_ -> {
            glow.setLevel(0.0);
            scaleDown.playFromStart();
        });
    }

    /**
     * Updates the health bar and numerical HP label on the HUD.
     * * This method synchronizes the visual health bar with the {@code baseHP} value:
     * 1. Dynamically resizes the foreground rectangle based on health percentage.
     * 2. Updates the text label to show current/max HP.
     * 3. Changes the bar color to {@code RED} when health drops below 30% (Critical Alert).
     */
    private void drawHP() {

        if (hpBarForeground == null) {
            setupHPUI();
        }

        double hpPercentage = (double) baseHP / 100.0;
        hpBarForeground.setWidth(200 * Math.max(0, hpPercentage));

        hpTextLabel.setText("HP: " + Math.max(0, baseHP) + " / 100");

        if (hpPercentage < 0.3) {
            hpBarForeground.setFill(Color.RED);
        }
    }

    /**
     * Reduces the player's base health and triggers feedback effects.
     * * This method manages the core lose-condition by:
     * 1. Deducting damage points from {@code baseHP}.
     * 2. Playing the hit audio feedback.
     * 3. Triggering a visual screen-shake effect on the HP UI.
     * 4. Evaluating if health has reached zero to transition to {@link GameState#GAME_OVER}.
     *
     * @param dmg The amount of damage to inflict on the base.
     */
    public void damageBase(int dmg) {
        baseHP -= dmg;
        if (hpLossSound != null) hpLossSound.play();
        playBaseHitEffect();

        if (baseHP <= 0) {
            gameState = GameState.GAME_OVER;
            waveActive = false;
            bullets.clear();
            enemies.clear();
            showGameOverUI();
        }
    }

    /**
     * Executes a "Screen Shake" animation on the health bar container.
     * * Utilizes a {@link javafx.animation.TranslateTransition} with high-frequency
     * oscillation to provide tactile visual feedback when the player takes damage.
     */
    private void playBaseHitEffect() {
        if (hpBarForeground != null) {

            Node hpContainer = hpBarForeground.getParent();

            TranslateTransition tt =
                    new TranslateTransition(Duration.millis(50), hpContainer);

            tt.setFromX(20);
            tt.setToX(26);
            tt.setCycleCount(4);
            tt.setAutoReverse(true);

            tt.setOnFinished(_ -> {
                hpContainer.setTranslateX(0);
                hpContainer.setLayoutX(20);
                hpContainer.setLayoutY(20);
            });

            tt.play();
        }
    }

    /** @return The current health points as an integer. */
    public int getBaseHP() {
        return baseHP;
    }

    /**
     * Updates the base health to a specific value.
     * * This setter is utilized for state initialization during game resets
     * or by special game effects, such as healing spells, that modify
     * the player's durability directly.
     *
     * @param baseHP The new health value to set for the base.
     */
    public void setBaseHP(int baseHP) {
        this.baseHP = baseHP;
    }

    /**
     * Restores the game engine to its initial state for a new session.
     * * This comprehensive reset performs the following:
     * 1. Variable Reset: Re-initializes HP to 100 and wave count to 1.
     * 2. Memory Cleanup: Clears all entity lists (Enemies, Towers, Bullets).
     * 3. Scene Reconstruction: Wipes the JavaFX layers and re-adds essential
     * nodes like background and paths.
     * 4. Card Regeneration: Clears the hand and draws a fresh set of cards.
     */
    public void resetGame() {

        // reset base stat
        baseHP = 100;
        gameState = GameState.PLAYING;
        waveActive = false;
        lastTime = 0;

        // clear model
        enemies.clear();
        towers.clear();
        bullets.clear();
        enemyViews.clear();
        towerViews.clear();

        // remove everything in the scene
        gameLayer.getChildren().clear();

        if (backgroundImageView != null) {
            gameLayer.getChildren().add(backgroundImageView);
        }

        gasCanvas = null;

        //remove old cards
        cardBar.getChildren().clear();
        cardViews.clear();

        createPath();

        // put previewImageView bac into gameLayer
        gameLayer.getChildren().add(previewImageView);
        previewImageView.toFront();

        // start wave no.1
        currentWaveNum = 1;
        createWave(currentWaveNum);

        waveActive = true;
    }

    /**
     * Identifies a tower at the specified spatial coordinates based on proximity.
     * * This helper method utilizes Euclidean distance to determine if the cursor
     * is hovering over an existing tower entity, facilitating selection or fusion.
     *
     * @param x The horizontal coordinate to check.
     * @param y The vertical coordinate to check.
     * @return The {@link Tower} found at the position, or {@code null} if empty.
     */
    private Tower getTowerAt(double x, double y) {
        for (Tower t : towers) {
            if (Math.hypot(x - t.getX(), y - t.getY()) < 30) {
                return t;
            }
        }
        return null;
    }

    /**
     * Executes the synthesis logic to combine two towers into a more powerful unit.
     * * The process follows these architectural steps:
     * 1. Interface Check: Verifies if the base tower implements {@link Mergeable}.
     * 2. Logic Resolution: Queries the fusion result based on the ingredient.
     * 3. Entity Replacement: Removes the old base and registers the new evolved tower.
     * 4. Visual Feedback: Triggers fusion sounds and specialized animations.
     *
     * @param base       The existing tower currently on the field.
     * @param ingredient The tower instance derived from the player's selected card.
     */
    private void performFusion(Tower base, Tower ingredient) {
        Tower result = null;

        if(base instanceof Mergeable){
            result = ((Mergeable) base).getFusionResult(ingredient);
        }
        if (result != null) {
            if (mergeSound != null) mergeSound.play();
            towers.remove(base);
            gameLayer.getChildren().removeIf(node ->
                    node instanceof TowerView && ((TowerView)node).getTower() == base);
            towerViews.removeIf(v -> v.getTower() == base);

            spawnTower(result, result.getImagePath(), 60);

            for (TowerView tv : towerViews) {
                if (tv.getTower() == result) {
                    playFusionEffect(tv);
                    tv.toFront();
                    break;
                }
            }
        }
    }

    /**
     * Updates the UI state by removing the utilized card from the player's hand.
     * * Synchronizes the visual card bar and the internal card collection
     * while resetting placement previews and opacity states.
     */
    private void removeSelectedCard() {
        CardView toRemove = null;
        for (Node node : cardBar.getChildren()) {
            if (node instanceof CardView cv && cv.getCard() == selectedCard) {
                toRemove = cv;
                break;
            }
        }
        if (toRemove != null) {
            cardBar.getChildren().remove(toRemove);
            cardViews.remove(toRemove);
        }
        selectedCard = null;
        previewImageView.setOpacity(0);
        hideRangePreview();
    }

    /**
     * Triggers a multi-stage visual animation for a newly fused tower.
     * * Utilizes {@link javafx.animation.Timeline} to coordinate:
     * - Scaling: A 'burst' effect where the tower expands and settles.
     * - Bloom: A glowing effect to signify successful evolution.
     *
     * @param newTowerView The visual component of the newly created tower.
     */
    private void playFusionEffect(TowerView newTowerView) {
        javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.0);
        newTowerView.setEffect(glow);

        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.ZERO,
                        new javafx.animation.KeyValue(newTowerView.scaleXProperty(), 1.0),
                        new javafx.animation.KeyValue(newTowerView.scaleYProperty(), 1.0),
                        new javafx.animation.KeyValue(glow.levelProperty(), 0.0)
                ),
                new javafx.animation.KeyFrame(Duration.millis(200),
                        new javafx.animation.KeyValue(newTowerView.scaleXProperty(), 1.5),
                        new javafx.animation.KeyValue(newTowerView.scaleYProperty(), 1.5),
                        new javafx.animation.KeyValue(glow.levelProperty(), 1.0)
                ),
                new javafx.animation.KeyFrame(Duration.millis(500),
                        new javafx.animation.KeyValue(newTowerView.scaleXProperty(), 1.2),
                        new javafx.animation.KeyValue(newTowerView.scaleYProperty(), 1.2),
                        new javafx.animation.KeyValue(glow.levelProperty(), 0.4)
                )
        );
        timeline.play();
    }

    /**
     * Initializes the visual components of the Heads-Up Display (HUD) for health tracking.
     * * This method programmatically constructs a layered HP bar consisting of:
     * 1. A background track for the health bar.
     * 2. A dynamic foreground rectangle with a linear gradient.
     * 3. A text overlay for numerical health representation.
     * * All components are encapsulated within a {@link javafx.scene.layout.StackPane}
     * for centralized positioning.
     */
    private void setupHPUI() {
        Rectangle hpBarBackground = new Rectangle(200, 25);
        hpBarBackground.setFill(Color.web("#333333"));
        hpBarBackground.setArcWidth(15);
        hpBarBackground.setArcHeight(15);
        hpBarBackground.setStroke(Color.WHITE);
        hpBarBackground.setStrokeWidth(2);

        hpBarForeground = new Rectangle(200, 25);
        hpBarForeground.setFill(javafx.scene.paint.LinearGradient.valueOf("to right, #ff416c, #ff4b2b"));
        hpBarForeground.setArcWidth(15);
        hpBarForeground.setArcHeight(15);

        hpTextLabel = new Text("HP: 100 / 100");
        hpTextLabel.setFill(Color.WHITE);
        hpTextLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        StackPane hpContainer = new StackPane();
        hpContainer.getChildren().addAll(hpBarBackground, hpBarForeground, hpTextLabel);
        hpContainer.setLayoutX(20);
        hpContainer.setLayoutY(20);

        uiLayer.getChildren().add(hpContainer);
    }

    /**
     * Pre-loads all short-duration sound effects into memory.
     * * Utilizing {@link javafx.scene.media.AudioClip} ensures low-latency playback
     * for repetitive game events such as taking damage, selecting cards, or merging towers.
     */
    private void loadSounds() {
        try {
            hpLossSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sound/hpLossSound.mp3")).toExternalForm());
            cardSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sound/cardSound.mp3")).toExternalForm());
            mergeSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sound/mergeSound.mp3")).toExternalForm());
            winSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sound/winSound.mp3")).toExternalForm());
            gameOverSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/sound/gameOverSound.mp3")).toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load sound files: " + e.getMessage());
        }
    }

    /**
     * Configures and starts the ambient background music (BGM).
     * * Uses {@link javafx.scene.media.MediaPlayer} for long-form audio handling,
     * allowing for infinite looping and volume normalization to ensure a non-intrusive
     * auditory atmosphere.
     */
    private void playBackgroundMusic() {
        try {
            String path = Objects.requireNonNull(getClass().getResource("/sound/bgm.mp3")).toExternalForm();
            Media media = new Media(path);
            backgroundMusic = new MediaPlayer(media);

            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);

            backgroundMusic.setVolume(0.1);

            backgroundMusic.play();
        } catch (Exception e) {
            System.out.println("Error loading BGM: " + e.getMessage());
        }
    }

    /**
     * Plays a specific audio file once based on the provided resource path.
     * * This helper method wraps the {@code AudioClip} instantiation with error
     * handling to prevent engine crashes in the event of missing audio assets.
     *
     * @param path The relative resource path to the audio file.
     */
    private void playSound(String path) {
        if (path == null || path.isEmpty()) return;
        try {
            AudioClip sound = new AudioClip(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
            sound.play();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + path);
        }
    }

    /**
     * Suspends the game logic updates.
     * * When set to {@code true}, the {@code update} loop will bypass all
     * entity movements, AI calculations, and timer progressions, effectively
     * freezing the game state while maintaining the current visual frame.
     */
    public void pauseGame() {
        paused = true;
    }

    /**
     * Resumes the game logic updates from the current state.
     * * Re-enables the processing of all game systems within the {@code update} loop.
     */
    public void resumeGame() {
        paused = false;
    }

    /**
     * Returns the global list of active enemies currently in the simulation.
     * @return A {@link List} containing all live {@link Enemy} instances.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the global list of towers currently deployed on the battlefield.
     * @return A {@link List} containing all active {@link Tower} instances.
     */
    public List<Tower> getTowers() {
        return towers;
    }
}