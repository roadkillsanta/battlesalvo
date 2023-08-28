package cs3500.pa04.model;

/**
 * Represents a HeatCord for our Ai to peform better
 */
public class HeatCoord extends Coord {
  private HeatCoord up;
  private HeatCoord down;
  private HeatCoord left;
  private HeatCoord right;
  private float probability;

  /**
   * The constructor to initialize the fields
   *
   * @param row represents the row
   * @param col represents the column
   */
  public HeatCoord(int col, int row, float probability) {
    super(col, row);
    this.probability = probability;
  }

  /**
   * To shot adjacent coordinates by increasing probability
   */
  private void adjacentHit(int prob) {
    if (!this.isHit()) {
      this.probability += prob;
      this.recalculate();
      //parityCoords.add(this);
    }
  }

  /**
   * To link the coordinate with the top one
   *
   * @param coord the coordinated to link to
   */
  public void linkUp(HeatCoord coord) {
    this.up = coord;
    //this.adjacent.add(coord);
  }

  /**
   * To link the coordinate with the down one
   *
   * @param coord the coordinate to link to
   */
  public void linkDown(HeatCoord coord) {
    this.down = coord;
    //this.adjacent.add(coord);
  }

  /**
   * To link the coordinate with the left one
   *
   * @param coord the coordinate to link to
   */
  public void linkLeft(HeatCoord coord) {
    this.left = coord;
    //this.adjacent.add(coord);
  }

  /**
   * To link the coordinate with the right one
   *
   * @param coord the coordinate to link to
   */
  public void linkRight(HeatCoord coord) {
    this.right = coord;
    //this.adjacent.add(coord);
  }

  /**
   * Get the tile's distance from the nearest unhit coords on all sides
   *
   * @return ship margins for tile
   */
  private int[] shipMargins() {
    int marginUp = 0;
    int marginDown = 0;
    int marginLeft = 0;
    int marginRight = 0;
    HeatCoord nextUp = this.up;
    for (int i = 0; i < 6; i++) {
      if (nextUp == null || nextUp.isHit() && !nextUp.occupied()) {
        break;
      }
      marginUp++;
      nextUp = nextUp.up;
    }
    HeatCoord nextDown = this.down;
    for (int i = 0; i < 6; i++) {
      if (nextDown == null || nextDown.isHit() && !nextDown.occupied()) {
        break;
      }
      marginDown++;
      nextDown = nextDown.down;
    }
    HeatCoord nextLeft = this.left;
    for (int i = 0; i < 6; i++) {
      if (nextLeft == null || nextLeft.isHit() && !nextLeft.occupied()) {
        break;
      }
      marginLeft++;
      nextLeft = nextLeft.left;
    }
    HeatCoord nextRight = this.right;
    for (int i = 0; i < 6; i++) {
      if (nextRight == null || nextRight.isHit() && !nextRight.occupied()) {
        break;
      }
      marginRight++;
      nextRight = nextRight.right;
    }
    return new int[] {marginUp, marginDown, marginLeft, marginRight};
  }

  /**
   * Recalculate after we get a hit
   */
  public void recalculate() {
    int ships = 0;
    int[] margins = this.shipMargins();
    int marginUp = margins[0];
    ships += marginUp > 1 ? marginUp - 1 : 0;
    int marginDown = margins[1];
    ships += marginDown > 1 ? marginDown - 1 : 0;
    int marginLeft = margins[2];
    ships += marginLeft > 1 ? marginLeft - 1 : 0;
    int marginRight = margins[3];
    ships += marginRight > 1 ? marginRight - 1 : 0;
    if (marginUp > 0 && marginDown > 0 || marginLeft > 0 && marginRight > 0) {
      ships++;
    }
    if (ships == 0 || this.isHit()) {
      probability = 0;
    }
  }

  /**
   * Don't hit the same one again
   *
   * @return true or false
   */
  @Override
  public boolean doHit() {
    this.probability = 0;
    return super.doHit();
  }

  /**
   * Process hit tiles (hit adjacents)
   */
  public void doAdjacentHit() {
    if (this.up != null) {
      this.up.adjacentHit(1000);
    }
    if (this.down != null) {
      this.down.adjacentHit(1000);
    }
    if (this.left != null) {
      this.left.adjacentHit(1000);
    }
    if (this.right != null) {
      this.right.adjacentHit(1000);
    }
  }

  /**
   * Gets the probability of the coordinate
   *
   * @return the probability of the coordinate
   */
  public float getProbability() {
    return this.probability;
  }
}
