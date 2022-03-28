package util.parser;

import java.util.HashMap;
import java.util.Map;
import model.Contact;

public class RequestParserImpl implements RequestParser {

  @Override
  public String getFilePath(String request) {

    return request.substring(12)
        .trim();
  }

  @Override
  public Contact getContact(String request) {

    Contact client = new Contact();

    try {
      String[] args = request.substring(4)
          .split(",");

      client.setTableNumber(Integer.parseInt(args[0].trim()));
      client.setName(args[1].trim());
      client.setPosition(args[2].trim());
      client.setOrganization(args[3].trim());
      client.setMail(args[4].trim());
      for (int i = 5; i < args.length; i++) {
        client.addPhoneNumber(args[i].trim());
      }
    }
    catch (IndexOutOfBoundsException | NumberFormatException e) {
      System.out.println("Saved");
    }

    return client;
  }

  @Override
  public Map<String, String> getFindArgs(String request) {

    String[] args = request.substring(6)
        .split(",");

    Map<String, String> findArgs = new HashMap<>();

    try {
      findArgs.put("searchField", args[0].trim());
      findArgs.put("searchFieldValue", args[1].trim());
    }
    catch (ArrayIndexOutOfBoundsException e) {
      System.out.println(
          "Maybe you forgot the comma between the data, or the data is incomplete, you need the search field and the "
              + "value. Hint: /find <searchField>, <searchFieldValue> ");
    }

    return findArgs;
  }

  @Override
  public Map<String, String> getUpdateArgs(String request) {

    String[] args = request.substring(8)
        .split(",");

    Map<String, String> updateArgs = new HashMap<>();

    try {
      updateArgs.put("searchField", args[0].trim());
      updateArgs.put("searchFieldValue", args[1].trim());
      updateArgs.put("field", args[2].trim());
      updateArgs.put("value", args[3].trim());
    }
    catch (ArrayIndexOutOfBoundsException e) {
      System.out.println(
          "Maybe you forgot the comma between the data, or the data is incomplete. Hint: /update <searchField>, "
              + "<searchFieldValue>, <updatingField>, <newValue>");
    }

    return updateArgs;
  }

  @Override
  public Map<String, String> getDeleteArgs(String request) {

    String[] args = request.substring(7)
        .split(",");

    Map<String, String> deleteArgs = new HashMap<>();

    try {
      deleteArgs.put("field", args[0].trim());
      deleteArgs.put("fieldValue", args[1].trim());
    }
    catch (ArrayIndexOutOfBoundsException e) {
      System.out.println(
          "Maybe you forgot the comma between the data, or the data is incomplete. Hint: /delete <searchField>, <searchFieldValue>");
    }

    return deleteArgs;
  }
}
