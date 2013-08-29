package com.notepad.util;

import com.notepad.provider.DbAdapter;

public class NotePadUtils {

	// GENERAL
	public static int ERROR = -1;
	public static final int MAX_CHARACTERS_VERTICAL = 15;
	public static final int MAX_CHARACTERS_HORIZONTAL = 25;
	public static final String FIRST_TAG = "[FIRST]";
	public static final String NORMAL_TAG = "[NORMAL]";
	public static final String DEBUG_TAG = "[DEBUG]";

	// DATABASE
	public final static String KEY_ROWID_DATABASE = DbAdapter.KEY_ROWID;
	public final static String KEY_TITLE_DATABASE = DbAdapter.KEY_NOTES_TITLE;
	public final static String KEY_BODY_DATABASE = DbAdapter.KEY_NOTES_BODY;
	public final static String KEY_DATE_DATABASE = DbAdapter.KEY_NOTES_DATE;
	public final static int OPTION_NOTES_DATABASE = DbAdapter.OPTION_NOTES;

	// EDIT / VIEW NOTES
	public final static String KEY_BEHAVIOUR_NOTE = "behaviour";

	public final static int ERROR_NOTES = 0;
	public final static int EDIT_NOTES = 1;
	public final static int VIEW_NOTES = 2;
	public final static int DELETE_NOTES = 3;
	public final static int ADD_NOTE = 4;
}
