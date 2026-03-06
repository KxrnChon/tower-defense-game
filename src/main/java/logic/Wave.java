package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * The Wave class manages a collection of enemy spawn events for a specific game round.
 * * This class acts as a container and controller for multiple {@link EnemySpawn}
 * sequences, ensuring that all defined spawners are updated synchronously
 * within the game loop. It provides status information to determine when
 * a wave has successfully completed all its spawning operations.
 *
 * @author Pun
 * @version 1.0
 */
public class Wave {

    /** A list containing all individual spawning sequences assigned to this wave. */
    private final List<EnemySpawn> spawns = new ArrayList<>();

    /**
     * Adds a new spawning sequence to the current wave.
     * * @param spawn The {@link EnemySpawn} object defining the type,
     * interval, and count of enemies to be produced.
     */
    public void addSpawn(EnemySpawn spawn) {
        spawns.add(spawn);
    }

    /**
     * Updates the state of all spawning sequences based on elapsed game time.
     * This method propagates the update signal to each individual spawner
     * within the wave.
     *
     * @param elapsedTime The time passed since the last update frame.
     */
    public void update(double elapsedTime) {
        for (EnemySpawn spawn : spawns) {
            spawn.update(elapsedTime);
        }
    }

    /**
     * Evaluates whether the wave has concluded all spawning activities.
     * Uses a stream-based check to ensure every spawner in the collection
     * has completed its respective tasks.
     *
     * @return true if all associated {@link EnemySpawn} objects are finished;
     * false otherwise.
     */
    public boolean isFinished() {
        return spawns.stream().allMatch(EnemySpawn::isFinished);
    }
}
