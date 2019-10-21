package com.sensorberg.permissionbitte;

public class Permission {

  public final String name;
  public final PermissionResult result;

  Permission(String name, PermissionResult result) {
    this.name = name;
    this.result = result;
  }

}