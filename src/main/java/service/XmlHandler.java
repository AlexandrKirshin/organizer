package service;

import static util.Tags.TAG_CONTACT;
import static util.Tags.TAG_MAIL;
import static util.Tags.TAG_NAME;
import static util.Tags.TAG_ORGANIZATION;
import static util.Tags.TAG_PHONE_NUMBER;
import static util.Tags.TAG_POSITION;
import static util.Tags.TAG_TABLE_NUMBER;

import java.util.ArrayList;
import java.util.List;
import model.Contact;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler {

  private Contact contact;
  private final List<Contact> contacts = new ArrayList<>();

  private boolean isContact;
  private String currentTag;

  public List<Contact> getContacts() {

    return contacts;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {

    currentTag = qName;

    if (qName.equals(TAG_CONTACT)) {
      isContact = true;
      contact = new Contact();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {

    if (qName.equals(TAG_CONTACT)) {
      isContact = false;
      contacts.add(contact);
    }

    currentTag = null;
  }

  @Override
  public void characters(char[] ch, int start, int length) {

    if (currentTag == null) {
      return;
    }

    if (isContact) {

      switch (currentTag) {

        case TAG_TABLE_NUMBER:
          String tableNumber = new String(ch, start, length).trim();
          contact.setTableNumber(Integer.parseInt(tableNumber));
          break;

        case TAG_NAME:
          String name = new String(ch, start, length);
          contact.setName(name);
          break;

        case TAG_POSITION:
          contact.setPosition(new String(ch, start, length));
          break;

        case TAG_ORGANIZATION:
          contact.setOrganization(new String(ch, start, length));
          break;

        case TAG_MAIL:
          contact.setMail(new String(ch, start, length));
          break;

        case TAG_PHONE_NUMBER:
          contact.getPhoneList()
              .add(new String(ch, start, length));
          break;
      }
    }
  }
}
