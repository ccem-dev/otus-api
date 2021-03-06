package org.ccem.otus.permissions.persistence.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ccem.otus.permissions.model.user.Permission;
import org.ccem.otus.permissions.model.user.SurveyGroupPermission;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class UserPermissionDTOTest {
  private static final String USER_PERMISSION_DTO_JSON = "{permissions:[{objectType:SurveyGroupPermission,email:test1@test}]}";
  private static final String SURVEY_GROUP_PERMISSION_JSON = "{objectType:SurveyGroupPermission,email:test1@test}";

  private UserPermissionDTO userPermissionDTO;
  private ArrayList<Permission> permissions;
  private Set<String> groups;
  private SurveyGroupPermission permission;

  @Before
  public void setup() {
    this.userPermissionDTO = new UserPermissionDTO();
    this.permissions = new ArrayList<Permission>();
    this.groups = new HashSet<String>();
    this.groups.add("Group");


    this.permission = new SurveyGroupPermission();
    Whitebox.setInternalState(this.permission, "objectType", "SurveyGroupPermission");
    Whitebox.setInternalState(this.permission, "email", "test1@test");
    Whitebox.setInternalState(this.permission, "groups", this.groups);
    this.permissions.add(this.permission);

    Whitebox.setInternalState(this.userPermissionDTO, "permissions", permissions);
  }

  @Test
  public void concatenatePermissions_method_should_return_concatenated_permissions() {
    UserPermissionDTO other = new UserPermissionDTO();
    ArrayList<Permission> otherPermissions = new ArrayList<Permission>();
    Set<String> groups = new HashSet<>();
    groups.add("Group1");
    Permission otherPermission = new SurveyGroupPermission();
    Whitebox.setInternalState(otherPermission, "objectType", "SurveyGroupPermission");
    Whitebox.setInternalState(otherPermission, "email", "test2@test");
    Whitebox.setInternalState(otherPermission, "groups", groups);
    otherPermissions.add(otherPermission);
    Whitebox.setInternalState(other, "permissions", otherPermissions);

    Assert.assertTrue(this.userPermissionDTO.getPermissions().get(0).equals(this.permission));
    this.userPermissionDTO.concatenatePermissions(other);
    Assert.assertTrue(this.userPermissionDTO.getPermissions().get(0).equals(otherPermission));
  }

  @Test
  public void concatenatePermissions_method_does_should_not_concatenated_when_permission_is_duplicate() {
    UserPermissionDTO other = new UserPermissionDTO();
    ArrayList<Permission> otherPermissions = new ArrayList<Permission>();
    Permission otherPermission = new Permission();
    Whitebox.setInternalState(otherPermission, "objectType", "Permission");
    Whitebox.setInternalState(otherPermission, "email", "test1@test");
    otherPermissions.add(otherPermission);
    Whitebox.setInternalState(other, "permissions", otherPermissions);

    this.userPermissionDTO.concatenatePermissions(other);
    Assert.assertEquals(1, this.userPermissionDTO.getPermissions().size());
    Assert.assertEquals("test1@test", this.userPermissionDTO.getPermissions().get(0).getEmail());
  }

  @Test
  public void deserialize_method_should_return_expected_UserPermissionDTO_with_elements() {
    UserPermissionDTO deserialized = UserPermissionDTO.deserialize(USER_PERMISSION_DTO_JSON);

    Assert.assertThat(deserialized, CoreMatchers.instanceOf(UserPermissionDTO.class));
    Assert.assertEquals("test1@test", deserialized.getPermissions().get(0).getEmail());
  }

  @Test
  public void deserializeSinglePermission_method_should_return_expected_UserPermissionDTO_with_elements() {
    UserPermissionDTO deserialized = UserPermissionDTO.deserializeSinglePermission(SURVEY_GROUP_PERMISSION_JSON);

    Assert.assertThat(deserialized, CoreMatchers.instanceOf(UserPermissionDTO.class));
    Assert.assertEquals("test1@test", deserialized.getPermissions().get(0).getEmail());
  }

  @Test
  public void getPermissions_method_should_return_expected_permissions() {
    List<Permission> permissions = this.userPermissionDTO.getPermissions();

    Assert.assertEquals("SurveyGroupPermission", permissions.get(0).getObjectType());
    Assert.assertEquals("test1@test", permissions.get(0).getEmail());
  }

}
