package com.sensorberg.permissionbitte;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * Entry point for easy permission requesting.
 */
public class PermissionBitte {

  private static final String TAG = "PERMISSION_BITTE";

  private static final MediatorLiveData<Permission> mediatorLiveData = new MediatorLiveData<>();
  public static final LiveData<Permission> permissionLiveData = mediatorLiveData;

  /**
   * Check if you need to ask for permission.
   *
   * @param context a Context
   * @return true when you should ask for permission, false otherwise
   */
  public static boolean shouldAsk(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return PermissionBitteFragment.neededPermissions(context).length > 0;
    } else {
      return false;
    }
  }

  /**
   * Ask for the permission. Which permission? Anything you register on your manifest that needs it.
   * It is safe to call this every time without querying `shouldAsk`.
   * In case you call `ask` without needing any permission, bitteBitte will immediately receive `yesYouCan()`
   *
   * @param activity   "this"
   */
  public static void ask(FragmentActivity activity) {
    if (!shouldAsk(activity)) {
      return;
    }

    PermissionBitteFragment permissionBitteFragment = (PermissionBitteFragment) activity
            .getSupportFragmentManager()
            .findFragmentByTag(TAG);

    if (permissionBitteFragment == null) {
      permissionBitteFragment = new PermissionBitteFragment();

      mediatorLiveData.addSource(permissionBitteFragment.liveData, new Observer<Permission>() {
        @Override
        public void onChanged(Permission permission) {
          mediatorLiveData.setValue(permission);
        }
      });

      activity.getSupportFragmentManager()
              .beginTransaction()
              .add(permissionBitteFragment, TAG)
              .commitNowAllowingStateLoss();
    }
  }

  /**
   * Just a helper methods in case the user blocks permission.
   * It goes to your application settings page for the user to enable permission again.
   *
   * @param activity "this"
   */
  public static void goToSettings(FragmentActivity activity) {
    activity.startActivity(new Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.getPackageName(), null)));
  }

}
