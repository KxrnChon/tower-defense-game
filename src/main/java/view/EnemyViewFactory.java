package view;

import enemy.Boss;
import enemy.Enemy;
import logic.EnemySpawn;

/**
 * The EnemyViewFactory class provides a centralized mechanism for creating
 * EnemyView instances based on the specific type of enemy.
 * * It implements the Static Factory Pattern to encapsulate the logic for
 * determining visual properties—such as sprite size—ensuring that different
 * enemy tiers (e.g., standard enemies vs. Bosses) are rendered with
 * appropriate proportions.
 */
public class EnemyViewFactory {

    /**
     * Private constructor to prevent instantiation of this utility factory class.
     */
    private EnemyViewFactory() {
        // prevent instantiation
    }

    /**
     * Creates and returns a new {@link EnemyView} configured for the given enemy.
     * * This method evaluates the runtime type of the {@code enemy} object to
     * assign visual constraints:
     * - Standard Enemies: Assigned a uniform size of 40 units.
     * - Boss Entities: Assigned a larger uniform size of 60 units to emphasize
     * their status as elite threats.
     *
     * @param enemy The {@link Enemy} model that requires a visual representation.
     * @param spawn The {@link EnemySpawn} logic context (reserved for future
     * initialization parameters).
     * @return A configured {@link EnemyView} object ready to be added to the game scene.
     */
    public static EnemyView createView(Enemy enemy, EnemySpawn spawn) {
        double uniformSize = 40;
        if(enemy instanceof Boss) uniformSize = 60;

        return new EnemyView(enemy, uniformSize);
    }

}
