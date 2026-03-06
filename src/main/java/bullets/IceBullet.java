package bullets;

import enemy.Enemy;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * IceBullet is a specialized subclass of the Bullet class that implements
 * a movement-speed reduction effect (slow debuff) upon impact.
 *
 * This class demonstrates the principles of inheritance and polymorphism by
 * reusing the base movement logic while extending the impact behavior to
 * apply specialized status effects to the target unit.
 *
 * @author Pun
 * @version 1.0
 */
public class IceBullet extends Bullet {

    /**
     * Constructs an IceBullet by invoking the superclass constructor.
     * Reuses the fundamental attributes defined in the Bullet class to maintain
     * structural consistency.
     *
     * @param startX Initial X-coordinate of the projectile
     * @param startY Initial Y-coordinate of the projectile
     * @param target The enemy unit to be tracked and affected by the slow debuff
     * @param speed  Movement velocity of the projectile
     * @param damage Health points to be deducted upon impact
     */
    public IceBullet(double startX, double startY, Enemy target, double speed, int damage) {
        super(startX, startY, target, speed, damage);
    }

    /**
     * Overrides the update logic to include specialized combat effects.
     * In addition to standard damage calculation, this method triggers the
     * applySlow effect on the target enemy, reducing its movement capacity.
     *
     * @param deltaTime The time interval elapsed since the last update frame
     */
    @Override
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
            target.applySlow(0.3, 5);
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
     * Overrides the visual representation of the projectile.
     * Uses a Light Blue theme to provide visual feedback to the player
     * regarding the projectile's elemental type.
     *
     * @param gc The GraphicsContext interface for drawing commands
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(x - (double) SIZE / 2, y - (double) SIZE / 2, SIZE, SIZE);
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(x - (double) SIZE / 2, y - (double) SIZE / 2, SIZE, SIZE);
    }
}