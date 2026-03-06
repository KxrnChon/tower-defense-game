package bullets;

import enemy.Enemy;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The Bullet class serves as the fundamental projectile entity in the game system.
 * It is responsible for calculating its own trajectory toward a dynamic target,
 * handling collision detection, and managing the damage application logic.
 *
 * @author Pun
 * @version 1.0
 */
public class Bullet {
    protected double x, y;
    protected Enemy target;
    protected double speed;
    protected int damage;
    protected boolean hasHit = false;
    protected double travelDistance = 0;
    protected static final int SIZE = 6;

    /**
     * Initializes a new Bullet instance with specified starting coordinates,
     * a target entity, and combat statistics.
     *
     * @param startX Initial X-coordinate of the projectile
     * @param startY Initial Y-coordinate of the projectile
     * @param target The Enemy object that this projectile will track
     * @param speed  Movement velocity of the projectile
     * @param damage Health points to be deducted upon successful impact
     */
    public Bullet(double startX, double startY, Enemy target, double speed, int damage) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    /**
     * Updates the projectile logic for each frame.
     * This method handles target validation, calculates movement vectors
     * toward the current target position, and monitors travel distance
     * for collision boundaries.
     *
     * @param deltaTime The time elapsed since the last frame update
     */
    public void update(double deltaTime) {
        if (target == null || !target.isAlive()) {
            hasHit = true;
            return;
        }

        Point2D targetPos = target.getPosition();
        double dx = targetPos.getX() - x;
        double dy = targetPos.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < 8 && travelDistance > 20) {
            target.takeDamage(damage);
            hasHit = true;
            return;
        }

        if (distance > 0) {
            double moveX = (dx / distance) * speed * 0.016;
            double moveY = (dy / distance) * speed * 0.016;
            x += moveX;
            y += moveY;
            travelDistance += speed * deltaTime;
        }
    }

    /**
     * Handles the graphical rendering of the projectile on the game canvas.
     * Visual style: A yellow circular body with a black outline.
     *
     * @param gc The GraphicsContext interface for drawing commands
     */
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - (double) SIZE / 2, y - (double) SIZE / 2, SIZE, SIZE);
        gc.setFill(Color.YELLOW);
        gc.fillOval(x - (double) SIZE / 2, y - (double) SIZE / 2, SIZE, SIZE);
    }

    /**
     * Checks if the projectile lifecycle has concluded.
     * @return true if the projectile has hit a target or is invalid, false otherwise
     */
    public boolean hasHit() { return hasHit; }

    /** @return The current horizontal coordinate of the projectile */
    public double getX() { return x; }

    /** @return The current vertical coordinate of the projectile */
    public double getY() { return y; }

    /** @return The current damage potential of this projectile */
    public int getDamage() { return damage; }

    /** @param damage Updates the damage value for this projectile instance */
    public void setDamage(int damage) { this.damage = damage; }

    /** @return The current Enemy instance being tracked by this projectile */
    public Enemy getTarget() { return target; }
}