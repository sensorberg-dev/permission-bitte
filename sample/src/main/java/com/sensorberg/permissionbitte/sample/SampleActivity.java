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

import com.sensorberg.permissionbitte.Permission;
import com.sensorberg.permissionbitte.PermissionBitte;
import com.sensorberg.permissionbitte.Permissions;

/**
 * Sample showing PermissionBitte.
 */
public class SampleActivity extends AppCompatActivity implements Observer<SampleViewModel.State>, View.OnClickListener {

  private static final String TAG = SampleActivity.class.getSimpleName();

  private SampleViewModel viewModel;
  private AlertDialog alertDialog = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button).setOnClickListener(this);
    viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
    viewModel.getState().observe(this, this);

    PermissionBitte.permissions(this).observe(this, new Observer<Permissions>() {
      @Override
      public void onChanged(Permissions permissions) {
        for (Permission permission : permissions.getPermissionSet()) {
          Log.d(TAG, permission.getName() + " " + permission.getResult());
        }
        Log.d(TAG, "--------------------------------------------------------");

        viewModel.onPermissionChanged(permissions);
      }
    });
  }

  @Override
  public void onClick(View v) {
    viewModel.askForPermission();
  }

  @Override
  public void onChanged(@Nullable SampleViewModel.State state) {
    switch (state) {
      case NEED_ASKING_FOR_PERMISSION:
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        break;

      case SHOW_RATIONALE:
        showRationaleDialog();
        break;

      case ASK_FOR_PERMISSION:
        PermissionBitte.ask(this);
        break;

      case PERMISSION_GRANTED:
        Toast.makeText(this, "Danke schön", Toast.LENGTH_SHORT).show();
        findViewById(R.id.button).setVisibility(View.GONE);
        break;

      case SHOW_SETTINGS:
        Toast.makeText(SampleActivity.this, "We really need those permissions", Toast.LENGTH_SHORT).show();
        PermissionBitte.goToSettings(this);

    }
  }

  private void showRationaleDialog() {
    if (alertDialog != null) {
      alertDialog.dismiss();
      alertDialog = null;
    }

    alertDialog = new AlertDialog.Builder(this)
            .setTitle("Bitte")
            .setMessage("I promise not to be a creep")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                viewModel.askForPermission();
              }
            })
            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                viewModel.rationaleDeclined();
              }
            })
            .setCancelable(false)
            .show();
  }
}
