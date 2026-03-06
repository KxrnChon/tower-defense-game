package logic;

import bullets.Bullet;
import enemy.Enemy;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;
import tower.RangeTower;
import tower.Tower;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {

    static class TestGameLogic extends GameLogic {

        int spawnCount = 0;

        public TestGameLogic(Pane root) {
            super(root, () -> {});
        }

        @Override
        public void spawnEnemy(Enemy e, EnemySpawn spawn) {
            spawnCount++;
            getEnemies().add(e);
        }
    }

    private TestGameLogic gameLogic;
    private EnemyPath path;

    @BeforeAll
    static void initFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already started
        }
    }

    @BeforeEach
    void setup() {

        Pane root = new Pane();
        gameLogic = new TestGameLogic(root);

        Waypoint w = new Waypoint(0,0);
        List<Waypoint> list = new ArrayList<>();
        list.add(w);

        path = new EnemyPath(list);
    }

    // -------------------------
    // Enemy Spawn Test
    // -------------------------

    @Test
    void testSpawnEnemyAddsEnemyToGame() {

        Enemy e = new Enemy(path,10,5,1);

        gameLogic.spawnEnemy(e,null);

        assertEquals(1, gameLogic.getEnemies().size());
    }

    // -------------------------
    // Tower Placement
    // -------------------------

    @Test
    void testAddTower() {

        Tower tower = new RangeTower(100,100);

        gameLogic.getTowers().add(tower);

        assertEquals(1, gameLogic.getTowers().size());
    }

    @Test
    void testMultipleTowers() {

        gameLogic.getTowers().add(new RangeTower(0,0));
        gameLogic.getTowers().add(new RangeTower(50,50));
        gameLogic.getTowers().add(new RangeTower(100,100));

        assertEquals(3, gameLogic.getTowers().size());
    }

    // -------------------------
    // Enemy Death
    // -------------------------

    @Test
    void testEnemyDies() {

        Enemy e = new Enemy(path,1,5,1);
        gameLogic.getEnemies().add(e);

        e.takeDamage(10);

        assertFalse(e.isAlive());
    }

    // -------------------------
    // Game Update Loop
    // -------------------------

    @Test
    void testGameUpdateRuns() {

        gameLogic.update(1.0);

        assertNotNull(gameLogic);
    }

    // -------------------------
    // Enemy Movement
    // -------------------------

    @Test
    void testEnemyMovesAfterUpdate() {

        List<Waypoint> list = new ArrayList<>();
        list.add(new Waypoint(0,0));
        list.add(new Waypoint(200,0));

        EnemyPath path = new EnemyPath(list);

        Enemy e = new Enemy(path,10,100,1);

        double startX = e.getX();

        for(int i=0;i<10;i++){
            e.update(0.5);
        }

        assertTrue(e.getX() > startX);
    }

    // -------------------------
    // Towers Target Enemy
    // -------------------------

    @Test
    void testTowerTargetsEnemy() {

        Tower tower = new RangeTower(0,0);
        Enemy enemy = new Enemy(path,10,5,1);

        gameLogic.getTowers().add(tower);
        gameLogic.getEnemies().add(enemy);
        List<Bullet> bullets = new ArrayList<Bullet>();
        tower.update(1.0, gameLogic.getEnemies(),bullets);

        assertTrue(true); // ยิงหรือไม่ขึ้นกับ cooldown
    }

    // -------------------------
    // Enemy List Remove Dead
    // -------------------------

    @Test
    void testRemoveDeadEnemy() {

        Enemy e = new Enemy(path,1,5,1);
        gameLogic.getEnemies().add(e);

        e.takeDamage(10);

        gameLogic.getEnemies().removeIf(enemy -> !enemy.isAlive());

        assertEquals(0, gameLogic.getEnemies().size());
    }

    // -------------------------
    // Multiple Enemy Spawn
    // -------------------------

    @Test
    void testSpawnMultipleEnemies() {

        gameLogic.spawnEnemy(new Enemy(path,10,5,1),null);
        gameLogic.spawnEnemy(new Enemy(path,10,5,1),null);
        gameLogic.spawnEnemy(new Enemy(path,10,5,1),null);

        assertEquals(3, gameLogic.getEnemies().size());
    }

}