package tower;

import Interface.Mergeable;

/**
 * Represents a specific implementation of an {@link AOETower} that utilizes toxic gas to damage enemies.
 * * The {@code GasTower} focuses on short-range, area-of-effect damage. Additionally,
 * it implements the {@link Mergeable} interface, allowing it to act as a base
 * component for advanced tower fusions when combined with other elemental towers.
 * @author Pun
 * @version 1.0
 */
public class GasTower extends AOETower implements Mergeable {

    /**
     * Constructs a GasTower with predefined stats:
     * - Range: 75 units
     * - Cooldown: 1.5 seconds
     * - Damage: 10 per tick
     *
     * @param x Horizontal position on the map.
     * @param y Vertical position on the map.
     */
    public GasTower(double x, double y) {
        super(x, y, 75, 1.5, 10);
        this.name = "Gas Tower";
        this.imagePath = "/image/tower/GasTower.png";
        this.placeSoundPath = "/sound/gasTowerSound.mp3";
    }

    /**
     * Determines the resulting advanced tower when this GasTower is merged with another tower.
     * * The fusion logic follows these elemental combinations:
     * 1. Gas + Zap = {@link PlasmaTower}
     * 2. Gas + Range = {@link TornadoTower}
     * 3. Gas + Ice = {@link MistTower}
     *
     * @param other The tower being merged with this one.
     * @return A new instance of the resulting {@link Tower}, or {@code null} if no valid fusion exists.
     */
    @Override
    public Tower getFusionResult(Tower other) {
        double tx = this.getX();
        double ty = this.getY();

        if (other instanceof ZapTower) return new PlasmaTower(tx, ty);
        if (other instanceof RangeTower) return new TornadoTower(tx, ty);
        if (other instanceof IceTower) return new MistTower(tx, ty);

        return null;
    }
}