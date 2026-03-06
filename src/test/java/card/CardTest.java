package card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    static class TestCard extends Card {
        public TestCard() {
            this.name = "TestTower";
            this.damage = 50;
            this.spa = 1.5;
            this.range = 120;
            this.special = "slow";
            this.summonFactory = "TowerObject";
        }
    }

    @Test
    void testGetters() {
        TestCard card = new TestCard();

        assertEquals("TestTower", card.getName());
        assertEquals(50, card.getDamage());
        assertEquals(1.5, card.getSpa());
        assertEquals(120, card.getRange());
        assertEquals("slow", card.getSpecial());
    }

    @Test
    void testSummon() {
        TestCard card = new TestCard();

        Object result = card.summon();

        assertEquals("TowerObject", result);
    }
}