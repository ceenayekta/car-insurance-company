import java.io.Serializable;

public class Address implements Cloneable, Comparable<Address>, Serializable {
  private int streetNum;
  private String street;
  private String suburb;
  private String city;
  public final static String delimitedKey = "A";
  
  public Address(int streetNum, String street, String suburb, String city) {
    this.streetNum = streetNum;
    this.street = street;
    this.suburb = suburb;
    this.city = city;
  }

  public Address(Address address) {
    this.streetNum = address.streetNum;
    this.street = address.street;
    this.suburb = address.suburb;
    this.city = address.city;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public int getStreetNum() {
    return streetNum;
  }

  public void setStreetNum(int streetNum) {
    this.streetNum = streetNum;
  }

  public String getSuburb() {
    return suburb;
  }
  
  public void setSuburb(String suburb) {
    this.suburb = suburb;
  }

  public void print() {
    System.out.println("StreetNum: " + streetNum + " Street: " + street + " Suburb: " + suburb + " City: " + city);
  }

  public String toString() {
    return "StreetNum: " + streetNum + " Street: " + street + " Suburb: " + suburb + " City: " + city;
  }

  //lab4
  @Override
  public Address clone() throws CloneNotSupportedException {
    return (Address) super.clone();
  }

  @Override
  public int compareTo(Address a) {
    return city.compareTo(a.city);
  }

  //lab6
  public String toDelimitedString() {
    return delimitedKey + "," + streetNum + "," + street + "," + suburb + "," + city;
  }
}
