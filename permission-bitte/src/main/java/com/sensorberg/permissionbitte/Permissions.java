package com.sensorberg.permissionbitte;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Permissions {

  private final Map<String, PermissionResult> permissions;

  Permissions(Map<String, PermissionResult> permissions) {
    this.permissions = permissions;
  }

  /**
   * Get all permissions.
   *
   * @return map of all permissions
   */
  @NonNull
  public Map<String, PermissionResult> getPermissions() {
    return permissions;
  }

  /**
   * Get a filtered set of permissions.
   *
   * @param permissionResult Filter parameter
   * @return set of Permission matching the given parameter
   */
  public Set<String> filter(PermissionResult permissionResult) {
    Set<String> set = new HashSet<>();

    for (String key : permissions.keySet()) {
      PermissionResult result = permissions.get(key);
      if (result == permissionResult) {
        set.add(key);
      }
    }

    return set;
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

  /**
   * Checks if at least one permission needs to be asked for.
   *
   * @return true if one permission matches PermissionResult.DENIED_PLEASE_ASK, false otherwise
   */
  public boolean needAskingForPermission() {
    return hasPermissionResult(PermissionResult.DENIED_PLEASE_ASK);
  }

  /**
   * Checks if all permissions have been granted - or no permissions required at all.
   *
   * @return true if all permission matches PermissionResult.GRANTED or no permissions at all required
   */
  public boolean allGranted() {
    for (PermissionResult result : permissions.values()) {
      if (result != PermissionResult.GRANTED) {
        return false;
      }
    }

    return true;
  }

  private boolean hasPermissionResult(PermissionResult permissionResult) {
    return permissions.containsValue(permissionResult);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permissions that = (Permissions) o;
    return Objects.equals(permissions, that.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permissions);
  }
}