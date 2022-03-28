package model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Contact {

  private int tableNumber;
  private String name;
  private String position;
  private String organization;
  private String mail;
  private List<String> phoneList = new ArrayList<>();

  public void addPhoneNumber(String phoneNumber) {

    phoneList.add(phoneNumber);
  }

  @Override
  public String toString() {

    return "\nContact {" +
        "\n tableNumber = " + tableNumber +
        ",\n Name: '" + name + '\'' +
        ",\n Position: '" + position + '\'' +
        ",\n Organization: '" + organization + '\'' +
        ",\n Mail: '" + mail + '\'' +
        ",\n Phone: " + phoneList +
        '}';
  }
}