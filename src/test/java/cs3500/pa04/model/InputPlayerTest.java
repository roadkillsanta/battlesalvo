package cs3500.pa04.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.view.GameView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputPlayerTest {

  private final PrintStream stdout = System.out;
  private InputPlayer player;
  private final Map<ShipType, Integer> spec = new HashMap<>();

  @BeforeEach
  void initData() {
    Scanner input = new Scanner("name\n");
    GameView view = new GameView(input);
    player = new InputPlayer(view, new Random(1));
    spec.put(ShipType.CARRIER, 1);
    spec.put(ShipType.BATTLESHIP, 1);
    spec.put(ShipType.DESTROYER, 1);
    spec.put(ShipType.SUBMARINE, 1);
    player.setup(6, 6, spec);
  }

  /**
   * test InputPlayer.name()
   */
  @Test
  void name() {
    Scanner input = new Scanner("name\n");
    GameView view = new GameView(input);
    player = new InputPlayer(view, new Random(1));
    assertEquals("name", player.name());
  }

  /**
   * test InputPlayer.takeShots()
   */
  @Test
  void takeShots() {
    Scanner input = new Scanner("name\n0 0 0 0 0 0 0 0 0 0 0 0\n");
    GameView view = new GameView(input);
    player = new InputPlayer(view, new Random(1));
    player.setup(6, 6, spec);

    List<Coord> correct = new ArrayList<>(
        Arrays.asList(new Coord(0, 0), new Coord(0, 0), new Coord(0, 0), new Coord(0, 0)));
    List<Coord> shots = player.takeShots();
    for (int i = 0; i < shots.size(); i++) {
      Coord coord = shots.get(i);
      Coord correctCoord = correct.get(i);
      assertEquals(correctCoord.getRow(), coord.getCol());
      assertEquals(correctCoord.getRow(), coord.getCol());
    }
  }

  /**
   * test InputPlayer.endGame()
   */
  @Test
  void endGame() {
    ByteArrayOutputStream outputReader = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputReader));

    player.endGame(GameResult.WIN, "teststring");
    player.endGame(GameResult.LOSE, "teststring");
    player.endGame(GameResult.DRAW, "teststring");

    String ln = System.lineSeparator();
    assertEquals("You win!" + ln
        + "teststring" + ln + "You lost!" + ln
        + "teststring" + ln + "Draw!" + ln + "teststring" + ln, outputReader.toString());

    System.setOut(stdout);
  }

}