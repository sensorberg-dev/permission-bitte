package com.sensorberg.permissionbitte;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

  private final List<Permission> permissions;

  Permissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

  public boolean showRationale() {
    return !getPermissions(PermissionResult.SHOW_RATIONALE).isEmpty();
  }

  @NonNull
  public List<Permission> getPermissions(PermissionResult... permissionResults) {
    List<Permission> result = new ArrayList<>();

    for (Permission permission : permissions) {
      for (PermissionResult permissionResult : permissionResults) {
        if (permissionResult == permission.result) {
          result.add(permission);
        }
      }
    }

    return result;
  }
}
