package de.tum.in.eist;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * POJO to store user data<br><br>
 * <b>Note:</b> You may notice that there are getters for every field of this class
 * even though they are not used by us.
 * This is because Jackson needs getters to convert a JSON into POJO.
 * We request Jersey to convert JSON into POJO when we add a new user in
 * {@link UserAccountManager} class and Jersey's default JSON mapper is Jackson.
 */
@Entity
public class UserData {
  @Id
  private String userId;
  private String userClass;
  private String email;
  private String city;
  private String country;
  private String firstName;
  private String lastName;
  private String pinCode;
  private String street;

  @SuppressWarnings("unused")
  private UserData() {}

  public UserData(String userClass,
      String email,
      String city,
      String country,
      String firstName,
      String lastName,
      String pinCode,
      String street,
      String userId) {
    this.userClass = requireNonNull(userClass);
    this.email = email;
    this.city = requireNonNull(city);
    this.country = requireNonNull(country);
    this.firstName = requireNonNull(firstName);
    this.lastName = requireNonNull(lastName);
    this.pinCode = pinCode;
    this.street = requireNonNull(street);
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getCity() {
    return city;
  }
  
  public String getUserClass() {
    return userClass;
  }
  
  public String getCountry() {
    return country;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public String getPinCode() {
    return pinCode;
  }
  
  public String getStreet() {
    return street;
  }

  @Override
  public int hashCode() {
    return Objects.hash(userClass,
        email,
        city,
        country,
        firstName,
        lastName,
        pinCode,
        street,
        userId);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UserData) {
      UserData other = (UserData) obj;
      return this.email.equals(other.email)
          && this.userClass.equals(other.userClass)
          && this.city.equals(other.city)
          && this.country.equals(other.country)
          && this.firstName.equals(other.firstName)
          && this.lastName.equals(other.lastName)
          && this.pinCode.equals(other.pinCode)
          && this.street.equals(other.street)
          && this.userId.equals(other.userId);
    }
    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("userClass", userClass)
        .add("email", email)
        .add("city", city)
        .add("country", country)
        .add("firstName", firstName)
        .add("lastName", lastName)
        .add("pinCode", pinCode)
        .add("street", street)
        .add("userId", userId)
        .toString();
  }
}
