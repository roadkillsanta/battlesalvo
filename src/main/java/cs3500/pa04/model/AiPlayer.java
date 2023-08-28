package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Represents the AbstractPlayers Class which implements the player interface
 */
public class AiPlayer extends AbstractPlayer {
  private HeatCoord[][] heatmap;
  private final ArrayList<HeatCoord> shootable = new ArrayList<>();
  private final ArrayList<HeatCoord> parity = new ArrayList<>();

  /**
   * Represents the AI's constructor
   */
  public AiPlayer() {
    super(new Random());
  }

  /**
   * Represents the AI's constructor but with a seeded Random
   *
   * @param rng used to generate a random number
   */
  public AiPlayer(Random rng) {
    super(rng);
  }

  /**
   * To represent a player
   *
   * @return The name of the player
   */
  @Override
  public String name() {
    return "AI";
  }

  /**
   * Represents the setup of the ships
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences
   *                       each ship should appear on the board
   * @return returns with a list of ships on coordinates that don't overlap
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.heatmap = new HeatCoord[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        HeatCoord coord = new HeatCoord(j, i, rng.nextFloat(1, 10));
        if ((j + i + 1) % 3 == 0) {
          coord = new HeatCoord(j, i, rng.nextFloat(1, 50));
          parity.add(coord);
        }
        this.heatmap[i][j] = coord;
        this.shootable.add(coord);
      }
    }
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        HeatCoord current = this.heatmap[i][j];
        if (i > 0) {
          current.linkUp(this.heatmap[i - 1][j]);
        }
        if (i < height - 1) {
          current.linkDown(this.heatmap[i + 1][j]);
        }
        if (j > 0) {
          current.linkLeft(this.heatmap[i][j - 1]);
        }
        if (j < width - 1) {
          current.linkRight(this.heatmap[i][j + 1]);
        }
      }
    }

    return super.setup(height, width, specifications);
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    int aliveShips = this.aliveShips();
    ArrayList<HeatCoord> shots = new ArrayList<>();
    while (shots.size() < aliveShips && shootable.size() > 0) {
      for (HeatCoord coord : shootable) {
        coord.recalculate();
      }
      shootable.sort((a, b) -> {
        if (b.getProbability() > a.getProbability()) {
          return 1;
        } else if (b.getProbability() == a.getProbability()) {
          return 0;
        } else {
          return -1;
        }
      });
      HeatCoord shot = shootable.remove(0);
      shots.add(shot);
      shot.doHit();
    }
    return new ArrayList<>(shots);
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord coord : shotsThatHitOpponentShips) {
      HeatCoord resolved = this.heatmap[coord.getRow()][coord.getCol()];
      resolved.setShip(new Ship(ShipType.UNKNOWN));
      resolved.doAdjacentHit();
    }
  }

  /**
   * @param gameResult if the player has won, lost, or forced a draw
   * @param reason     the reason for the game ending
   */
  @Override
  public void endGame(GameResult gameResult, String reason) {
    if (gameResult.equals(GameResult.WIN)) {
      System.out.println("AI: :)");
    } else {
      System.out.println("AI: :(");
    }
    System.out.println(reason);
  }
}

