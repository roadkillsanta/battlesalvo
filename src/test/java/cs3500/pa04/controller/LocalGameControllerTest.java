package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.view.GameView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Test for LocalGameController class
 */
class LocalGameControllerTest {

  private ByteArrayOutputStream outputReader;
  private LocalGameController controller;

  @BeforeEach
  void init() {
    Scanner input = new Scanner("6 6\n1 1 1 1\n\n");
    GameView view = new GameView(input);
    AiPlayer p2 = new AiPlayer(new Random(1));
    AiPlayer p3 = new AiPlayer(new Random(1));
    controller = new LocalGameController(view, p2, p3);
    outputReader = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputReader));
  }

  @Test
  public void testRun() {
    controller.run();
    //the game should run to completion
    assertTrue(outputReader.toString().contains("AI"));
    assertNotNull(outputReader.toString());
  }
}