package com.sensorberg.permissionbitte.sample;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.sensorberg.permissionbitte.PermissionBitte;
import com.sensorberg.permissionbitte.Permission;

/**
 * Sample showing PermissionBitte with Android Architecture Components
 */
public class ArchitectureComponentsActivity extends AppCompatActivity implements Observer<ArchitectureComponentsViewModel.State>, View.OnClickListener {

  private ArchitectureComponentsViewModel vm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.button).setOnClickListener(this);
    vm = ViewModelProviders.of(this).get(ArchitectureComponentsViewModel.class);
    vm.getState().observe(this, this);
    vm.shouldAskPermission(PermissionBitte.shouldAsk(this));

    PermissionBitte.permissionLiveData.observe(this, new Observer<Permission>() {
      @Override
      public void onChanged(Permission permission) {
        // react on the permission state
      }
    });
  }

  @Override
  public void onClick(View v) {
    vm.userAgreesForPermissionAsking();
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
      case SHOW_PERMISSION_RATIONALE:
        new AlertDialog.Builder(this)
                .setTitle("Bitte")
                .setMessage("I promise not to be a creep")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    vm.userAgreesForPermissionAsking();
                  }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    vm.userDeclinedRationale();
                  }
                })
                .setCancelable(false)
                .show();
        break;
      case ON_PERMISSION_DENIED:
        PermissionBitte.goToSettings(this);
      case ON_PERMISSION_RATIONALE_DECLINED:
        Toast.makeText(this, "We really need those permissions", Toast.LENGTH_SHORT).show();
        finish();
        break;
    }
  }
}
