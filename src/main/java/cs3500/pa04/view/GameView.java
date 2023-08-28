package cs3500.pa04.view;

import cs3500.pa04.model.Coord;
import java.util.Scanner;

/**
 * View from the command-line-interface (console)
 */
public class GameView {
  private final Scanner input;

  /**
   * Makes sure the input of the setup is valid
   *
   * @param validate the given integer
   * @param min      the min size a board could be
   * @param max      the max size a board could be
   * @return true for valid false for invalid
   */
  private static boolean validInt(int validate, int min, int max) {
    return validate < min || validate > max;
  }

  /**
   * Represents the constructor of the view class
   *
   * @param input Store the input the user types
   */
  public GameView(Scanner input) {
    this.input = input;
  }

  /**
   * Ask for the users name
   *
   * @return The prompt and store the answer
   */
  public String promptName() {
    System.out.println("Please enter your player name: ");
    return input.nextLine();
  }

  /**
   * Asks for the users board dimension checks if its valid and stores their response
   *
   * @param minDimension the lowest dimension based on the rules
   * @param maxDimension the highest dimension based on the rules
   * @return The prompt and stores their response
   */
  public int[] promptBoardSize(int minDimension, int maxDimension) {
    if (minDimension > maxDimension) {
      throw new IllegalArgumentException(
          "Minimum dimension cannot be larger than maximum dimension!");
    }
    System.out.println("SYSTEM: Game setup");
    System.out.printf(
        "Please enter a valid height and width below (two numbers between %s and %s):\n",
        minDimension,
        maxDimension);
    int height = input.nextInt();
    int width = input.nextInt();
    while (validInt(height, minDimension, maxDimension)
        || validInt(width, minDimension, maxDimension)) {
      System.out.printf(
          "Invalid dimensions! Please enter a valid height and width below "
              + "(two numbers between %s and %s):\n",
          minDimension,
          maxDimension);
      height = input.nextInt();
      width = input.nextInt();
    }
    return new int[] {height, width};
  }

  /**
   * Asks for the number of each ship they want and stores their answer if its valid
   *
   * @param maxShipCount The max a ship could be based on the game rules
   * @return The prompt and stores user response for the amount of ships they want
   */
  public int[] promptShipCounts(int maxShipCount) {
    if (maxShipCount < 4) {
      throw new IllegalArgumentException("Max ship count must be greater than or equal to 4!");
    }
    System.out.println("Please enter a valid grouping of ships.");
    int[] values = new int[4];
    for (int i = 0; i < values.length; i++) {
      values[i] = input.nextInt();
    }
    while (true) {
      int sum = 0;
      boolean allValid = true;
      for (int value : values) {
        if (value < 1) {
          allValid = false;
          continue;
        }
        sum = sum + value;
      }
      if (sum <= maxShipCount && allValid) {
        break;
      }
      System.out.println("""
          Invalid Fleet Size!
          Please enter a valid grouping of ships.
          Remember:
          - Order: CARRIER, BATTLESHIP, DESTROYER, SUBMARINE
          - There must be at least one of each ship.
          """);
      for (int i = 0; i < values.length; i++) {
        values[i] = input.nextInt();
      }
    }
    return values;
  }

  /**
   * To prompt the user to shoot and store their response
   *
   * @param shotCount The amount of shots the user can take
   * @param height    the height of the board
   * @param width     the width of the board
   * @return The prompt to shoot and where they shot
   */
  public int[][] promptSalvo(int shotCount, int height, int width) {
    int[][] retarray = new int[shotCount][2];
    System.out.printf("Please enter %s coordinates to hit:\n", shotCount);
    for (int i = 0; i < shotCount; i++) {
      int x = input.nextInt();
      int y = input.nextInt();
      while (validInt(x, 0, width - 1) || validInt(y, 0, height - 1)) {
        System.out.printf(
            "Invalid coordinate! "
                + "Please enter an x value between 0 and %s and a y value between 0 and %s\n",
            width - 1, height - 1);
        x = input.nextInt();
        y = input.nextInt();
      }
      retarray[i][0] = x;
      retarray[i][1] = y;
    }
    return retarray;
  }

  /**
   * Displays two game boards in the console
   *
   * @param local    the local player's board
   * @param opponent the opponent's board from the local player's perspective
   */
  public void showBoard(Coord[][] local, Coord[][] opponent) {
    StringBuilder oppBoard = new StringBuilder();
    StringBuilder localBoard = new StringBuilder();
    oppBoard.append("Opponent's Board: \n");
    localBoard.append("Your Board: \n");
    for (int i = 0; i < local.length; i++) {
      for (int j = 0; j < local[0].length; j++) {
        oppBoard.append(" ").append(opponent[i][j].toString());
        localBoard.append(" ").append(local[i][j].toString());
      }
      oppBoard.append("\n");
      localBoard.append("\n");
    }
    System.out.println(oppBoard);
    System.out.println(localBoard);
  }
}
