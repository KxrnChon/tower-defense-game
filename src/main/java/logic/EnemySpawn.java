package logic;

import enemy.Enemy;
import javafx.scene.paint.Color;
import view.VisualShape;

import java.util.function.Supplier;

/**
 * The EnemySpawn class manages the logic for instantiating a specific group of enemies.
 * * This class acts as a specialized generator that uses a factory pattern to produce
 * enemy units at designated time intervals. It maintains its own internal clock
 * and state to ensure that spawning occurs only after a specified start delay and
 * adheres to a predefined frequency (interval) and limit (count).
 *
 * @author Pun
 * @version 1.0
 */
public class EnemySpawn {

    /** The functional interface used to instantiate new enemy objects. */
    private final Supplier<Enemy> factory;
    /** Total number of enemies to be produced by this spawner. */
    private final int count;

    /** Time interval between consecutive spawn events. */
    private final double interval;

    /** Reference to the game's core logic controller for enemy registration. */
    private final GameLogic gameLogic;

    public double size;
    public Color color;
    public VisualShape shape;

    /** Delay before the first enemy of this group begins to spawn. */
    private final double startTime;

    /** Number of enemies successfully instantiated so far. */
    private int spawned = 0;

    /** Timestamp of the last successful spawn event. */
    private double lastSpawnTime = -999;

    /** Accumulator for total elapsed time within the spawner's lifecycle. */
    private double timePassed = 0;

    /**
     * Constructs an EnemySpawn controller with precise spawning parameters and visual data.
     *
     * @param gameLogic The central {@link GameLogic} instance
     * @param factory   Supplier functional interface for producing {@link Enemy} instances
     * @param startTime Initial delay in seconds before spawning commences
     * @param count     Total capacity of enemies for this spawning event
     * @param interval  Duration between individual spawns in seconds
     * @param size      The physical size representation of the spawned unit
     * @param color     The color attribute for rendering
     * @param shape     The geometric shape type for visual identification
     */
    public EnemySpawn(GameLogic gameLogic, Supplier<Enemy> factory, double startTime , int count, double interval,
                      double size, Color color, VisualShape shape) {
        this.gameLogic = gameLogic;
        this.factory = factory;
        this.startTime = startTime;
        this.count = count;
        this.interval = interval;
        this.size = size;
        this.color = color;
        this.shape = shape;
    }

    /**
     * Updates the spawning timer and executes instantiation if conditions are met.
     * * The method checks three primary conditions:
     * 1. If the global start delay has been exceeded.
     * 2. If the current spawned count is below the maximum limit.
     * 3. If the elapsed time since the last spawn exceeds the defined interval.
     *
     * @param deltaTime The time elapsed since the last game frame update
     */
    public void update(double deltaTime) {

        timePassed += deltaTime;

        if (timePassed < startTime) return;
        if (spawned >= count) return;

        if (timePassed - lastSpawnTime >= interval) {

            Enemy e = factory.get();
            gameLogic.spawnEnemy(e, this);

            spawned++;
            lastSpawnTime = timePassed;
        }
    }

    /**
     * Determines if the spawner has reached its full capacity and finished its operation.
     *
     * @return true if the number of spawned enemies equals or exceeds the target count;
     * false otherwise.
     */
    public boolean isFinished() {
        return spawned >= count;
    }
}
