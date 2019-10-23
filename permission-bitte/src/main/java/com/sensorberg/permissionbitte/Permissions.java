package com.sensorberg.permissionbitte;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Gives access to all required permissions in manifest and their result.
 */
public class Permissions {

  private final Map<String, PermissionResult> map;

  Permissions(Map<String, PermissionResult> map) {
    this.map = map;
  }

  /**
   * Get all permissions.
   *
   * @return Map of all permissions
   */
  @NonNull
  public Map<String, PermissionResult> getPermissions() {
    return map;
  }

  /**
   * Get a filtered set of permissions.
   *
   * @param permissionResult filter parameter
   * @return Set of permission names matching the filter parameter
   */
  public Set<String> filter(PermissionResult permissionResult) {
    Set<String> set = new HashSet<>();

    for (String key : map.keySet()) {
      PermissionResult result = map.get(key);
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
    for (PermissionResult result : map.values()) {
      if (result != PermissionResult.GRANTED) {
        return false;
      }
    }

    return true;
  }

  private boolean hasPermissionResult(PermissionResult permissionResult) {
    return map.containsValue(permissionResult);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permissions that = (Permissions) o;

    Object a = map;
    Object b = that.map;

    return (a == b) || (a != null && a.equals(b));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(new Object[]{map});
  }

}