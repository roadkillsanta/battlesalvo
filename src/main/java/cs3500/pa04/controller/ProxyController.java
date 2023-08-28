package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.model.json.CoordRecord;
import cs3500.pa04.model.json.EndMessage;
import cs3500.pa04.model.json.JoinResponse;
import cs3500.pa04.model.json.NetMessage;
import cs3500.pa04.model.json.SetupMessage;
import cs3500.pa04.model.json.SetupResponse;
import cs3500.pa04.model.json.VolleyMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

/**
 * Represents the ProxyController class
 */
public class ProxyController {
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final NetGameController game;
  private final ObjectMapper mapper = new ObjectMapper();
  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");

  /**
   * Constructor
   *
   * @param server socket io
   * @param game real controller
   * @throws IOException io issues
   */
  public ProxyController(Socket server, NetGameController game) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.game = game;
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        NetMessage message = parser.readValueAs(NetMessage.class);
        this.delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(NetMessage message) {
    String method = message.method();
    JsonNode args = message.args();
    JsonNode response = VOID_RESPONSE;
    switch (method) {
      case "join" -> response = this.join();
      case "setup" -> response = this.setup(args);
      case "take-shots" -> response = this.takeShots();
      case "report-damage" -> response = this.reportDamage(args);
      case "successful-hits" -> this.successfulHits(args);
      case "end-game" -> this.end(args);
      default -> throw new IllegalArgumentException();
    }
    NetMessage responseMessage = new NetMessage(method, response);
    this.out.println(this.serialize(responseMessage));
  }

  /**
   * When joining the server
   *
   * @return serialized version of the record for join
   */
  private JsonNode join() {
    return this.serialize(new JoinResponse("roadkillsanta", game.getGameType()));
  }

  /**
   * SetUp for JsonNode
   *
   * @param args the argument passed for setup
   * @return return the JsonNode for the setup method
   */
  private JsonNode setup(JsonNode args) {
    SetupMessage message = this.mapper.convertValue(args, SetupMessage.class);
    SetupResponse response = new SetupResponse(game.setup(message));
    return this.serialize(response);
  }

  /**
   * TakeShots JsonNode
   *
   * @return returns the JsonNode for the takeShots method
   */
  private JsonNode takeShots() {
    VolleyMessage volley = new VolleyMessage(this.game.takeShots());
    return this.serialize(volley);
  }

  /**
   * Report Damage JsonNode argument
   *
   * @param args the jsonNode argument for successful hit
   * @return returns the Json Node for report damage
   */
  private JsonNode reportDamage(JsonNode args) {
    VolleyMessage message = this.mapper.convertValue(args, VolleyMessage.class);
    List<CoordRecord> damages = this.game.reportDamage(message);
    return this.serialize(new VolleyMessage(damages));
  }

  /**
   * successful Hits argument
   *
   * @param args the jsonNode argument for successful hit
   */
  private void successfulHits(JsonNode args) {
    VolleyMessage message = this.mapper.convertValue(args, VolleyMessage.class);
    this.game.successfulHits(message);
  }

  /**
   * Endgame argument
   *
   * @param args the jsonNode argument for end game
   */
  private void end(JsonNode args) {
    EndMessage message = this.mapper.convertValue(args, EndMessage.class);
    this.game.endGame(message);
  }

  /**
   * Serialize the record
   *
   * @param record the record being serialized
   * @return returns a JsonNode
   * @throws IllegalArgumentException Throws an exception if it can't be serialized
   */
  private JsonNode serialize(Record record) throws IllegalArgumentException {
    try {
      return this.mapper.convertValue(record, JsonNode.class);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Given record cannot be serialized");
    }
  }
}
