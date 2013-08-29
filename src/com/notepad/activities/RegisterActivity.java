package com.notepad.activities;

import com.notepad.R;
import com.notepad.util.LoginDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText editTextUserName, editTextPassword, editTextConfirmPassword;
	Button btnCreateAccount;

	LoginDbAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		// get Instance of Database Adapter
		loginDataBaseAdapter = new LoginDbAdapter(this);
		loginDataBaseAdapter = loginDataBaseAdapter.open();

		// Get Refferences of Views
		editTextUserName = (EditText) findViewById(R.id.editTextUserName);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

		btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String userName = editTextUserName.getText().toString();
				String password = editTextPassword.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText()
						.toString();
				if (userName.equals("") || password.equals("")
						|| confirmPassword.equals("")) {
					Toast.makeText(getApplicationContext(), "Field empty",
							Toast.LENGTH_LONG).show();
					return;
				}
				// check if both password matches
				if (!password.equals(confirmPassword)) {
					Toast.makeText(getApplicationContext(),
							"Password does not match", Toast.LENGTH_LONG)
							.show();
					return;
				} else {
					// Save the Data in Database
					loginDataBaseAdapter.insertEntry(userName, password);
					
					Toast.makeText(getApplicationContext(), "Account Created Successfully... please login",
							Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		loginDataBaseAdapter.close();
	}
}
