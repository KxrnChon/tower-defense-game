package tower;

import bullets.Bullet;
import Interface.Mergeable;
import enemy.Enemy;

import java.util.List;

/**
 * A specialized {@link Tower} designed for high-frequency electrical attacks.
 * * The {@code ZapTower} excels in rapid-fire engagement, featuring a significantly
 * lower cooldown compared to other basic towers. This makes it highly effective
 * against groups of weak enemies. As a core elemental component, it provides
 * electrical properties to fusions, leading to high-damage or chain-reaction
 * advanced towers.
 * @author Pun
 * @version 1.0
 */
public class ZapTower extends Tower implements Mergeable {

    /**
     * Constructs a ZapTower with rapid-response statistics:
     * - Range: 150 units
     * - Cooldown: 0.5 seconds (The fastest among basic towers)
     * - Damage: 7
     * - Bullet Speed: 200 units/sec
     *
     * @param x Horizontal position on the game map.
     * @param y Vertical position on the game map.
     */
    public ZapTower(double x, double y) {
        super(x, y, 150, 0.5, 7);
        this.name = "Zap Tower";
        setBulletSpeed(200);
        this.imagePath = "/image/tower/ZapTower.png";
        this.placeSoundPath = "/sound/zapTowerSound.mp3";
    }

    /**
     * Fires a standard {@link Bullet} toward the targeted enemy.
     * * While the bullet itself is standard, the high firing frequency allows
     * for consistent damage output (DPS) across the tower's range.
     *
     * @param target  The enemy identified by the targeting logic.
     * @param bullets The global list of active projectiles.
     */
    @Override
    protected void shoot(Enemy target, List<Bullet> bullets) {
        Bullet bullet = new Bullet(x, y, target, getBulletSpeed(), getBulletDamage());
        bullets.add(bullet);
    }

    /**
     * Defines the electrical fusion recipes when combined with other elemental towers.
     * * The "Zap" element typically adds raw power or multi-target capabilities:
     * 1. Zap + Range = {@link TeslaTower} (Long-range electrical arcs)
     * 2. Zap + Gas = {@link PlasmaTower} (High-energy thermal damage)
     * 3. Zap + Ice = {@link SuperconductTower} (Utility-heavy debuff combination)
     *
     * @param other The secondary tower involved in the fusion.
     * @return A new specialized {@link Tower} instance, or {@code null} if no valid recipe exists.
     */
    @Override
    public Tower getFusionResult(Tower other) {
        double tx = this.getX();
        double ty = this.getY();

        if (other instanceof RangeTower) return new TeslaTower(tx, ty);
        if (other instanceof GasTower) return new PlasmaTower(tx, ty);
        if (other instanceof IceTower) return new SuperconductTower(tx, ty);

        return null;
    }
}

