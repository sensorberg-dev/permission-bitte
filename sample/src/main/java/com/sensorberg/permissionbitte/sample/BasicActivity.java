package com.sensorberg.permissionbitte.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sensorberg.permissionbitte.BitteBitte;
import com.sensorberg.permissionbitte.PermissionBitte;

/**
 * Sample of a very basic activity asking for permission.
 * It shows a button to trigger the permission dialog if permission is needed,
 * and hide it when it doesn't
 */
public class BasicActivity extends AppCompatActivity implements BitteBitte {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (PermissionBitte.shouldAsk(this)) {
			findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PermissionBitte.ask(BasicActivity.this, BasicActivity.this);
					findViewById(R.id.button).setVisibility(View.GONE);
				}
			});
		} else {
			findViewById(R.id.button).setVisibility(View.GONE);
		}
	}

	@Override
	public void yesYouCan() {
		Toast.makeText(this, "Danke schön!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void noYouCant() {
		Toast.makeText(this, "NNOOOOOoooooo......", Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void askNicer() {
		new AlertDialog.Builder(this)
				.setTitle("Bitte")
				.setMessage("I promise not to be a creep")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PermissionBitte.ask(BasicActivity.this, BasicActivity.this);
					}
				})
				.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BasicActivity.this.finish();
					}
				})
				.setCancelable(false)
				.show();
	}
}
