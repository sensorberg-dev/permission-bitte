package com.sensorberg.permissionbitte;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;

import java.util.Arrays;

public class Permission implements Parcelable {

  @NonNull
  private final String name;

  @NonNull
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

  @Override
  public String toString() {
    return "Permission{" +
            "name='" + name + '\'' +
            ", result=" + result +
            '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeInt(this.result.ordinal());
  }

  protected Permission(Parcel in) {
    this.name = Preconditions.checkNotNull(in.readString());
    this.result = PermissionResult.values()[in.readInt()];
  }

  public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() {
    @Override
    public Permission createFromParcel(Parcel source) {
      return new Permission(source);
    }

    @Override
    public Permission[] newArray(int size) {
      return new Permission[size];
    }
  };
}
