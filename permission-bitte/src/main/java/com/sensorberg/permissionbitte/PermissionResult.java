package com.sensorberg.permissionbitte;


import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

/**
 * Result of permission.
 */
public enum PermissionResult {
  /**
   * Permission has been granted.
   */
  GRANTED,

  /**
   * Permission have not been requested yet and it's implicitly denied by the Android framework until the user accepts it.
   * See {@link PermissionBitte#ask(FragmentActivity)}
   */
  REQUEST_PERMISSION,

  /**
   * Permission has been permanently denied by the user.
   * The only way to acquire this permission is to instruct the user to go to the settings and do it manually.
   * See {@link PermissionBitte#goToSettings(Activity)}
   */
  DENIED,

  /**
   * Permission has been previously denied by the user and the client application should show UI with rationale for requesting a permission.
   * See <a href="https://developer.android.com/reference/android/app/Activity.html#shouldShowRequestPermissionRationale(java.lang.String)">Activity.shouldShowRequestPermissionRationale</a>
   */
  SHOW_RATIONALE
}