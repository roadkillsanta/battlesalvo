package cs3500.pa04.model;

import cs3500.pa04.model.json.CoordRecord;

/**
 * Represents the Coords class
 */

public class Coord {
  private final int col;
  private final int row;
  private Ship ship;
  protected boolean hasShip;
  private boolean hit = false;

  /**
   * The constructor to initialize the fields
   *
   * @param col represents the column which this Coord belongs to
   * @param row represents the row which this Coord belongs to
   */
  public Coord(int col, int row) {
    this.col = col;
    this.row = row;
  }


  /**
   * Has this tile been hit?
   *
   * @return hit status
   */
  public boolean isHit() {
    return this.hit;
  }

  /**
   * Performs a hit on the tile
   *
   * @return boolean value for if this was the first hit
   */
  public boolean doHit() {
    boolean retval = this.hasShip && !this.hit;
    this.hit = true;
    return retval;
  }

  /**
   * Gets the status of the coordinate which is an Enum
   *
   * @return the status of the Enum
   */
  public boolean occupied() {
    return this.hasShip;
  }

  public void setShip(Ship ship) {
    this.ship = ship;
    this.hasShip = true;
  }

  /**
   * We need to override equals in order to mutate correctly
   *
   * @param other the object
   * @return returns the new coordinate
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Coord c) {
      return this.col == c.getCol() && this.row == c.getRow();
    }
    return false;
  }

  /**
   * Need to override inorder for toString to work properly
   *
   * @return a string
   */
  @Override
  public String toString() {
    if (this.hit && this.hasShip) {
      return "X";
    } else if (this.hit) {
      return "0";
    } else if (this.hasShip) {
      return Character.toString(this.ship.type.name().charAt(0));
    }
    return "-";
  }

  /**
   * A record for the Coordinate
   *
   * @return A new Coordinate record with x and y
   */
  public CoordRecord toRecord() {
    return new CoordRecord(this.col, this.row);
  }

  /**
   * Getter for the Coord's x value
   *
   * @return value of X
   */
  public int getCol() {
    return this.col;
  }

  /**
   * Getter for the Coord's y value
   *
   * @return value of Y
   */
  public int getRow() {
    return this.row;
  }
}

