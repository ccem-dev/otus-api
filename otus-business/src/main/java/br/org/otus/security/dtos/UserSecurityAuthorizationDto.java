package br.org.otus.security.dtos;

import org.ccem.otus.exceptions.Dto;
import br.org.tutty.Equalization;
import org.ccem.otus.exceptions.webservice.security.EncryptedException;

public class UserSecurityAuthorizationDto implements Dto{

    @Equalization(name = "name")
    private String name;

    @Equalization(name = "surname")
    private String surname;

    @Equalization(name = "phone")
    private String phone;

    @Equalization(name = "email")
    private String email;

    @Equalization(name = "token")
    private String token;

    @Equalization(name = "code")
    private Integer code;

    @Override
    public Boolean isValid() {
        return Boolean.TRUE;
    }

    @Override
    public void encrypt() throws EncryptedException {
    }

    public void setToken(String token) {
        this.token = token;
    }
}
