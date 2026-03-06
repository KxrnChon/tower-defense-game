package enemy;

import javafx.geometry.Point2D;
import path.EnemyPath;

/**
 * The Enemy class serves as the base entity for all hostile units in the game.
 * It manages essential unit properties including navigation along a path,
 * health management, and debuff logic.
 *
 * This class provides the foundation for movement calculations using vector
 * mathematics to ensure smooth transitions between waypoints.
 *
 * @author Pun
 * @version 1.0
 */
public class Enemy {
    private final EnemyPath enemyPath;
    private int index;

    private int hp;
    private int damage;
    private boolean alive = true;
    private double baseSpeed;
    private double currentSpeed;
    private final int maxHp;
    protected String imagePath;

    private double slowTimer = 0;
    private double slowPercent = 0;

    private Point2D position;

    /**
     * Constructs a new Enemy with a specific path and base statistics.
     * The enemy is automatically positioned at the start of the provided path.
     *
     * @param enemyPath The predefined path waypoints for navigation
     * @param hp        Initial health points
     * @param damage    Damage dealt to the player upon reaching the end
     * @param speed     Base movement velocity
     */
    public Enemy(EnemyPath enemyPath, int hp, int damage, double speed) {
        this.enemyPath = enemyPath;
        setHp(hp);
        setDamage(damage);
        setBaseSpeed(speed);
        setCurrentSpeed(speed);
        this.maxHp = hp;
        index = 0;
        position = enemyPath.getWaypoint(0).getPosition();
    }

    /** @return The resource path for the enemy's visual sprite. */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Updates the enemy's state for each frame.
     * This includes handling the slowing effect duration, calculating the
     * movement vector toward the next waypoint, and updating the position.
     *
     * @param deltaTime Time elapsed since the last update frame
     */
    public void update(double deltaTime) {
        if (!alive) return;

        if (isFinished()) {
            onReachEnd();
            return;
        }

        if (slowTimer > 0) {
            slowTimer -= deltaTime;
            currentSpeed = baseSpeed * (1 - slowPercent);
        } else {
            currentSpeed = baseSpeed;
            slowPercent = 0;
        }

        Point2D target = enemyPath.getWaypoint(index).getPosition();
        Point2D direction = target.subtract(position);

        if (direction.magnitude() < 2) {
            index++;
            return;
        }

        Point2D velocity = direction.normalize()
                .multiply(currentSpeed * deltaTime);

        position = position.add(velocity);
    }

    /**
     * Reduces health points and triggers death logic if health reaches zero.
     *
     * @param damage Amount of damage to subtract from current health
     */
    public void takeDamage(int damage){
        if(!alive) return;

        setHp(hp - damage);
        if(hp <= 0) die();
    }

    /** @return true if the enemy is still active in the game. */
    public boolean isAlive() {
        return alive;
    }

    /** Sets the enemy state to inactive and stops all updates. */
    public void die() {
        alive = false;
    }

    /** @return true if the enemy has navigated through all path waypoints. */
    public boolean isFinished() {
        return index >= enemyPath.length();
    }

    /** Logic executed when the enemy successfully completes its path. */
    public void onReachEnd() {die();}

    /** @return true if the enemy has reached the end of the path, false otherwise.*/
    public boolean hasReachedEnd() {return isFinished();}

    /** @return Current waypoint index in the navigation sequence. */
    public int getIndex() {
        return index;
    }

    /** @return Current health points. */
    public int getHp() {
        return hp;
    }

    /** @param hp Sets health points, with a minimum of zero. */
    public void setHp(int hp) {
        this.hp = Math.max(hp,0);
    }

    /** @return The damage value of this enemy unit. */
    public int getDamage() {
        return damage;
    }

    /** @param damage Sets the damage value and ensure the damage does not fall below zero. */
    public void setDamage(int damage) {
        this.damage = Math.max(damage,0);
    }

    /** @return Current 2D position coordinates. */
    public Point2D getPosition() {
        return position;
    }

    /** @return The {@link EnemyPath} object containing the waypoint data. */
    public EnemyPath getEnemyPath() {
        return enemyPath;
    }

    /**
     * Applies a movement slowing effect for a specific duration.
     * If multiple slows are applied, the strongest effect takes priority.
     *
     * @param percent  Speed reduction percentage (0.0 to 1.0)
     * @param duration Duration of the effect in seconds
     */
    public void applySlow(double percent, double duration) {
        if (percent > slowPercent) {
            slowPercent = percent;
        }
        slowTimer = Math.max(slowTimer, duration);
    }

    /** @param baseSpeed the base movement speed of this enemy. */
    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = Math.max(0,baseSpeed);
    }

    /** @return The base movement speed of this enemy. */
    public double getBaseSpeed() {
        return baseSpeed;
    }

    /** @param currentSpeed Sets the actual movement speed used for current frame calculations. */
    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    /** @return true if the enemy is slowed, false otherwise. */
    public boolean isSlowed() {
        return slowTimer > 0;
    }

    /** @return The maximum health points (HP). */
    public int getMaxHp() {
        return maxHp;
    }

    /** @return true if the enemy is armored, false by default. */
    public boolean hasArmor() { return false; }

    /** @return The integer value of this enemy's armor. */
    public int getArmor() { return 0; }

    /** @return The current X-coordinate. */
    public double getX() {
        return position.getX();
    }

    /** @return The current Y-coordinate. */
    public double getY() {
        return position.getY();
    }

    /** @param point2D sets the enemy's position on the game map. */
    public void setPosition(Point2D point2D) {
        this.position = point2D;
    }

    /** @param i Sets the waypoint index of this enemy. */
    public void setIndex(int i) {
        index = i;
    }
}