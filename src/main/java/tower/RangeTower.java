package tower;

import bullets.Bullet;
import Interface.Mergeable;
import enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * A specialized {@link Tower} characterized by its superior engagement distance and versatile targeting.
 * * The {@code RangeTower} boasts the highest base range among basic towers, allowing it
 * to cover large portions of the map. Unlike standard ground towers, its refined
 * {@link #findTarget} logic allows it to engage any living enemy within its radius.
 * It also serves as a critical component in the fusion system to create high-range
 * advanced structures.
 * @author Pun
 * @version 1.0
 */
public class RangeTower extends Tower implements Mergeable {

    /**
     * Constructs a RangeTower with high-distance combat statistics:
     * - Range: 250 units (Long-range)
     * - Cooldown: 1.0 second
     * - Damage: 10
     * - Bullet Speed: 250 units/sec (Fast projectiles)
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public RangeTower(double x,double y){
        super(x,y,250,1, 10);
        this.name = "Range Tower";
        setBulletSpeed(250);
        this.imagePath = "/image/tower/RangeTower.png";
        this.placeSoundPath = "/sound/rangeTowerSound.mp3";
    }

    /**
     * Fires a standard {@link Bullet} toward the identified target.
     * * The bullet speed is enhanced compared to basic towers to ensure it reaches
     * distant targets effectively.
     *
     * @param target  The optimal enemy identified by the targeting logic.
     * @param bullets The global list of active projectiles.
     */
    @Override
    protected void shoot(Enemy target, List<Bullet> bullets) {
        bullets.add(new Bullet(x, y, target, getBulletSpeed(), getBulletDamage()));
    }

    /**
     * Overrides the targeting logic to remove type restrictions.
     * * Unlike the base Tower class, {@code RangeTower} can target all enemies (including
     * flying ones) that are alive and within range. It maintains the "First" targeting
     * priority, focusing on the enemy furthest along the path.
     *
     * @param enemies List of all potential targets in the game.
     * @return A list containing the single most progressed enemy within range.
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
     * Defines the fusion outcomes when a RangeTower is combined with elemental components.
     * * Key recipes include:
     * 1. Range + Zap = {@link TeslaTower} (Long-range chain lightning)
     * 2. Range + Gas = {@link TornadoTower} (High-velocity AoE)
     * 3. Range + Ice = {@link GlacierTower} (Extended slow-zone control)
     *
     * @param other The secondary tower involved in the fusion.
     * @return A new advanced {@link Tower} instance, or {@code null} if the combination is invalid.
     */
    @Override
    public Tower getFusionResult(Tower other) {
        double tx = this.getX();
        double ty = this.getY();

        if (other instanceof ZapTower) return new TeslaTower(tx, ty);
        if (other instanceof GasTower) return new TornadoTower(tx, ty);
        if (other instanceof IceTower) return new GlacierTower(tx, ty);

        return null;
    }
}
