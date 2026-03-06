package logic;

import enemy.Enemy;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class EnemySpawnTest {

    @BeforeAll
    static void initFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    static class TestGameLogic extends GameLogic {
        int spawnCount = 0;

        public TestGameLogic(Pane root, Runnable onExitToMenu) {
            super(root, onExitToMenu);
        }

        @Override
        public void spawnEnemy(Enemy e, EnemySpawn spawn) {
            spawnCount++;
        }
    }

    TestGameLogic gameLogic;
    EnemySpawn enemySpawn;

    @BeforeEach
    void setup() {

        Pane root = new Pane();
        gameLogic = new TestGameLogic(root, () -> {});

        Waypoint w = new Waypoint(0,0);
        List<Waypoint> list = new ArrayList<>();
        list.add(w);
        EnemyPath path = new EnemyPath(list);

        Supplier<Enemy> factory = () -> new Enemy(path,10,5,1);

        enemySpawn = new EnemySpawn(
                gameLogic,
                factory,
                0,
                3,
                1,
                10,
                null,
                null
        );
    }

    @Test
    void testSpawnEnemy() {

        enemySpawn.update(1.1);

        assertEquals(1, gameLogic.spawnCount);
    }

    @Test
    void testSpawnMultipleEnemies() {

        enemySpawn.update(1.1);
        enemySpawn.update(1.1);
        enemySpawn.update(1.1);

        assertEquals(3, gameLogic.spawnCount);
    }

    @Test
    void testSpawnStopsAtCount() {

        enemySpawn.update(1.1);
        enemySpawn.update(1.1);
        enemySpawn.update(1.1);
        enemySpawn.update(1.1);

        assertEquals(3, gameLogic.spawnCount);
        assertTrue(enemySpawn.isFinished());
    }

    @Test
    void testNotSpawnBeforeStartTime() {

        Waypoint w = new Waypoint(0,0);
        List<Waypoint> list = new ArrayList<>();
        list.add(w);
        EnemyPath path = new EnemyPath(list);

        Supplier<Enemy> factory = () -> new Enemy(path,10,5,1);

        EnemySpawn spawn = new EnemySpawn(
                gameLogic,
                factory,
                5,      // startTime
                3,
                1,
                10,
                null,
                null
        );

        spawn.update(1);

        assertEquals(0, gameLogic.spawnCount);
    }
}