package ae.ac.ku.pizza;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

  private String
    firstName,
    lastName,
    emailAddress,
    streetAddress,
    buildingNumber,
    floorNumber,
    apartmentNumber;

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

  public User(String firstName, String lastName, String emailAddress, String streetAddress, String buildingNumber, String floorNumber, String apartmentNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.streetAddress = streetAddress;
    this.buildingNumber = buildingNumber;
    this.floorNumber = floorNumber;
    this.apartmentNumber = apartmentNumber;
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(firstName);
    dest.writeString(lastName);
    dest.writeString(emailAddress);
    dest.writeString(streetAddress);
    dest.writeString(buildingNumber);
    dest.writeString(floorNumber);
    dest.writeString(apartmentNumber);
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
    emailAddress = in.readString();
    streetAddress = in.readString();
    buildingNumber = in.readString();
    floorNumber = in.readString();
    apartmentNumber = in.readString();
  }
}
