package com.sensorberg.permissionbitte;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * DO NOT USE THIS FRAGMENT DIRECTLY!
 * It's only here because fragments have to be public
 */
public class PermissionBitteFragment extends Fragment {

  private static final int BITTE_LET_ME_PERMISSION = 23;

  private final MutableLiveData<Permissions> mutableLiveData = new MutableLiveData<>();

  private boolean askForPermission = false;

  public PermissionBitteFragment() {
    setRetainInstance(true);
  }

  @Override
  public void onResume() {
    super.onResume();

    if (askForPermission) {
      askForPermission = false;
      ask();
    }
  }

  LiveData<Permissions> getPermission() {
    updateData();
    return mutableLiveData;
  }

  void ask() {
    if (!isResumed()) {
      askForPermission = true;
      return;
    }

    List<Permission> allPermissions = getPermissionList(getActivity());
    if (allPermissions.isEmpty()) {
      // this shouldn't happen, but just to be sure
      getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
      return;
    }

    List<String> permissionNames = new ArrayList<>();

    for (Permission permission : allPermissions) {
      permissionNames.add(permission.name);
    }

    if (!permissionNames.isEmpty()) {
      requestPermissions(permissionNames.toArray(new String[0]), BITTE_LET_ME_PERMISSION);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode != BITTE_LET_ME_PERMISSION || permissions.length <= 0) {
      return;
    }

    List<Permission> permissionList = new ArrayList<>();

    for (int i = 0; i < permissions.length; i++) {
      final String name = permissions[i];

      if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
        if (shouldShowRequestPermissionRationale(name)) {
          permissionList.add(new Permission(name, PermissionResult.SHOW_RATIONALE));
        } else {
          permissionList.add(new Permission(name, PermissionResult.DENIED));
        }

      } else {
        permissionList.add(new Permission(name, PermissionResult.GRANTED));
      }
    }

    mutableLiveData.setValue(new Permissions(permissionList));
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @NonNull
  private List<Permission> getPermissionList(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;

    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
    } catch (PackageManager.NameNotFoundException e) { /* ignore */ }

    List<Permission> permissions = new ArrayList<>();

    if (packageInfo == null
            || packageInfo.requestedPermissions == null
            || packageInfo.requestedPermissionsFlags == null) {
      return permissions;
    }

    for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
      int flags = packageInfo.requestedPermissionsFlags[i];
      String group = null;

      try {
        group = packageManager.getPermissionInfo(packageInfo.requestedPermissions[i], 0).group;
      } catch (PackageManager.NameNotFoundException e) { /* ignore */ }

      String name = packageInfo.requestedPermissions[i];
      if (((flags & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) && group != null) {
        if (shouldShowRequestPermissionRationale(name)) {
          permissions.add(new Permission(name, PermissionResult.SHOW_RATIONALE));
        } else {
          permissions.add(new Permission(name, PermissionResult.DENIED_PLEASE_ASK));
        }
      } else {
        permissions.add(new Permission(name, PermissionResult.GRANTED));
      }
    }

    return permissions;
  }

  private void updateData() {
    List<Permission> permissionList = getPermissionList(getActivity());
    mutableLiveData.setValue(new Permissions(permissionList));
  }
}
