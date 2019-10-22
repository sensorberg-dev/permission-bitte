package com.sensorberg.permissionbitte;


public enum PermissionResult {
  /**
   * Permission has been granted.
   */
  GRANTED,

  /**
   * Permission has not been granted and need to ask again.
   */
  DENIED_PLEASE_ASK,

  /**
   * Permission has been denied.
   */
  DENIED,

  /**
   * Permission needs to show a rationale.
   */
  SHOW_RATIONALE
}