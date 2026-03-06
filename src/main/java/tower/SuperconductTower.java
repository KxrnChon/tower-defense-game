package tower;

import bullets.Bullet;
import bullets.IceBullet;
import enemy.Enemy;

import java.util.List;

/**
 * An advanced fusion tower that combines rapid-fire electrical frequency with cryogenic effects.
 * * The {@code SuperconductTower} is the elite result of merging a Zap Tower and an Ice Tower.
 * By leveraging the "Superconductivity" concept, it eliminates the high cooldown constraints
 * of standard ice-based towers. It fires {@link IceBullet} projectiles at an extremely
 * high velocity and frequency, allowing it to suppress and slow down multiple enemies
 * almost simultaneously.
 * @author Pun
 * @version 1.0
 */
public class SuperconductTower extends Tower {

    /**
     * Constructs a SuperconductTower with optimized utility statistics:
     * - Range: 180 units (Enhanced coverage)
     * - Cooldown: 0.4 seconds (The fastest slowing tower in the game)
     * - Damage: 10 per projectile
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public SuperconductTower(double x, double y) {
        super(x, y, 180, 0.4, 10);
        this.imagePath = "/image/tower/SuperconductTower.png";
        this.placeSoundPath = "/sound/zapTowerSound.mp3";
    }

    /**
     * Fires a high-frequency {@link IceBullet} at the selected target.
     * * This method combines the high fire rate inherited from the Zap element with
     * the "Slow" debuff from the Ice element. Due to the very low cooldown,
     * this tower is capable of keeping a target or a group of targets perpetually
     * slowed.
     *
     * @param target  The enemy currently identified as the priority target.
     * @param bullets The global list of active projectiles.
     */
    @Override protected void shoot(Enemy target, List<Bullet> bullets) {
        bullets.add(new IceBullet(x, y, target, getBulletSpeed(), getBulletDamage()));
    }
}
