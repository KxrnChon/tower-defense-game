package tower;

import bullets.Bullet;
import enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * An elite fusion tower that combines exceptional engagement range with extreme firing velocity.
 * * The {@code TeslaTower} is the advanced result of merging a Zap Tower and a Range Tower.
 * Designed for high-precision and high-frequency strikes, it possesses a massive detection
 * radius and the lowest attack cooldown in the game. It functions as a "Long-range Striker,"
 * capable of eliminating targets across a significant portion of the map with lightning-fast
 * projectiles.
 * @author Pun
 * @version 1.0
 */
public class TeslaTower extends Tower {

    /**
     * Constructs a TeslaTower with peak performance statistics:
     * - Range: 250 units (Long-range coverage)
     * - Cooldown: 0.3 seconds (The fastest attack speed in the game)
     * - Damage: 15 (High single-target DPS)
     * - Bullet Speed: 300 units/sec (Ultra-fast projectiles to match range)
     *
     * @param x Horizontal position on the map.
     * @param y Vertical position on the map.
     */
    public TeslaTower(double x, double y) {
        super(x, y, 250, 0.3, 15);
        setBulletSpeed(300);
        this.imagePath = "/image/tower/TeslaTower.png";
        this.placeSoundPath = "/sound/zapTowerSound.mp3";
    }

    /**
     * Identifies the optimal target using advanced long-range logic.
     * * Similar to the Range Tower, this implementation removes all type restrictions,
     * allowing it to strike any living enemy (including high-speed or flying units).
     * It prioritizes the "First" enemy—the one furthest along the path—to ensure
     * maximum defensive efficiency.
     *
     * @param enemies List of all active enemies in the battlefield.
     * @return A list containing the single most progressed target within range.
     */
    protected List<Enemy> findTarget(List<Enemy> enemies) {

        Enemy bestTarget = null;
        int maxIndex = -1;
        double closestToNextPoint = Double.MAX_VALUE;

        for (Enemy e : enemies) {

            if (!e.isAlive()) continue;
            if (notInRange(e)) continue;

            int idx = e.getIndex();

            if (idx > maxIndex) {
                maxIndex = idx;
                bestTarget = e;

                if (!e.isFinished()) {
                    var nextPoint = e.getEnemyPath()
                            .getWaypoint(idx)
                            .getPosition();

                    closestToNextPoint =
                            e.getPosition().distance(nextPoint);
                }
            }

            else if (idx == maxIndex && !e.isFinished()) {

                var nextPoint = e.getEnemyPath()
                        .getWaypoint(idx)
                        .getPosition();

                double dist =
                        e.getPosition().distance(nextPoint);

                if (dist < closestToNextPoint) {
                    closestToNextPoint = dist;
                    bestTarget = e;
                }
            }
        }

        List<Enemy> result = new ArrayList<>();
        if (bestTarget != null) {
            result.add(bestTarget);
        }

        return result;
    }

    /**
     * Dispatches a high-velocity {@link Bullet} toward the targeted enemy.
     * * Due to the 0.3s cooldown and 300 bullet speed, the tower provides almost
     * continuous projectile flow, effectively shredding enemy health bars before
     * they can exit the engagement zone.
     *
     * @param target  The enemy identified as the priority target.
     * @param bullets The global projectile list.
     */
    @Override protected void shoot(Enemy target, List<Bullet> bullets) {
        bullets.add(new Bullet(x, y, target, getBulletSpeed(), getBulletDamage()));
    }
}
