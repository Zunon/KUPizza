package ae.ac.ku.pizza;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

  private String
    firstName;
  private String lastName;
  private String phoneNumber;

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  private String emailAddress;
  private String streetAddress;
  private String buildingNumber;
  private String floorNumber;
  private String apartmentNumber;
  private String location;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public String getBuildingNumber() {
    return buildingNumber;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public String getFloorNumber() {
    return floorNumber;
  }

  public void setFloorNumber(String floorNumber) {
    this.floorNumber = floorNumber;
  }

  public String getApartmentNumber() {
    return apartmentNumber;
  }

  public void setApartmentNumber(String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public User(String firstName, String lastName, String phoneNumber, String emailAddress, String streetAddress, String buildingNumber, String floorNumber, String apartmentNumber, String location) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
    this.streetAddress = streetAddress;
    this.buildingNumber = buildingNumber;
    this.floorNumber = floorNumber;
    this.apartmentNumber = apartmentNumber;
    this.location = location;
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(firstName);
    dest.writeString(lastName);
    dest.writeString(phoneNumber);
    dest.writeString(emailAddress);
    dest.writeString(streetAddress);
    dest.writeString(buildingNumber);
    dest.writeString(floorNumber);
    dest.writeString(apartmentNumber);
    dest.writeString(location);
  }

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    public User createFromParcel(Parcel par) {
      return new User(par);
    }

    public User[] newArray(int size) {
      return new User[size];
    }
  };

  public User(Parcel in) {
    firstName = in.readString();
    lastName = in.readString();
    phoneNumber = in.readString();
    emailAddress = in.readString();
    streetAddress = in.readString();
    buildingNumber = in.readString();
    floorNumber = in.readString();
    apartmentNumber = in.readString();
    location = in.readString();
  }
}
