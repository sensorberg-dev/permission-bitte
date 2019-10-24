package com.sensorberg.permissionbitte;

import java.util.Arrays;

public class Permission {
  private final String name;
  private final PermissionResult result;

  public Permission(String name, PermissionResult result) {
    this.name = name;
    this.result = result;
  }

  public String getName() {
    return name;
  }

  public PermissionResult getResult() {
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permission that = (Permission) o;

    Object a = name;
    Object b = that.name;

    return (a == b) || (a != null && a.equals(b));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(new Object[]{name, result});
  }

}
