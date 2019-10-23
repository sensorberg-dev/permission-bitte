package com.sensorberg.permissionbitte;


/**
 * Result of permission.
 */
public enum PermissionResult {
  /**
   * Permission has been granted.
   */
  GRANTED,

  /**
   * Permission has not been granted and needs to be asked for.
   */
  DENIED_PLEASE_ASK,

  /**
   * Permission has been denied.
   */
  DENIED,

  /**
   * Permission requires to show a rationale.
   */
  SHOW_RATIONALE
}