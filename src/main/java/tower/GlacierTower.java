package tower;

import bullets.Bullet;
import bullets.IceBullet;
import enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * An advanced fusion tower that combines superior range with cryogenic status effects.
 * * The {@code GlacierTower} represents the pinnacle of long-range crowd control.
 * By merging the properties of a Range Tower and an Ice Tower, it achieves a
 * massive engagement radius, firing heavy {@link IceBullet} projectiles that
 * damage and slow down enemies from a distance, effectively creating a
 * "kill zone" across a large portion of the map.
 * @author Pun
 * @version 1.0
 */
public class GlacierTower extends Tower {

    /**
     * Constructs a GlacierTower with elite-tier statistics:
     * - Range: 400 units (Extreme range)
     * - Cooldown: 1.8 seconds (Slow fire rate, balanced by high impact)
     * - Damage: 25 (High single-target damage)
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public GlacierTower(double x, double y) {
        super(x, y, 400, 1.8, 25);
        this.imagePath = "/image/tower/GlacierTower.png";
        this.placeSoundPath = "/sound/iceTowerSound.mp3";
    }

    /**
     * Identifies the optimal target within its vast engagement radius.
     * * Similar to the Range Tower's logic, this method removes type restrictions,
     * allowing the tower to target any living enemy (including flying units).
     * It prioritizes the "First" enemy—the one furthest along the navigational path.
     *
     * @param enemies List of all active enemies in the game.
     * @return A list containing the single most advanced enemy within range.
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
     * Fires a high-velocity {@link IceBullet} at the selected target.
     * * Each shot deals significant damage and applies a powerful "Slow" debuff,
     * leveraging the combined elemental strengths of its component towers.
     *
     * @param target  The enemy identified as the priority target.
     * @param bullets The global list of active projectiles.
     */
    @Override protected void shoot(Enemy target, List<Bullet> bullets) {
        bullets.add(new IceBullet(x, y, target, getBulletSpeed(), getBulletDamage()));
    }
}
