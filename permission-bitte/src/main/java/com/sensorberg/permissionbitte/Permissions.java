package com.sensorberg.permissionbitte;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

  private final List<Permission> permissions;

  Permissions(List<Permission> permissions) {
    this.permissions = permissions;
  }

  /**
   * Get all permissions.
   *
   * @return list of all permissions
   */
  @NonNull
  public List<Permission> getPermissions() {
    return permissions;
  }

  /**
   * Get a filtered list of permissions.
   *
   * @param permissionResult Filter parameter
   * @return list of Permission matching the given parameter
   */
  public List<Permission> filter(PermissionResult permissionResult) {
    List<Permission> result = new ArrayList<>();

    for (Permission permission : permissions) {
      if (permission.result == permissionResult) {
        result.add(permission);
      }
    }

    return result;
  }

  /**
   * Check if at least one permission has been denied.
   *
   * @return true if one permission matches PermissionResult.DENIED, false otherwise
   */
  public boolean deniedPermanently() {
    return hasPermissionResult(PermissionResult.DENIED);
  }

  /**
   * Checks if at least one permission needs to show rationale.
   *
   * @return true if one permission matches PermissionResult.SHOW_RATIONALE, false otherwise
   */
  public boolean showRationale() {
    return hasPermissionResult(PermissionResult.SHOW_RATIONALE);
  }

  private boolean hasPermissionResult(PermissionResult permissionResult) {
    for (Permission permission : permissions) {
      if (permission.result == permissionResult) {
        return true;
      }
    }

    return false;
  }
}