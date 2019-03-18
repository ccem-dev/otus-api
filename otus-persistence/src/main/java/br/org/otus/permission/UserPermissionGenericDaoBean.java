package br.org.otus.permission;

import javax.inject.Inject;

import org.ccem.otus.exceptions.webservice.common.DataNotFoundException;
import org.ccem.otus.permissions.model.user.Permission;
import org.ccem.otus.permissions.persistence.user.UserPermissionDTO;
import org.ccem.otus.permissions.persistence.user.UserPermissionDao;
import org.ccem.otus.permissions.persistence.user.UserPermissionGenericDao;
import org.ccem.otus.permissions.persistence.user.UserPermissionProfileDao;

import java.util.List;
import java.util.stream.Collectors;

public class UserPermissionGenericDaoBean implements UserPermissionGenericDao {

  private static final String DEFAULT_PROFILE = "DEFAULT";

  @Inject
  private UserPermissionDao userPermissionDao;

  @Inject
  private UserPermissionProfileDao userPermissionProfileDao;

  @Override
  public UserPermissionDTO getUserPermissions(String email) throws DataNotFoundException {
    UserPermissionDTO userCustomPermission = userPermissionDao.getAll(email);
    UserPermissionDTO permissionProfile = userPermissionProfileDao.getProfile(DEFAULT_PROFILE);

    permissionProfile.concatenatePermissions(userCustomPermission);
    
    return permissionProfile;
  }

  @Override
  public Permission savePermission(Permission permission) throws DataNotFoundException {
    UserPermissionDTO permissionProfile = userPermissionProfileDao.getProfile(DEFAULT_PROFILE);
    List<Permission> permissionFound = permissionProfile.getPermissions().stream().filter(profilePermission -> profilePermission.equals(permission)).collect(Collectors.toList());
    if(!permissionFound.isEmpty()){
      userPermissionDao.deletePermission(permission);
    } else {
      userPermissionDao.savePermission(permission);
    }
    return permission;
  }



}
