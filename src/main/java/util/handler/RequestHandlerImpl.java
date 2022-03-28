package util.handler;

import java.util.List;
import java.util.Map;
import model.Contact;
import service.OrganizerService;
import service.OrganizerXmlService;
import util.parser.RequestParser;

public class RequestHandlerImpl implements RequestHandler {

  private final RequestParser requestParser;
  private final OrganizerService xmlService;

  public RequestHandlerImpl(RequestParser requestParser, OrganizerXmlService xmlService) {

    this.requestParser = requestParser;
    this.xmlService = xmlService;
  }

  @Override
  public void handleRequest(String request) {

    if (request.equals("/help")) {
      System.out.println("Use a comma to separate the data! \n" +
          "The organizer has the following fields: tableNumber(numeric value), name, position, organization, "
          + "mail, phoneNumber/ \n" +
          "Import file - /setFiePath <pathToFile> \n" +
          "Add contact - /add <tableNumber(numeric value)>, <name>, <position>, <organization>, <mail>, <phoneNumber>\n"
          +
          "Update contact - /update <searchField>, <searchFieldValue>, <updatingField>, <newValue> \n" +
          "Delete contact - /delete <searchField>, <searchFieldValue> \n" +
          "Show list contact - /get \n" +
          "Find contact - /find <searchField>, <searchFieldValue>"
      );
    }

    if (request.startsWith("/setFilePath")) {

      String pathToFile = requestParser.getFilePath(request);
      xmlService.setPathToFile(pathToFile);
    }

    if (request.startsWith("/add")) {

      Contact client = requestParser.getContact(request);
      xmlService.addContact(client);
    }

    if (request.startsWith("/update")) {

      Map<String, String> updateArgs = requestParser.getUpdateArgs(request);

      String searchField = updateArgs.get("searchField");
      String searchFieldValue = updateArgs.get("searchFieldValue");
      String field = updateArgs.get("field");
      String value = updateArgs.get("value");

      List<Contact> contactsList = xmlService.update(searchField, searchFieldValue, field, value);
      System.out.println(contactsList);
    }

    if (request.startsWith("/delete")) {

      Map<String, String> deleteArgs = requestParser.getDeleteArgs(request);
      String field = deleteArgs.get("field");
      String fieldValue = deleteArgs.get("fieldValue");

      xmlService.delete(field, fieldValue);
    }

    if (request.startsWith("/get")) {

      List<Contact> clientsList = xmlService.getClientsList();
      System.out.println(clientsList);
    }

    if (request.startsWith("/find")) {

      Map<String, String> findArgs = requestParser.getFindArgs(request);
      String searchField = findArgs.get("searchField");
      String searchFieldValue = findArgs.get("searchFieldValue");

      List<Contact> contactsList = xmlService.findContact(searchField, searchFieldValue);
      System.out.println(contactsList);
    }
  }
}
