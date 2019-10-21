package com.sensorberg.permissionbitte.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.sensorberg.permissionbitte.PermissionBitte;

/**
 * Sample of a very basic activity asking for permission.
 * It shows a button to trigger the permission dialog if permission is needed,
 * and hide it when it doesn't
 */
public class BasicActivity extends AppCompatActivity {

  private PermissionBitte permissionBitte = new PermissionBitte();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    if (permissionBitte.shouldAsk(this)) {
//      findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          permissionBitte.ask(BasicActivity.this);
//          findViewById(R.id.button).setVisibility(View.GONE);
//        }
//      });
//    } else {
//      findViewById(R.id.button).setVisibility(View.GONE);
//    }
  }
}
