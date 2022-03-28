import service.OrganizerXmlService;
import util.ConsoleListener;
import util.handler.RequestHandler;
import util.handler.RequestHandlerImpl;
import util.parser.RequestParser;
import util.parser.RequestParserImpl;

public class Main {

  public static void main(String[] args) {

    RequestParser requestParser = new RequestParserImpl();
    OrganizerXmlService xmlService = new OrganizerXmlService();

    RequestHandler requestHandler = new RequestHandlerImpl(requestParser, xmlService);
    ConsoleListener consoleListener = new ConsoleListener(requestHandler);

    consoleListener.run();
  }
}
