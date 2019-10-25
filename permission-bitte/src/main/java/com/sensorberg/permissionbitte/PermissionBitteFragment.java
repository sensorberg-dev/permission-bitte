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
import androidx.lifecycle.Transformations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DO NOT USE THIS FRAGMENT DIRECTLY!
 * It's only here because fragments have to be public
 */
public class PermissionBitteFragment extends Fragment {

  private static final int BITTE_LET_ME_PERMISSION = 23;

  private final MutableLiveData<Permissions> mutableLiveData = new MutableLiveData<>();
  final LiveData<Permissions> permissionLiveData = Transformations.distinctUntilChanged(mutableLiveData);

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

    updateData();
  }

  void ask() {
    if (!isResumed()) {
      askForPermission = true;
      return;
    }

    Map<String, PermissionResult> allPermissions = getPermissions(getActivity());
    if (allPermissions.isEmpty()) {
      // no permissions to handle
      getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
      return;
    }

    Set<String> permissionNames = allPermissions.keySet();

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

    Map<String, PermissionResult> permissionMap = new HashMap<>();

    for (int i = 0; i < permissions.length; i++) {
      final String name = permissions[i];

      if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
        if (shouldShowRequestPermissionRationale(name)) {
          permissionMap.put(name, PermissionResult.SHOW_RATIONALE);
        } else {
          permissionMap.put(name, PermissionResult.DENIED);
        }

      } else {
        permissionMap.put(name, PermissionResult.GRANTED);
      }
    }

    mutableLiveData.setValue(new Permissions(permissionMap));
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @NonNull
  private Map<String, PermissionResult> getPermissions(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;

    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
    } catch (PackageManager.NameNotFoundException e) { /* ignore */ }

    Map<String, PermissionResult> permissions = new HashMap<>();
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
      if (group != null) {
        if ((flags & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) {
          if (shouldShowRequestPermissionRationale(name)) {
            permissions.put(name, PermissionResult.SHOW_RATIONALE);
          } else {
            permissions.put(name, PermissionResult.REQUEST_PERMISSION);
          }
        } else {
          permissions.put(name, PermissionResult.GRANTED);
        }
      }
    }

    return permissions;
  }

  private void updateData() {
    Map<String, PermissionResult> permissionMap = getPermissions(getActivity());

    // to not loose denied state during onResume(), permissionMap gets updated with previously DENIED permissions
    Permissions lastKnownPermissions = mutableLiveData.getValue();

    if (lastKnownPermissions != null) {
      Set<Permission> deniedPermissions = lastKnownPermissions.filter(PermissionResult.DENIED);

      for (Permission deniedPermission : deniedPermissions) {
        String deniedPermissionName = deniedPermission.getName();
        PermissionResult permissionResult = permissionMap.get(deniedPermissionName);

        if (permissionResult != null && permissionResult != PermissionResult.GRANTED) {
          permissionMap.put(deniedPermissionName, PermissionResult.DENIED);
        }
      }
    }

    mutableLiveData.setValue(new Permissions(permissionMap));
  }
}
