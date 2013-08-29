package com.notepad.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.notepad.R;
import com.notepad.provider.DbAdapter;
import com.notepad.provider.NotePadAdapter;
import com.notepad.util.NotePadUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NotePadActivity extends Activity {

	private Context mContexto;
	private DbAdapter mDbAdapter;
	private ListView mainGrid;
	private Button mainButtonAddNote;
	private final int MAIN_BUTTON_ADD = 1;
	EditText search;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTES_TITLE = "title";
	public static final String KEY_NOTES_BODY = "body";
	public static final String KEY_NOTES_DATE = "date";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		search = (EditText) findViewById(R.id.search_text);
		mainGrid = (ListView) findViewById(R.id.listview);
		mainButtonAddNote = (Button) findViewById(R.id.mainButtonAddNote);

		mainGrid.setOnItemClickListener(new GridClick());
		mainButtonAddNote.setOnClickListener(new ClickEvents(MAIN_BUTTON_ADD));

		mContexto = getApplicationContext();
		mDbAdapter = new DbAdapter(mContexto);
		mDbAdapter.open(NotePadUtils.OPTION_NOTES_DATABASE);

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence title, int arg1, int arg2,
					int arg3) {
				if (title.toString() != null && title.toString().length() > 2) {
					try {
						Cursor note = mDbAdapter.fetchNoteByTitle(title
								.toString());

						String notePad = note.getString(note
								.getColumnIndex("title"));
						Log.d("note", "" + notePad);
						
						Toast.makeText(NotePadActivity.this, notePad, Toast.LENGTH_SHORT).show();
						
						
						
					}

					catch (SQLiteException e) {
						e.printStackTrace();
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		boolean initialConfiguration = isFirstRun();
		if (initialConfiguration)
			doFirstStartup();
		else
			doNormalStartup();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}

	@Override
	protected void onDestroy() {
		mDbAdapter.close();

		super.onDestroy();
	}

	private boolean isFirstRun() {
		boolean firstRun;
		firstRun = false; // for the moment

		return firstRun;
	}

	private void doFirstStartup() {

		Log.v(NotePadUtils.FIRST_TAG, "Doing the first run dance");
	}

	
	private void doNormalStartup() {
		Log.v(NotePadUtils.NORMAL_TAG, "Doing the normal run dance");
		fillData();
	}

	private void fillData() {
		mainGrid.setAdapter(new NotePadAdapter(this, mDbAdapter
				.fetchAllNotes()));
	}

	@SuppressLint("SimpleDateFormat")
	protected void testAddNotes(boolean addTestNotes) {
		int random = (int) (Math.random() * 5);
		Log.v("NUMERO DE NOTAS A AGREGAR", random + "");
		
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		java.util.Date currentDate = new java.util.Date();
		String date= dateFormat.format(currentDate).toString();
		for (int i = 0; i < random; i++) {
			long id = mDbAdapter.createNote("test" + i, "nota" + i, date);
			Log.v("ADDED", "Note ID: " + id);
		}
	}

	private class GridClick implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try {
				Intent intent = new Intent(mContexto, NotepadViewNote.class);
				intent.putExtra(NotePadUtils.KEY_BEHAVIOUR_NOTE,
						NotePadUtils.VIEW_NOTES);
				intent.putExtra(NotePadUtils.KEY_ROWID_DATABASE, id);
				startActivityForResult(intent, NotePadUtils.VIEW_NOTES);
			} catch (Exception ex) {
				return;
			}
		}
	}

	private class ClickEvents implements OnClickListener {
		private int behaviour;
		private Intent intent;

		public ClickEvents(int behaviour) {
			this.behaviour = behaviour;
		}

		public void onClick(View parent) {
			switch (behaviour) {
			case MAIN_BUTTON_ADD:
				intent = new Intent(mContexto, EditNote.class);
				intent.putExtra(NotePadUtils.KEY_BEHAVIOUR_NOTE,
						NotePadUtils.ADD_NOTE);
				startActivityForResult(intent, NotePadUtils.ADD_NOTE);
				break;

			default:
				Toast.makeText(mContexto, "OOPS", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
