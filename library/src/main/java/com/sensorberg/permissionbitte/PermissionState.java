package com.sensorberg.permissionbitte;

public class PermissionState {

  public final Permission permission;
  public final String state;

  PermissionState(Permission permission, String state) {
    this.permission = permission;
    this.state = state;
  }

}