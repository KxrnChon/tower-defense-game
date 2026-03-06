package tower;

import bullets.Bullet;
import bullets.IceBullet;
import Interface.Mergeable;
import enemy.Enemy;

import java.util.List;

/**
 * A specialized {@link Tower} that focuses on crowd control by firing ice-based projectiles.
 * * The {@code IceTower} is designed to slow down enemy movement, providing more time for
 * other towers to deal damage. It implements the {@link Mergeable} interface,
 * acting as a fundamental element in advanced elemental fusions.
 * @author Pun
 * @version 1.0
 */
public class IceTower extends Tower implements Mergeable {

    /**
     * Constructs an IceTower with predefined crowd-control statistics:
     * - Range: 150 units (Mid-range)
     * - Cooldown: 1.0 second
     * - Damage: 5 (Low damage, compensated by utility)
     * - Bullet Speed: 200 units/sec
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public IceTower(double x, double y) {
        super(x, y, 150, 1, 5);
        this.name = "Ice Tower";
        setBulletSpeed(200);
        this.imagePath = "/image/tower/IceTower.png";
        this.placeSoundPath = "/sound/iceTowerSound.mp3";
    }

    /**
     * Fires an {@link IceBullet} toward the specified target.
     * * Unlike standard bullets, the {@code IceBullet} instantiated here typically
     * carries a "Slow" status effect that is applied to the enemy upon impact.
     *
     * @param target  The enemy identified by the targeting logic.
     * @param bullets The global list of active projectiles.
     */
    @Override
    protected void shoot(Enemy target, List<Bullet> bullets) {
        IceBullet bullet = new IceBullet(x, y, target, getBulletSpeed(), getBulletDamage());
        bullets.add(bullet);
    }

    /**
     * Defines the fusion recipes when an IceTower is combined with other elemental types.
     * * Current recipes include:
     * 1. Ice + Zap = {@link SuperconductTower} (High utility/damage)
     * 2. Ice + Range = {@link GlacierTower} (Enhanced slowing area)
     * 3. Ice + Gas = {@link MistTower} (Obscuring AoE)
     *
     * @param other The tower instance to merge with.
     * @return A new specialized {@link Tower} instance, or {@code null} if no recipe matches.
     */
    @Override
    public Tower getFusionResult(Tower other) {
        double tx = this.getX();
        double ty = this.getY();

        if (other instanceof ZapTower) return new SuperconductTower(tx, ty);
        if (other instanceof RangeTower) return new GlacierTower(tx, ty);
        if (other instanceof GasTower) return new MistTower(tx, ty);

        return null;
    }
}

