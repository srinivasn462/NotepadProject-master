package com.notepad.activities;

import com.notepad.R;
import com.notepad.util.LoginDbAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends Activity {
	Button btnSignIn, btnSignUp;
	LoginDbAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		loginDataBaseAdapter = new LoginDbAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		btnSignIn = (Button) findViewById(R.id.buttonSignIN);
		btnSignUp = (Button) findViewById(R.id.buttonSignUP);

		btnSignUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentSignUP = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(intentSignUP);
			}
		});
	}

	public void signIn(View V) {
		final Dialog dialog = new Dialog(HomeActivity.this);
		dialog.setContentView(R.layout.login);
		dialog.setTitle("Login");

		final EditText editTextUserName = (EditText) dialog
				.findViewById(R.id.editTextUserNameToLogin);
		final EditText editTextPassword = (EditText) dialog
				.findViewById(R.id.editTextPasswordToLogin);

		Button btnSignIn = (Button) dialog.findViewById(R.id.buttonSignIn);

		btnSignIn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String userName = editTextUserName.getText().toString();
				String password = editTextPassword.getText().toString();

				String storedPassword = loginDataBaseAdapter
						.getSinlgeEntry(userName);

				if (password.equals(storedPassword)) {

					dialog.dismiss();
					Intent intent = new Intent(getApplicationContext(),
							NotePadActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(HomeActivity.this,
							"User Name or Password does not match",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		dialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		loginDataBaseAdapter.close();
	}
}
