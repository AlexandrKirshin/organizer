package service;

import java.util.List;
import model.Contact;

public interface OrganizerService {

  void setPathToFile(String pathToFile);

  void addContact(Contact client);

  List<Contact> update(String searchField, String searchFieldValue, String field, String value);

  void delete(String field, String fieldValue);

  List<Contact> getClientsList();

  List<Contact> findContact(String searchField, String searchFieldValue);

}
