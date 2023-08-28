### Controller class
 - Removed dependency injection
   - Simplifies the program.
 - Adjusted win system
   - Simplifies the program and makes it more consistent with the network specification.

### AbstractPlayer class
 - Removed dependency injection (and associated methods)
   - Game States can be determined from the controller via the Ship class.
 - Made endGame() abstract
   - Both child classes require different implementations of this method
 - Removed getter for board, changed visibility to protected instead.
   - Protected visibility means child classes can access it when needed. This is ideal because the InputPlayer only requires access to communicate this information to the View.

### AlgorithmPlayer class
 - Modified Heatmap to no longer be random
   - Improves performance against both the player and other algorithms.
 - Removed getter for heatmap
   - Not necessary to be displayed to the user.

### InputPlayer class
 - Removed dependency injection
   - Same reason as AbstractPlayer class
 - Removed getter for oppBoard
   - Instead, the view is directly given this data.
   - In the previous iteration it was redundant anyway

### Coord class
 - Adjusted visibility of x and y to protected, inserted getters.
   - Compliance with code quality customs.
 - Overridden equals
   - Easier time for comparison and tests.

### HeatCoord class
 - Added references to adjacent HeatCoords
   - Important for adjacency probability buffs during gameplay.
 - Added probability recalculation function
   - More advanced algorithm.
 - Added more robust hit behavior
   - More advanced algorithm.

### GameResult enum
 - Removed P1WIN, P2WIN, and ONGOING and added WIN
   - No longer necessary for new controller and win system. WIN was added for consistency with the networking specification.

### GameOverData class
 - Removed
   - No longer necessary for new controller and win system.

### GameView interface
 - Removed in favor of a GameView class
   - Interfaces with only one class implementing them are unnecessary.

### ConsoleView class
 - Renamed to GameView
   - Same reason as GameView interface removal.
 - Made board display non-static.
   - Static method was no longer as the only calls to this method are from instances of GameView
