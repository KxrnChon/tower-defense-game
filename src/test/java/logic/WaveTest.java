package logic;

import enemy.Enemy;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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

public class WaveTest {

        static {
            new JFXPanel(); // start JavaFX safely
        }

        static class TestGameLogic extends GameLogic {

            int spawnCount = 0;

            public TestGameLogic() {
                super(new Pane(), () -> {});
            }

            @Override
            public void spawnEnemy(Enemy e, EnemySpawn spawn) {
                spawnCount++;
            }
        }

    TestGameLogic gameLogic;
    EnemySpawn spawn1;
    EnemySpawn spawn2;
    Wave wave;

    @BeforeEach
    void setup() {

        gameLogic = new TestGameLogic();

        Waypoint w = new Waypoint(0,0);
        List<Waypoint> list = new ArrayList<>();
        list.add(w);

        EnemyPath path = new EnemyPath(list);

        Supplier<Enemy> factory = () -> new Enemy(path,10,5,1);

        spawn1 = new EnemySpawn(gameLogic,factory,0,2,1,10,null,null);
        spawn2 = new EnemySpawn(gameLogic,factory,0,2,1,10,null,null);

        wave = new Wave();
        wave.addSpawn(spawn1);
        wave.addSpawn(spawn2);
    }

    @Test
    void testWaveSpawnsEnemies() {

        wave.update(1.1);

        assertEquals(2, gameLogic.spawnCount);
    }

    @Test
    void testWaveSpawnMultipleTimes() {

        wave.update(1.1);
        wave.update(1.1);

        assertEquals(4, gameLogic.spawnCount);
    }

    @Test
    void testWaveNotFinishedInitially() {

        assertFalse(wave.isFinished());
    }

    @Test
    void testWaveFinishedAfterAllSpawnsDone() {

        wave.update(1.1);
        wave.update(1.1);

        assertTrue(wave.isFinished());
    }

    @Test
    void testWaveUpdateDoesNotSpawnBeforeStartTime() {

        Waypoint w = new Waypoint(0,0);
        List<Waypoint> list = new ArrayList<>();
        list.add(w);

        EnemyPath path = new EnemyPath(list);

        Supplier<Enemy> factory = () -> new Enemy(path,10,5,1);

        EnemySpawn delayedSpawn =
                new EnemySpawn(gameLogic,factory,5,2,1,10,null,null);

        Wave delayedWave = new Wave();
        delayedWave.addSpawn(delayedSpawn);

        delayedWave.update(1);

        assertEquals(0, gameLogic.spawnCount);
    }
}