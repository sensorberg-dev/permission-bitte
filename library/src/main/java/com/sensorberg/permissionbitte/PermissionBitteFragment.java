package com.sensorberg.permissionbitte;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * DO NOT USE THIS FRAGMENT DIRECTLY!
 * It's only here because fragments have to be public
 */
public class PermissionBitteFragment extends Fragment {

  private static final int BITTE_LET_ME_PERMISSION = 23;

  final MutableLiveData<Permission> liveData = new MutableLiveData<>();

  public PermissionBitteFragment() {
    setRetainInstance(true);
  }

  @Override
  public void onResume() {
    super.onResume();

    String[] needed = neededPermissions(getActivity());
    if (needed.length > 0) {
      requestPermissions(needed, BITTE_LET_ME_PERMISSION);
    } else {
      // this shouldn't happen, but just to be sure
      getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode != BITTE_LET_ME_PERMISSION || permissions.length <= 0) {
      return;
    }

    for (int i = 0; i < permissions.length; i++) {
      final String permissionName = permissions[i];

      if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
        updateLiveData(permissionName, PermissionResult.PERMISSION_DENIED);
      } else {
        updateLiveData(permissionName, PermissionResult.PERMISSION_GRANTED);
      }
    }

    getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
  }

  private void updateLiveData(String permissionName, PermissionResult permissionResult) {
    liveData.setValue(new Permission(permissionName, permissionResult));
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @NonNull
  static String[] neededPermissions(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo = null;
    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
    } catch (PackageManager.NameNotFoundException e) { /* */ }

    List<String> needed = new ArrayList<>();

    if (packageInfo != null
            && packageInfo.requestedPermissions != null
            && packageInfo.requestedPermissionsFlags != null) {

      for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
        int flags = packageInfo.requestedPermissionsFlags[i];
        String group = null;

        try {
          group = packageManager.getPermissionInfo(packageInfo.requestedPermissions[i], 0).group;
        } catch (PackageManager.NameNotFoundException e) { /* */ }

        if (((flags & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) && group != null) {
          needed.add(packageInfo.requestedPermissions[i]);
        }
      }
    }

    return needed.toArray(new String[0]);
  }
}
