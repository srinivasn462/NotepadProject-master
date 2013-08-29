package com.notepad.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NOTES_TITLE = "title";
	public static final String KEY_NOTES_BODY = "body";
	public static final String KEY_NOTES_DATE = "date";

	public static final String KEY_PLACES_NAME = "name";
	public static final String KEY_PLACES_LATITUDE = "latitude";
	public static final String KEY_PLACES_LONGITUDE = "longitude";

	public static final int OPTION_NOTES = 1;
	public static final int OPTION_PLACES = 2;

	private static final String TAG = "NotepasGPSDB";

	private DatabaseHelper mHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NOTES_CREATE = ""
			+ "CREATE TABLE notes(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "title TEXT NOT NULL," + "body TEXT NOT NULL,"
			+ "date INTEGER NOT NULL);";

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_NOTES_TABLE = "notes";
	private static final int DATABASE_VERSION = 2;

	private final Context mContexto;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private String create;
		private String table;

		public DatabaseHelper(Context contexto, String create, String table) {
			super(contexto, DATABASE_NAME, null, DATABASE_VERSION);
			this.create = create;
			this.table = table;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(create);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + table);
			onCreate(db);
		}
	}

	public DbAdapter(Context contexto) {
		this.mContexto = contexto;
	}

	public DbAdapter open(int option) throws SQLException {
		switch (option) {
		case OPTION_NOTES:
			mHelper = new DatabaseHelper(mContexto, DATABASE_NOTES_CREATE,
					DATABASE_NOTES_TABLE);
			break;

		default:
			break;
		}
		mDb = mHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mHelper.close();
	}

	public long createNote(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTES_TITLE, title);
		initialValues.put(KEY_NOTES_BODY, body);
		initialValues.put(KEY_NOTES_DATE, 0);

		return mDb.insert(DATABASE_NOTES_TABLE, null, initialValues);
	}

	public long createNote(String title, String body, String date) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTES_TITLE, title);
		initialValues.put(KEY_NOTES_BODY, body);
		initialValues.put(KEY_NOTES_DATE, date);
		return mDb.insert(DATABASE_NOTES_TABLE, null, initialValues);
	}

	public boolean deleteNote(long rowId) {
		return mDb.delete(DATABASE_NOTES_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAllNotes() {
		return mDb.query(DATABASE_NOTES_TABLE, new String[] { KEY_ROWID,
				KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_DATE }, null, null,
				null, null, null);
	}

	public Cursor fetchAllNotesById() {
		return mDb.query(DATABASE_NOTES_TABLE, new String[] { KEY_ROWID },
				null, null, null, null, null);
	}

	public Cursor fetchNote(long rowId) {
		Cursor mCursor = mDb.query(true, DATABASE_NOTES_TABLE, new String[] {
				KEY_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_DATE },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	public Cursor fetchNoteByTitle(String title) {
		Cursor mCursor = mDb.query(true, DATABASE_NOTES_TABLE, new String[] {
				KEY_ROWID, KEY_NOTES_TITLE, KEY_NOTES_BODY, KEY_NOTES_DATE },
				KEY_NOTES_TITLE + " LIKE ?",
				new String[] { "%" + title + "%" }, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	public boolean updateNote(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTES_TITLE, title);
		args.put(KEY_NOTES_BODY, body);
		return mDb.update(DATABASE_NOTES_TABLE, args, KEY_ROWID + "=" + rowId,
				null) > 0;
	}

	public boolean updateNote(long rowId, String title, String body, String date) {
		ContentValues args = new ContentValues();
		args.put(KEY_NOTES_TITLE, title);
		args.put(KEY_NOTES_BODY, body);
		args.put(KEY_NOTES_DATE, date);
		return mDb.update(DATABASE_NOTES_TABLE, args, KEY_ROWID + "=" + rowId,
				null) > 0;
	}

}
