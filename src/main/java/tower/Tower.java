package tower;

import bullets.Bullet;
import enemy.Enemy;
import enemy.FlyingEnemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the skeletal implementation of a defensive structure within the game.
 * * The {@code Tower} class defines the core attributes and behaviors for all tower types,
 * including target acquisition, cooldown management, and status effect handling (e.g., stunning).
 * It employs a template-like structure where the update logic is standardized,
 * but the specific shooting mechanism is deferred to subclasses.
 */
public abstract class Tower {

    /** The display name of the tower. */
    protected String name;

    /** The spatial coordinates of the tower on the game map. */
    protected double x, y;

    /** The circular radius within which the tower can detect and engage enemies. */
    protected double range;

    /** The time interval (in seconds) required between consecutive shots. */
    protected double cooldown;

    /** Internal counter to track elapsed time for the cooldown cycle. */
    protected double timer = 0;

    /** Resource path for the tower's visual sprite. */
    protected String imagePath = "";

    /** Resource path for the sound effect played when the tower is placed. */
    protected String placeSoundPath = "";

    /** The velocity of projectiles fired by this tower. */
    protected double bulletSpeed = 200;

    /** The amount of health reduction applied by this tower's projectiles. */
    protected final int bulletDamage;

    /** Internal counter for the duration of a stun effect. */
    protected double stunTimer = 0;

    /** State flag indicating if the tower is currently disabled. */
    protected boolean stunned = false;

    /**
     * Constructs a Tower with fundamental combat statistics.
     *
     * @param x        The horizontal position.
     * @param y        The vertical position.
     * @param range    The detection radius.
     * @param cooldown The shooting frequency.
     * @param damage   The attack power.
     */
    public Tower(double x, double y, double range, double cooldown, int damage) {
        this.x = x;
        this.y = y;
        this.range = range;
        this.cooldown = cooldown;
        this.bulletDamage = damage;
    }

    /**
     * Updates the tower's state during each game tick.
     * * This method handles:
     * 1. Stun Recovery: Decrements the stun timer and restores functionality when expired.
     * 2. Cooldown Management: Accumulates {@code deltaTime} to determine if the tower can fire.
     * 3. Target Acquisition: Invokes {@link #findTarget} to locate valid enemies.
     * 4. Attack Execution: Triggers the {@link #shoot} method for identified targets.
     *
     * @param deltaTime The time elapsed since the last update.
     * @param enemies   The list of currently active enemies.
     * @param bullets   The global list of projectiles to append new shots to.
     */
    public void update(double deltaTime, List<Enemy> enemies, List<Bullet> bullets) {
        if (stunned) {
            stunTimer -= deltaTime;

            if (stunTimer <= 0) {
                stunned = false;
            }

            return;
        }

        timer += deltaTime;

        if (timer < cooldown) return;

        List<Enemy> targets = findTarget(enemies);
        if (targets.isEmpty()) return;

        for (Enemy target : targets) {
            shoot(target, bullets);
        }

        timer = 0;
    }

    /**
     * Logic for selecting the most appropriate enemy to attack.
     * * The current implementation prioritizes the enemy that is:
     * 1. Alive and not a {@link FlyingEnemy}.
     * 2. Within the tower's {@code range}.
     * 3. Furthest along the path (highest Waypoint index).
     * 4. Closest to their next objective in case of a tie in index.
     *
     * @param enemies The list of potential targets.
     * @return A list containing the optimal target(s).
     */
    protected List<Enemy> findTarget(List<Enemy> enemies) {

        Enemy bestTarget = null;
        int maxIndex = -1;
        double closestToNextPoint = Double.MAX_VALUE;

        for (Enemy e : enemies) {

            if (!e.isAlive()) continue;
            if (e instanceof FlyingEnemy) continue;
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
     * Calculates the Euclidean distance to determine if an enemy is outside the attack radius.
     *
     * @param e The enemy to check.
     * @return {@code true} if the enemy is beyond the engagement range.
     */
    protected boolean notInRange(Enemy e) {
        double dx = e.getPosition().getX() - x;
        double dy = e.getPosition().getY() - y;
        return !(dx * dx + dy * dy <= range * range);
    }

    /**
     * Disables the tower for a specified duration.
     * @param duration Time in seconds the tower remains inactive.
     */
    public void stun(double duration) {
        stunned = true;
        stunTimer = duration;
    }

    /**
     * Abstract method to be implemented by subclasses to define projectile behavior.
     *
     * @param target  The enemy to engage.
     * @param bullets The list to add new projectiles to.
     */
    protected abstract void shoot(Enemy target, List<Bullet> bullets);

    /** @return The x-coordinate of the tower. */
    public double getX() {
        return x;
    }

    /** @return The y-coordinate of the tower. */
    public double getY() {
        return y;
    }

    /** @return The attack range in pixels of this tower. */
    public double getRange() {
        return range;
    }

    /** @return The bullet damage of this tower */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /** @return The speed of the bullet of this tower. */
    public double getBulletSpeed() {
        return bulletSpeed;
    }

    /** @param bulletSpeed Sets the new speed value of this tower's projectiles. */
    public void setBulletSpeed(double bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    /** @return The cooldown duration between attacks. */
    public double getCooldown() {
        return cooldown;
    }

    /**
     * Updates the tower's location on the game map.
     * @param x The new horizontal coordinate.
     * @param y The new vertical coordinate.
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** @return The name of the tower. */
    public String getName() {
        return this.name;
    }

    /** @return The resource path string of the tower's sprite image. */
    public String getImagePath() {
        return imagePath;
    }

    /** @param path Sets a new image path for the tower's visual representation. */
    public void setImagePath(String path) {
        this.imagePath = path;
    }

    /** @return The file path to the sound effect played upon placement. */
    public String getPlaceSoundPath() { return placeSoundPath; }

    /** @return {@link javafx.geometry.Point2D} Returns the tower's location as a 2D point object. */
    public javafx.geometry.Point2D getPosition() {
        return new javafx.geometry.Point2D(x, y);
    }
}

