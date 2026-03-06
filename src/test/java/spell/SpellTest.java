package spell;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpellTest {

    static class DummySpell extends Spell {

        public DummySpell() {
            this.name = "Dummy";
            this.damage = 10;
            this.range = 50;
            this.special = "Test";
        }

        @Override
        public void cast(double x, double y) {
            // do nothing
        }
    }

    @Test
    void testGetName() {
        DummySpell spell = new DummySpell();
        assertEquals("Dummy", spell.getName());
    }

    @Test
    void testGetDamage() {
        DummySpell spell = new DummySpell();
        assertEquals(10, spell.getDamage());
    }

    @Test
    void testGetRange() {
        DummySpell spell = new DummySpell();
        assertEquals(50, spell.getRange());
    }

    @Test
    void testGetSpecial() {
        DummySpell spell = new DummySpell();
        assertEquals("Test", spell.getSpecial());
    }

    @Test
    void testCastMethodExists() {
        DummySpell spell = new DummySpell();
        assertDoesNotThrow(() -> spell.cast(10,20));
    }
}