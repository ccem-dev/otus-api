package br.org.otus.user.management;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.exceptions.webservice.http.EmailNotificationException;
import org.ccem.otus.exceptions.webservice.security.EncryptedException;
import org.ccem.otus.exceptions.webservice.validation.ValidationException;

import br.org.otus.email.service.EmailNotifierService;
import br.org.otus.email.user.management.DisableUserNotificationEmail;
import br.org.otus.email.user.management.EnableUserNotificationEmail;
import br.org.otus.model.User;
import br.org.otus.user.UserDao;
import br.org.otus.user.dto.ManagementUserDto;
import br.org.tutty.Equalizer;

@Stateless
public class ManagementUserServiceBean implements ManagementUserService {
    @Inject
    private UserDao userDao;

    @Inject
    private EmailNotifierService emailNotifierService;

    @Override
    public User fetchByEmail(String email) {
        return userDao.fetchByEmail(email);
    }

    @Override
    public void enable(ManagementUserDto managementUserDto) throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
        if (managementUserDto.isValid()) {
            User user = fetchByEmail(managementUserDto.getEmail());
            user.enable();

            userDao.update(user);

            EnableUserNotificationEmail enableUserNotificationEmail = new EnableUserNotificationEmail();
            enableUserNotificationEmail.defineRecipient(user);
            enableUserNotificationEmail.setFrom(emailNotifierService.getSender());
            emailNotifierService.sendEmail(enableUserNotificationEmail);
        }else{
            throw new ValidationException();
        }
    }

    @Override
    public void disable(ManagementUserDto managementUserDto) throws EmailNotificationException, EncryptedException, ValidationException, DataNotFoundException {
        if (managementUserDto.isValid()) {
            User user = fetchByEmail(managementUserDto.getEmail());

            if (!user.isAdmin()) {
                user.disable();

                userDao.update(user);

                DisableUserNotificationEmail disableUserNotificationEmail = new DisableUserNotificationEmail();
                disableUserNotificationEmail.defineRecipient(user);
                disableUserNotificationEmail.setFrom(emailNotifierService.getSender());
                emailNotifierService.sendEmail(disableUserNotificationEmail);
            }
        } else {
            throw new ValidationException();
        }
    }

    @Override
    public Boolean isUnique(String emailToVerify) {
        if (emailToVerify != null && userDao.emailExists(emailToVerify)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<ManagementUserDto> list() {
        List<ManagementUserDto> administrationUsersDtos = new ArrayList<>();
        List<User> users = userDao.fetchAll();

        users.stream().forEach(user -> {
            ManagementUserDto managementUserDto = new ManagementUserDto();

            Equalizer.equalize(user, managementUserDto);
            administrationUsersDtos.add(managementUserDto);
        });

        return administrationUsersDtos;
    }
}
