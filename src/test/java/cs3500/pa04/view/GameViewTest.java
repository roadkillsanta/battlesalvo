package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa04.model.Coord;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the view class
 */
class GameViewTest {

  private static final String LN = System.lineSeparator();
  GameView view;

  PrintStream stdout = System.out;
  ByteArrayOutputStream outputReader;

  @BeforeEach
  void setup() {
    outputReader = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputReader));
  }

  @AfterEach
  void reset() {
    System.setOut(stdout);
  }

  @Test
  void promptName() {
    Scanner inputs = new Scanner("1\n2\n0\n\ndjksla\n");
    view = new GameView(inputs);
    assertEquals("1", view.promptName());
    assertEquals("Please enter your player name: "
        +
        LN, outputReader.toString());
    assertEquals("2", view.promptName());
    assertEquals("0", view.promptName());
    assertEquals("", view.promptName());
    assertEquals("djksla", view.promptName());
  }

  @Test
  void promptBoardSize() {
    Scanner inputs = new Scanner("1 1\n6 6\n1 1\n0 -1\n40 0\n0 0\n10 10\n");
    view = new GameView(inputs);
    assertThrows(IllegalArgumentException.class, () -> view.promptBoardSize(4, 2),
        "Minimum dimension cannot be larger than maximum dimension!");
    assertArrayEquals(new int[] {6, 6}, view.promptBoardSize(6, 15));
    assertEquals("SYSTEM: Game setup"
        +
        LN
        +
        "Please enter a valid height and width below (two numbers between 6 and 15):"
        + LN
        +
        "Invalid dimensions! Please enter a valid height and width below "
        +
        "(two numbers between 6 and 15):"
        +
        LN, outputReader.toString());
    assertArrayEquals(new int[] {0, 0}, view.promptBoardSize(0, 0));
    assertEquals("SYSTEM: Game setup"
        + LN
        +
        "Please enter a valid height and width below (two numbers between 6 and 15):"
        + LN
        +
        "Invalid dimensions! Please enter a valid height and width below "
        +
        "(two numbers between 6 and 15):"
        +
        LN
        + "SYSTEM: Game setup"
        + LN
        +
        "Please enter a valid height and width below (two numbers between 0 and 0):"
        + LN
        +
        "Invalid dimensions! Please enter a valid height and width below "
        +
        "(two numbers between 0 and 0):"
        +
        LN
        +
        "Invalid dimensions! Please enter a valid height and width below "
        +
        "(two numbers between 0 and 0):"
        +
        LN
        +
        "Invalid dimensions! Please enter a valid height and width below "
        +
        "(two numbers between 0 and 0):"
        +
        LN, outputReader.toString());
    assertArrayEquals(new int[] {10, 10}, view.promptBoardSize(6, 15));
  }

  @Test
  void promptShipCounts() {
    Scanner inputs = new Scanner("3 1 1 1\n3 1 1 1\n1 1 1 1\n1 0 1 2\n2 1 1 1\n");
    view = new GameView(inputs);
    assertArrayEquals(new int[] {3, 1, 1, 1}, view.promptShipCounts(6));
    assertThrows(IllegalArgumentException.class, () -> view.promptShipCounts(3),
        "Max ship count must be greater than or equal to 4!");
    assertArrayEquals(new int[] {1, 1, 1, 1}, view.promptShipCounts(4));
    view.promptShipCounts(5);
    assertEquals("Please enter a valid grouping of ships."
        +
        LN
        +
        "Please enter a valid grouping of ships."
        +
        LN
        +
        "Invalid Fleet Size!"
        +
        LN
        +
        "Please enter a valid grouping of ships."
        +
        LN
        +
        "Remember:"
        +
        LN
        +
        "- Order: CARRIER, BATTLESHIP, DESTROYER, SUBMARINE"
        +
        LN
        +
        "- There must be at least one of each ship."
        +
        LN
        +
        LN
        +
        "Please enter a valid grouping of ships."
        +
        LN
        +
        "Invalid Fleet Size!"
        +
        LN
        +
        "Please enter a valid grouping of ships."
        +
        LN
        +
        "Remember:"
        +
        LN
        +
        "- Order: CARRIER, BATTLESHIP, DESTROYER, SUBMARINE"
        +
        LN
        +
        "- There must be at least one of each ship."
        +
        LN
        +
        LN, outputReader.toString());
  }

  @Test
  void showBoard() {
    Scanner inputs = new Scanner("\n");
    view = new GameView(inputs);
    Coord[][] board = new Coord[6][6];
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        board[i][j] = new Coord(i, j);
      }
    }
    view.showBoard(board, board);
    assertEquals("Opponent's Board: "
        +
        LN
        +
        " - - - - - -"
        + LN
        +
        " - - - - - -"
        + LN
        +
        " - - - - - -"
        + LN
        +
        " - - - - - -"
        + LN
        +
        " - - - - - -"
        + LN
        +
        " - - - - - -"
        + LN
        + LN
        +
        "Your Board: "
        + LN
        +
        " - - - - - -"
        +
        LN
        +
        " - - - - - -"
        +
        LN
        +
        " - - - - - -"
        +
        LN
        +
        " - - - - - -"
        +
        LN
        +
        " - - - - - -"
        +
        LN
        +
        " - - - - - -"
        +
        LN
        +
        LN, outputReader.toString());
  }

  @Test
  void promptSalvo() {
    Scanner inputs = new Scanner("1 1\n6 6\n20 20\n0 0\n-1 5\n5 -1\n5 5 0 1 0 2\n0 3\n");
    view = new GameView(inputs);
    assertArrayEquals(new int[][] {{1, 1}, {0, 0}, {5, 5}, {0, 1}, {0, 2}, {0, 3}},
        view.promptSalvo(6, 6, 6));
    assertEquals("Please enter 6 coordinates to hit:"
        +
        LN
        +
        "Invalid coordinate! Please enter an x value between 0 and 5 and a y value between 0 and 5"
        +
        LN
        +
        "Invalid coordinate! Please enter an x value between 0 and 5 and a y value between 0 and 5"
        +
        LN
        +
        "Invalid coordinate! Please enter an x value between 0 and 5 and a y value between 0 and 5"
        +
        LN
        +
        "Invalid coordinate! Please enter an x value between 0 and 5 and a y value between 0 and 5"
        +
        LN, outputReader.toString());
  }
}
