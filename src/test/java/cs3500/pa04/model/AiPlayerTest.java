package cs3500.pa04.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Represents the tests for the AI player class
 */
class AiPlayerTest {
  AiPlayer ai;
  List<Ship> shipList;

  /**
   * Setup for testing
   */
  @BeforeEach
  void setup() {
    ai = new AiPlayer();
    Map<ShipType, Integer> ships = new HashMap<>();
    ships.put(ShipType.CARRIER, 1);
    ships.put(ShipType.BATTLESHIP, 1);
    ships.put(ShipType.DESTROYER, 1);
    ships.put(ShipType.SUBMARINE, 1);
    shipList = ai.setup(10, 10, ships);
  }

  /**
   * Tests the name method
   */
  @Test
  void testAiPlayerName() {
    assertEquals("AI", ai.name());
  }

  /**
   * Tests the setup method
   */
  @Test
  void testAiPlayerSetup() {
    assertNotNull(shipList);
    assertEquals(4, shipList.size());
  }

  /**
   * Tests the take shots method
   */
  @Test
  void testAiPlayerTakeShots() {
    List<Coord> shots = ai.takeShots();
    assertNotNull(shots);
    assertEquals(4, shots.size());
  }

  /**
   * Tests the SuccessfulHit method
   */
  @Test
  void testAiPlayerSuccessfulHits() {
    List<Coord> shots = ai.takeShots();
    ai.successfulHits(shots);

    for (Coord c : shots) {
      assertTrue(c.isHit());
    }
  }

  /**
   * Tests the end game method
   */
  @Test
  void testAiPlayerEndGame() {
    ai.endGame(GameResult.WIN, "Test Reason");
  }

}
