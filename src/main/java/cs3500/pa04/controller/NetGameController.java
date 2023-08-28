package cs3500.pa04.controller;

import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameType;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.json.CoordRecord;
import cs3500.pa04.model.json.EndMessage;
import cs3500.pa04.model.json.SetupMessage;
import cs3500.pa04.model.json.ShipRecord;
import cs3500.pa04.model.json.VolleyMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller implementation for a human player vs an AI player
 */
public class NetGameController {
  protected final AiPlayer player;
  protected final GameType type;

  /**
   * Constructor
   *
   * @param player AI player
   */
  public NetGameController(AiPlayer player) {
    this.player = player;
    this.type = GameType.SINGLE;
  }

  /**
   * Constructor
   *
   * @param player AI player
   * @param type   type of game to be played
   */
  public NetGameController(AiPlayer player, GameType type) {
    this.player = player;
    this.type = type;
  }

  /**
   * Setup actions for the BattleSalvo client
   */
  public List<ShipRecord> setup(SetupMessage message) {
    ArrayList<ShipRecord> ships = new ArrayList<>();
    for (Ship ship : player.setup(message.height(), message.width(), message.spec())) {
      ships.add(ship.toRecord());
    }
    return ships;
  }

  /**
   * returns the list of coordinates that were shot
   *
   * @return a list of coords that were shot
   */
  public List<CoordRecord> takeShots() {
    List<CoordRecord> shots = new ArrayList<>();
    for (Coord coord : player.takeShots()) {
      shots.add(coord.toRecord());
    }
    return shots;
  }

  /**
   * List of coords for the places it hit
   *
   * @param message the coordinates it hit
   * @return the list of records
   */
  public List<CoordRecord> reportDamage(VolleyMessage message) {
    ArrayList<Coord> shots = new ArrayList<>();
    for (CoordRecord record : message.shots()) {
      shots.add(new Coord(record.x(), record.y()));
    }
    ArrayList<CoordRecord> records = new ArrayList<>();
    for (Coord coord : this.player.reportDamage(shots)) {
      records.add(coord.toRecord());
    }
    return records;
  }

  /**
   * The successful hits add record
   *
   * @param message The coordinates where there were successful hits
   */
  public void successfulHits(VolleyMessage message) {
    ArrayList<Coord> shots = new ArrayList<>();
    for (CoordRecord record : message.shots()) {
      shots.add(new Coord(record.x(), record.y()));
    }
    this.player.successfulHits(shots);
  }

  /**
   * Ends game message
   *
   * @param message the message that is sent
   */
  public void endGame(EndMessage message) {
    this.player.endGame(message.result(), message.reason());
  }

  /**
   * GameType we want to play
   *
   * @return the type of game
   */
  public GameType getGameType() {
    return this.type;
  }

}