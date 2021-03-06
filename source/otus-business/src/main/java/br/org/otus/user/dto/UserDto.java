package br.org.otus.user.dto;

import br.org.otus.email.EmailSender;
import br.org.otus.security.EncryptorResources;
import br.org.tutty.Equalization;
import org.ccem.otus.exceptions.Dto;
import org.ccem.otus.exceptions.Encripting;
import org.ccem.otus.exceptions.webservice.security.EncryptedException;

public class UserDto implements EmailSender, Dto, Encripting {

  @Equalization(name = "name")
  private String name;

  @Equalization(name = "surname")
  private String surname;

  @Equalization(name = "phone")
  private String phone;

  @Equalization(name = "email")
  private String email;

  @Equalization(name = "password")
  private String password;

  private String passwordConfirmation;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordConfirmation() {
    return passwordConfirmation;
  }

  public void setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
  }


  @Override
  public Boolean isValid() {
    return Boolean.TRUE;
  }

  @Override
  public void encrypt() throws EncryptedException {
    this.password = EncryptorResources.encryptIrreversible(password);
    this.passwordConfirmation = EncryptorResources.encryptIrreversible(passwordConfirmation);
  }

}