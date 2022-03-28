package service;

import static util.Tags.TAG_CONTACT;
import static util.Tags.TAG_MAIL;
import static util.Tags.TAG_NAME;
import static util.Tags.TAG_ORGANIZATION;
import static util.Tags.TAG_ORGANIZER;
import static util.Tags.TAG_PHONE_LIST;
import static util.Tags.TAG_PHONE_NUMBER;
import static util.Tags.TAG_POSITION;
import static util.Tags.TAG_TABLE_NUMBER;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Contact;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OrganizerXmlService implements OrganizerService {

  private static String pathToFile = "organizer.xml";
  private Document document;

  public void setPathToFile(String pathToFile) {

    OrganizerXmlService.pathToFile = pathToFile;
  }

  @Override
  public void addContact(Contact client) {

    document = getXMLContent(pathToFile);

    createNewContactXMLContext(client, document);

    saveXMLContent(document, pathToFile);
  }

  @Override
  public List<Contact> update(String searchField, String searchFieldValue, String field, String value) {

    document = getXMLContent(pathToFile);

    NodeList clients = document.getElementsByTagName(TAG_CONTACT);

    try {
      for (int i = 0; i < clients.getLength(); i++) {
        Element element = (Element) clients.item(i);

        if (element.getElementsByTagName(searchField)
            .item(0)
            .getTextContent()
            .equals(searchFieldValue)) {
          element.getElementsByTagName(field)
              .item(0)
              .setTextContent(value);
        }
      }
    }
    catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("The field: " + searchField + " does not exist");
    }

    saveXMLContent(document, pathToFile);

    return findContact(searchField, searchFieldValue);
  }

  @Override
  public void delete(String field, String fieldValue) {

    document = getXMLContent(pathToFile);

    NodeList clients = document.getElementsByTagName(TAG_CONTACT);
    for (int i = 0; i < clients.getLength(); i++) {
      Element element = (Element) clients.item(i);
      if (element.getElementsByTagName(field)
          .item(0)
          .getTextContent()
          .equals(fieldValue)) {
        element.getParentNode()
            .removeChild(element);
      }
    }

    saveXMLContent(document, pathToFile);
  }

  @Override
  public List<Contact> getClientsList() {

    List<Contact> contactsList = new ArrayList<>();

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      XmlHandler xmlHandler = new XmlHandler();
      parser.parse(new File(pathToFile), xmlHandler);

      contactsList = xmlHandler.getContacts();
      return contactsList;
    }
    catch (ParserConfigurationException | SAXException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      System.out.println("Set file path");
    }

    return contactsList;
  }

  @Override
  public List<Contact> findContact(String searchField, String searchFieldValue) {

    document = getXMLContent(pathToFile);
    List<Contact> contactsList = new ArrayList<>();

    NodeList clients = document.getElementsByTagName(TAG_CONTACT);

    try {
      for (int i = 0; i < clients.getLength(); i++) {
        Element element = (Element) clients.item(i);
        if (element.getElementsByTagName(searchField)
            .item(0)
            .getTextContent()
            .contains(searchFieldValue)) {

          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          Source xmlSource = new DOMSource(clients.item(i));
          Result outputTarget = new StreamResult(outputStream);
          TransformerFactory.newInstance()
              .newTransformer()
              .transform(xmlSource, outputTarget);
          InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

          SAXParserFactory factory = SAXParserFactory.newInstance();
          SAXParser parser = factory.newSAXParser();
          XmlHandler xmlHandler = new XmlHandler();
          parser.parse(is, xmlHandler);

          contactsList.addAll(xmlHandler.getContacts());
        }
      }
      return contactsList;
    }
    catch (NullPointerException e) {
      System.out.println("The search field: " + searchField + " does not exist");
    }
    catch (ParserConfigurationException | SAXException | TransformerException | IOException e) {
      e.printStackTrace();
    }

    return contactsList;
  }

  private void createNewContactXMLContext(Contact client, Document document) {

    Element contacts = document.getDocumentElement();
    Element newClient = document.createElement(TAG_CONTACT);

    Element tableNumber = document.createElement(TAG_TABLE_NUMBER);
    tableNumber.appendChild(document.createTextNode(Integer.toString(client.getTableNumber())));
    newClient.appendChild(tableNumber);

    Element name = document.createElement(TAG_NAME);
    name.appendChild(document.createTextNode(client.getName()));
    newClient.appendChild(name);

    Element position = document.createElement(TAG_POSITION);
    position.appendChild(document.createTextNode(client.getPosition()));
    newClient.appendChild(position);

    Element organization = document.createElement(TAG_ORGANIZATION);
    organization.appendChild(document.createTextNode(client.getOrganization()));
    newClient.appendChild(organization);

    Element mail = document.createElement(TAG_MAIL);
    mail.appendChild(document.createTextNode(client.getMail()));
    newClient.appendChild(mail);

    Element phoneList = document.createElement(TAG_PHONE_LIST);

    for (String phone : client.getPhoneList()) {
      Element phoneNumber = document.createElement(TAG_PHONE_NUMBER);
      phoneNumber.appendChild(document.createTextNode(phone));
      phoneList.appendChild(phoneNumber);
    }

    newClient.appendChild(phoneList);

    contacts.appendChild(newClient);
  }

  private Document createDocument() {

    Document document = null;

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      document = db.newDocument();
      Element organizer = document.createElement(TAG_ORGANIZER);
      document.appendChild(organizer);
    }
    catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return document;
  }

  private void saveXMLContent(Document document, String pathToFile) {

    try {
      TransformerFactory tff = TransformerFactory.newInstance();
      Transformer tf = tff.newTransformer();
      tf.setOutputProperty(OutputKeys.INDENT, "no");
      DOMSource ds = new DOMSource(document);
      StreamResult sr = new StreamResult(pathToFile);
      tf.transform(ds, sr);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Document getXMLContent(String pathToFile) {

    boolean isFileExist = false;
    long fileSize = 0L;
    try {
      isFileExist = Files.exists(Paths.get(pathToFile));
      fileSize = Files.size(Paths.get(pathToFile));
    }
    catch (IOException e) {
      System.out.println(e.getMessage());
    }

    if (!isFileExist || fileSize == 0) {
      return createDocument();
    }
    else {
      try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.parse(pathToFile);
      }
      catch (IOException e) {
        System.out.println("Set file path");
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return document;
    }
  }
}
