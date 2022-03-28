package util.parser;

import java.util.Map;
import model.Contact;

public interface RequestParser {

  String getFilePath(String request);

  Contact getContact(String request);

  Map<String, String> getFindArgs(String request);

  Map<String, String> getUpdateArgs(String request);

  Map<String, String> getDeleteArgs(String request);
}
