package tower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GasTowerTest {

    @Test
    void fusionWithZapTowerReturnsPlasmaTower() {

        GasTower gasTower = new GasTower(100, 200);
        ZapTower zapTower = new ZapTower(0,0);

        Tower result = gasTower.getFusionResult(zapTower);

        assertNotNull(result);
        assertTrue(result instanceof PlasmaTower);
        assertEquals(100, result.getX());
        assertEquals(200, result.getY());
    }

    @Test
    void fusionWithRangeTowerReturnsTornadoTower() {

        GasTower gasTower = new GasTower(100, 200);
        RangeTower rangeTower = new RangeTower(0,0);

        Tower result = gasTower.getFusionResult(rangeTower);

        assertNotNull(result);
        assertTrue(result instanceof TornadoTower);
        assertEquals(100, result.getX());
        assertEquals(200, result.getY());
    }

    @Test
    void fusionWithIceTowerReturnsMistTower() {

        GasTower gasTower = new GasTower(100, 200);
        IceTower iceTower = new IceTower(0,0);

        Tower result = gasTower.getFusionResult(iceTower);

        assertNotNull(result);
        assertTrue(result instanceof MistTower);
        assertEquals(100, result.getX());
        assertEquals(200, result.getY());
    }

    @Test
    void fusionWithOtherTowerReturnsNull() {

        GasTower gasTower = new GasTower(100, 200);
        GasTower otherGas = new GasTower(0,0);

        Tower result = gasTower.getFusionResult(otherGas);

        assertNull(result);
    }
}