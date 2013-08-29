package com.notepad.activities;

import com.notepad.R;
import com.notepad.provider.DbAdapter;
import com.notepad.util.NotePadUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * NotepadViewNote: Activity destined to view and delete notes
 */
public class NotepadViewNote extends Activity {

	private Context mContexto;
	private DbAdapter mDbAdapter;
	private Long mRowId;

	private TextView noteTitleText;
	private TextView noteBodyText;
	private TextView noteDateText;

	private final static int MENU_EDIT = 1;
	private final static int MENU_SHARE = 2;
	private final static int MENU_DELETE = 3;

	private StringBuilder texto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewnote);
		mContexto = getApplicationContext();
		mDbAdapter = new DbAdapter(mContexto);
		mDbAdapter.open(NotePadUtils.OPTION_NOTES_DATABASE);
		mRowId = getRowId(savedInstanceState);
		initUIElements();
		fillApp();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillApp();
	}

	@Override
	protected void onDestroy() {
		mDbAdapter.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		int magic_number = 0;

		menu.add(magic_number, MENU_EDIT, MENU_EDIT, R.string.noteMenuEdit);
		menu.add(magic_number, MENU_SHARE, MENU_SHARE, R.string.noteMenuShare);
		menu.add(magic_number, MENU_DELETE, MENU_DELETE,
				R.string.noteMenuDelete);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	
	public boolean onOptionsItemSelected(MenuItem item) {
		int MENU_OPTION = item.getItemId();
		Intent intent;
		switch (MENU_OPTION) {

		case MENU_DELETE:
			delete();
			finish();
			return true;
		case MENU_EDIT:
			intent = new Intent(mContexto, EditNote.class);
			intent.putExtra(NotePadUtils.KEY_BEHAVIOUR_NOTE,
					NotePadUtils.EDIT_NOTES);
			intent.putExtra(NotePadUtils.KEY_ROWID_DATABASE, mRowId);
			startActivityForResult(intent, NotePadUtils.EDIT_NOTES);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void initUIElements() {
		noteTitleText = (TextView) findViewById(R.id.noteViewTitleText);
		noteBodyText = (TextView) findViewById(R.id.noteViewBodyText);
		noteDateText = (TextView) findViewById(R.id.note_date);

		// TODO Places and all these things
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(NotePadUtils.KEY_ROWID_DATABASE, mRowId);
		super.onSaveInstanceState(outState);
	}

	
	private void fillApp() {
		Log.v("ROWID", mRowId + "");
		if (mRowId != null) {
			Cursor note = mDbAdapter.fetchNote(mRowId);
			startManagingCursor(note);
			String body = note.getString(note
					.getColumnIndexOrThrow(NotePadUtils.KEY_BODY_DATABASE));
			String title = note.getString(note
					.getColumnIndexOrThrow(NotePadUtils.KEY_TITLE_DATABASE));
			String date = note.getString(note
					.getColumnIndexOrThrow(NotePadUtils.KEY_DATE_DATABASE));

			// Title hack
			String defaultNoteName = "Note";// getString(R.string.defaultNoteName);
			title = (title.equals("")) ? defaultNoteName : title;

			noteTitleText.setText(title);
			noteBodyText.setText(body);
			noteDateText.setText(date);

			// Erase the cursor
			note = null;
		}
	}

	
	private long getRowId(Bundle savedInstanceState) {
		Long rowId;
		Log.v("BUNDLESTATE", (savedInstanceState == null) + "");
		
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			rowId = extras.getLong(NotePadUtils.KEY_ROWID_DATABASE);
		} else
			rowId = (Long) savedInstanceState
					.getSerializable(NotePadUtils.KEY_ROWID_DATABASE);
		return (rowId != null) ? rowId : NotePadUtils.ERROR;
	}

	
	private void delete() {
		mDbAdapter.deleteNote(mRowId);
	}

	OnClickListener buttonClick = new OnClickListener() {
		public void onClick(View v) {
			fillText();
		}
	};

	private void fillText() {
		if (texto == null) {
			texto = new StringBuilder();
			texto.append("-- Prueba --\n"
					+ "Esto no se guardara en la nota\n\n");
		}

		int random, palabras = (int) (Math.random() * 100);
		String[] dic = { "hur", "dur", "durka", "durka", "herp", "derp",
				"nigger", "fagget", "durka", "jose miguel salcido aguilar" };

		for (int i = 0; i < palabras; i++) {
			random = (int) (Math.random() * (dic.length - 1));
			texto.append(dic[random] + " ");
		}

		noteBodyText.setText(texto);
	}
}
