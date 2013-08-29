package com.notepad.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.notepad.R;
import com.notepad.provider.DbAdapter;
import com.notepad.util.NotePadUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNote extends Activity {

	private Context mContexto;
	private DbAdapter mDbAdapter;
	private Long mRowId;
	private int mBehaviour;
	private EditText noteTitleText;
	private EditText noteBodyText;
	private TextView noteDate;
	private Button saveNote;
	private final static int MENU_SAVE = 0;
	private final static int MENU_CANCEL = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnote);
		mContexto = getApplicationContext();
		mDbAdapter = new DbAdapter(mContexto);
		mDbAdapter.open(NotePadUtils.OPTION_NOTES_DATABASE);
		mBehaviour = setBehaviour(savedInstanceState);
		mRowId = getRowId(savedInstanceState);
		initUIElements();
		fillApp();
	}

	@SuppressLint("SimpleDateFormat")
	private void initUIElements() {
		noteTitleText = (EditText) findViewById(R.id.noteTitleText);
		noteBodyText = (EditText) findViewById(R.id.noteBodyText);
		noteDate = (TextView)findViewById(R.id.note_date);
		saveNote = (Button) findViewById(R.id.save_note);
		saveNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				save();
			}
		});
		noteDate = (TextView) findViewById(R.id.node_date);
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		java.util.Date date = new java.util.Date();
		noteDate.setText(dateFormat.format(date).toString());
		System.out.println("Current Date : " + dateFormat.format(date));
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return mRowId;
	}

	@SuppressLint("SimpleDateFormat")
	private void fillApp() {
		if (mRowId != null && mBehaviour == NotePadUtils.EDIT_NOTES) {
			Cursor note = mDbAdapter.fetchNote(mRowId);
			startManagingCursor(note);
			DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
			java.util.Date currentDatedate = new java.util.Date();
		String date=	dateFormat.format(currentDatedate).toString();

			// TODO Places
			String title = note.getString(note
					.getColumnIndexOrThrow(NotePadUtils.KEY_TITLE_DATABASE));
			String body = note.getString(note
					.getColumnIndexOrThrow(NotePadUtils.KEY_BODY_DATABASE));
			noteTitleText.setText(title);
			noteBodyText.setText(body);
			noteDate.setText(date);

			note = null;
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		mDbAdapter.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int magic_number = 0;
		menu.add(magic_number, MENU_SAVE, MENU_SAVE, R.string.noteMenuSave);
		menu.add(magic_number, MENU_CANCEL, MENU_CANCEL,
				R.string.noteMenuCancel);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int MENU_OPTION = item.getItemId();
		switch (MENU_OPTION) {
		case MENU_SAVE:
			return true;
		case MENU_CANCEL:
			finish();
			return true;
		default:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private int setBehaviour(Bundle savedInstanceState) {
		Integer behaviour;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			behaviour = extras.getInt(NotePadUtils.KEY_BEHAVIOUR_NOTE);
		} else {
			behaviour = (Integer) savedInstanceState
					.getSerializable(NotePadUtils.KEY_BEHAVIOUR_NOTE);
		}
		return (behaviour != null) ? behaviour : NotePadUtils.ERROR_NOTES;
	}

	private long getRowId(Bundle savedInstanceState) {
		Long rowId;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			rowId = extras.getLong(NotePadUtils.KEY_ROWID_DATABASE);
		} else
			rowId = (Long) savedInstanceState
					.getSerializable(NotePadUtils.KEY_ROWID_DATABASE);
		return (rowId != null) ? rowId : NotePadUtils.ERROR;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(NotePadUtils.KEY_ROWID_DATABASE, mRowId);
		outState.putInt(NotePadUtils.KEY_BEHAVIOUR_NOTE, mBehaviour);
		super.onSaveInstanceState(outState);
	}

	@SuppressLint("SimpleDateFormat")
	public void save() {
		String body = noteBodyText.getText().toString();
		String title = noteTitleText.getText().toString();
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		java.util.Date currentDate = new java.util.Date();
		String date = dateFormat.format(currentDate).toString();

		Toast msg = Toast.makeText(mContexto, R.string.emptyNote,
				Toast.LENGTH_SHORT);

		if (body.equals("") && title.equals("")) {
			msg.show();
			return;
		} else if (body.equals("")) {
			msg.show();
			return;
		}
		
		if (mBehaviour == NotePadUtils.ADD_NOTE) {

			long id = mDbAdapter.createNote(title, body, date);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbAdapter.updateNote(mRowId, title, body, date);
		}

		finish();
	}

}
