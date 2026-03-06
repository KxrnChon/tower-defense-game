package tower;

import bullets.Bullet;
import enemy.Enemy;
import enemy.FlyingEnemy;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import path.EnemyPath;
import path.Waypoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    static {
        new JFXPanel();
    }

    private EnemyPath createPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0,0));
        points.add(new Waypoint(100,0));
        points.add(new Waypoint(200,0));
        return new EnemyPath(points);
    }

    // tower จำลองเพื่อ test
    class DummyTower extends Tower {

        public DummyTower(double x,double y){
            super(x,y,200,1,10);
        }

        @Override
        protected void shoot(Enemy target, List<Bullet> bullets) {
            bullets.add(new Bullet(x,y,target,getBulletSpeed(),getBulletDamage()));
        }
    }

    @Test
    void testUpdateShootsWhenCooldownReached() {

        DummyTower tower = new DummyTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(enemy);
        List<Bullet> bullets = new ArrayList<>();

        tower.update(2,enemies,bullets);

        assertEquals(1, bullets.size());
    }

    @Test
    void testUpdateNotShootBeforeCooldown() {

        DummyTower tower = new DummyTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(enemy);
        List<Bullet> bullets = new ArrayList<>();

        tower.update(0.5,enemies,bullets);

        assertEquals(0, bullets.size());
    }

    @Test
    void testTowerStunnedCannotShoot() {

        DummyTower tower = new DummyTower(0,0);

        Enemy enemy = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(enemy);
        List<Bullet> bullets = new ArrayList<>();

        tower.stun(5);

        tower.update(2,enemies,bullets);

        assertEquals(0, bullets.size());
    }

    @Test
    void testFindTargetChoosesFarthestEnemy() {

        DummyTower tower = new DummyTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        Enemy e2 = new Enemy(createPath(),100,10,1);

        e1.setIndex(0);
        e2.setIndex(1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(e2,result.get(0));
    }

    @Test
    void testFindTargetIgnoreFlyingEnemy() {

        DummyTower tower = new DummyTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        Enemy e2 = new FlyingEnemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertTrue(result.contains(e1));
        assertFalse(result.contains(e2));
    }

    @Test
    void testFindTargetIgnoreDeadEnemy() {

        DummyTower tower = new DummyTower(0,0);

        Enemy e1 = new Enemy(createPath(),100,10,1);
        e1.takeDamage(999);

        Enemy e2 = new Enemy(createPath(),100,10,1);

        List<Enemy> enemies = List.of(e1,e2);

        List<Enemy> result = tower.findTarget(enemies);

        assertEquals(e2,result.get(0));
    }

}