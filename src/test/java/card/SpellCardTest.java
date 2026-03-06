package card;

import enemy.Enemy;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import spell.FireBall;
import spell.Spell;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpellCardTest {

    @Test
    void testConstructor() {
        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        SpellCard card = new SpellCard(() -> new FireBall(enemies, pane));

        FireBall fireBall = new FireBall(enemies, pane);

        assertEquals(fireBall.getName(), card.getName());
        assertEquals(fireBall.getDamage(), card.getDamage());
        assertEquals(fireBall.getRange(), card.getRange());
        assertEquals(fireBall.getSpecial(), card.getSpecial());
    }

    @Test
    void testSummonCreatesSpell() {
        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        SpellCard card = new SpellCard(() -> new FireBall(enemies, pane));

        Spell spell = card.summon();

        assertNotNull(spell);
        assertTrue(spell instanceof FireBall);
    }

    @Test
    void testSummonCreatesNewInstance() {
        List<Enemy> enemies = new ArrayList<>();
        Pane pane = new Pane();

        SpellCard card = new SpellCard(() -> new FireBall(enemies, pane));

        Spell s1 = card.summon();
        Spell s2 = card.summon();

        assertNotSame(s1, s2);
    }
}