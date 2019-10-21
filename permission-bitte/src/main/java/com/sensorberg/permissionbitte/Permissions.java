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
    return hasPermissionResult(PermissionResult.SHOW_RATIONALE);
  }

  public List<Permission> get(PermissionResult permissionResult) {
    List<Permission> result = new ArrayList<>();

    for (Permission permission : permissions) {
      if (permission.result == permissionResult) {
        result.add(permission);
      }
    }

    return result;
  }

  public boolean deniedPermanently() {
    return hasPermissionResult(PermissionResult.DENIED);
  }

  private boolean hasPermissionResult(PermissionResult permissionResult) {
    return !get(permissionResult).isEmpty();
  }

  @NonNull
  public List<Permission> getPermissions() {
    return permissions;
  }
}