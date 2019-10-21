package com.sensorberg.permissionbitte.sample;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sensorberg.permissionbitte.PermissionBitte;
import com.sensorberg.permissionbitte.Permission;
import com.sensorberg.permissionbitte.PermissionResult;
import com.sensorberg.permissionbitte.Permissions;

import java.util.List;

/**
 * Sample showing PermissionBitte with Android Architecture Components
 */
public class ArchitectureComponentsActivity extends AppCompatActivity implements Observer<ArchitectureComponentsViewModel.State>, View.OnClickListener {

  private static final String TAG = ArchitectureComponentsActivity.class.getSimpleName();

  private ArchitectureComponentsViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button).setOnClickListener(this);
    viewModel = ViewModelProviders.of(this).get(ArchitectureComponentsViewModel.class);
    viewModel.getState().observe(this, this);

    PermissionBitte.permissions(this).observe(this, new Observer<Permissions>() {
      @Override
      public void onChanged(Permissions permissions) {
        // react on the permission state
        for (Permission permission : permissions.getPermissions()) {
          Log.d(TAG, permission.name + " " + permission.result);
        }
        Log.d(TAG, "--------------------------------------------------------");

        if (permissions.deniedPermanently()) {
          List<Permission> deniedPermissionList = permissions.get(PermissionResult.DENIED);

          // check if you really need that permission or if you can live without.

          // In case you can not live without that permission
          Toast.makeText(ArchitectureComponentsActivity.this, "We really need those permissions", Toast.LENGTH_SHORT).show();
          finish();
        } else if (permissions.showRationale()) {
          // show a dialog explaining why you need that permission
          showRationaleDialog();
        }
      }
    });
  }

  @Override
  public void onClick(View v) {
    viewModel.userAgreesForPermissionAsking();
  }

  @Override
  public void onChanged(@Nullable ArchitectureComponentsViewModel.State state) {
    switch (state) {
      case PERMISSION_GOOD:
        Toast.makeText(this, "Danke schön", Toast.LENGTH_SHORT).show();
        findViewById(R.id.button).setVisibility(View.GONE);
        break;
      case SHOW_PERMISSION_BUTTON:
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        break;
      case ASK_FOR_PERMISSION:
        PermissionBitte.ask(this);
        break;
      case ON_PERMISSION_DENIED:
        PermissionBitte.goToSettings(this);
      case ON_PERMISSION_RATIONALE_DECLINED:
        Toast.makeText(this, "We really need those permissions", Toast.LENGTH_SHORT).show();
        finish();
        break;
    }
  }

  private void showRationaleDialog() {
    new AlertDialog.Builder(this)
            .setTitle("Bitte")
            .setMessage("I promise not to be a creep")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                viewModel.userAgreesForPermissionAsking();
              }
            })
            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                viewModel.userDeclinedRationale();
              }
            })
            .setCancelable(false)
            .show();
  }
}
