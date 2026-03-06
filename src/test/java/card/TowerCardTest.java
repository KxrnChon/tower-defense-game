package card;

import org.junit.jupiter.api.Test;
import tower.RangeTower;
import tower.Tower;

import static org.junit.jupiter.api.Assertions.*;

public class TowerCardTest {

    @Test
    void testConstructor() {
        TowerCard card = new TowerCard(() -> new RangeTower(0,0));

        RangeTower tower = new RangeTower(0,0);

        assertEquals(tower.getName(), card.getName());
        assertEquals(tower.getBulletDamage(), card.getDamage());
        assertEquals(tower.getCooldown(), card.getSpa());
        assertEquals(tower.getRange(), card.getRange());
    }

    @Test
    void testSummonCreatesTower() {
        TowerCard card = new TowerCard(() -> new RangeTower(0,0));

        Tower tower = card.summon();

        assertNotNull(tower);
        assertTrue(tower instanceof RangeTower);
    }

    @Test
    void testSummonCreatesNewInstance() {
        TowerCard card = new TowerCard(() -> new RangeTower(0,0));

        Tower t1 = card.summon();
        Tower t2 = card.summon();

        assertNotSame(t1, t2);
    }
}