package com.sensorberg.permissionbitte.sample;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sensorberg.permissionbitte.Permission;
import com.sensorberg.permissionbitte.PermissionResult;
import com.sensorberg.permissionbitte.Permissions;

import java.util.Set;

public class SampleViewModel extends ViewModel {

  private final MutableLiveData<State> state = new MutableLiveData<>();

  LiveData<State> getState() {
    return state;
  }

  void askForPermission() {
    state.setValue(State.ASK_FOR_PERMISSION);
  }

  void rationaleDeclined() {
    state.setValue(State.SHOW_SETTINGS);
  }

  void onPermissionChanged(Permissions permissions) {
    if (permissions.deniedPermanently()) {
      Set<Permission> deniedPermissions = permissions.filter(PermissionResult.DENIED);
      // check if you really need that permission or if you can live without.

      // In case you can not live without that permission
      state.setValue(State.SHOW_SETTINGS);
    } else if (permissions.showRationale()) {
      Set<Permission> rationalePermissions = permissions.filter(PermissionResult.SHOW_RATIONALE);
      // check if you really need that permission or if you can live without.

      // show a dialog explaining why you need that permission
      state.setValue(State.SHOW_RATIONALE);
    } else if (permissions.needAskingForPermission()) {
      state.setValue(State.NEED_ASKING_FOR_PERMISSION);
    } else if (permissions.allGranted()) {
      state.setValue(State.PERMISSION_GRANTED);
    }
  }

  enum State {
    PERMISSION_GRANTED,
    ASK_FOR_PERMISSION,
    NEED_ASKING_FOR_PERMISSION,
    SHOW_RATIONALE,
    SHOW_SETTINGS,
  }
}
