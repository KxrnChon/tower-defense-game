package spell;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.Pane;
import logic.GameLogic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealingBaseTest {

    static {
        new JFXPanel(); // start JavaFX safely
    }

    @Test
    void testHealingIncreaseHP() {

        Pane pane = new Pane();

        GameLogic logic = new GameLogic(pane, () -> {});

        logic.setBaseHP(40);

        HealingBase heal = new HealingBase(logic, pane);

        heal.cast(0,0);

        assertEquals(90, logic.getBaseHP());
    }

    @Test
    void testHealingDoesNotExceedMaxHP() {

        Pane pane = new Pane();

        GameLogic logic = new GameLogic(pane, () -> {});

        logic.setBaseHP(80);

        HealingBase heal = new HealingBase(logic, pane);

        heal.cast(0,0);

        assertEquals(100, logic.getBaseHP());
    }
}