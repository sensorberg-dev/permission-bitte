package com.sensorberg.permissionbitte;

import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;

import java.util.Arrays;

public class Permission {
  private final String name;
  private final PermissionResult result;

  Permission(String name, PermissionResult result) {
    this.name = Preconditions.checkNotNull(name);
    this.result = Preconditions.checkNotNull(result);
  }

  @NonNull
  public String getName() {
    return name;
  }

  @NonNull
  public PermissionResult getResult() {
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permission that = (Permission) o;
    return name.equals(that.name) && result == that.result;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(new Object[]{name, result});
  }

}
