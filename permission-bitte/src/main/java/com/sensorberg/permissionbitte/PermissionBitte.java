package com.sensorberg.permissionbitte;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;

/**
 * Entry point for easy permission requesting.
 */
public class PermissionBitte {

  private static final String TAG = "PERMISSION_BITTE";

  /**
   * Get LiveData of Permissions.
   *
   * @param activity an Activity
   * @return LiveData with Permissions
   */
  public static LiveData<Permissions> permissions(FragmentActivity activity) {
    return getOrCreate(activity).permissionLiveData;
  }

  /**
   * Ask for the permission. Which permission? Anything you register on your manifest that needs it.
   *
   * @param activity an Activity
   */
  public static void ask(FragmentActivity activity) {
    getOrCreate(activity).ask();
  }

  /**
   * Just a helper methods in case the user blocks permission.
   * It goes to your application settings page for the user to enable permission again.
   *
   * @param activity an Activity
   */
  public static void goToSettings(Activity activity) {
    activity.startActivity(new Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.getPackageName(), null)));
  }

  private static PermissionBitteFragment getOrCreate(FragmentActivity activity) {
    PermissionBitteFragment permissionBitteFragment = (PermissionBitteFragment) activity
            .getSupportFragmentManager()
            .findFragmentByTag(TAG);

    if (permissionBitteFragment == null) {
      permissionBitteFragment = new PermissionBitteFragment();

      activity.getSupportFragmentManager()
              .beginTransaction()
              .add(permissionBitteFragment, TAG)
              .commitNowAllowingStateLoss();
    }

    return permissionBitteFragment;
  }
}