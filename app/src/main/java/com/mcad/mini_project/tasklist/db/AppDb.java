package com.mcad.mini_project.tasklist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AppDb extends SQLiteOpenHelper {
	private static final String TAG = "AppDb";

	public static final String DB_NAME               = "TaskTList.db";
	public static final int    DB_VERSION__TASKS     = 1;
	public static final int    DB_VERSION            = DB_VERSION__TASKS;

	private static AppDb instance = null;

	private AppDb(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * Get an instance of the app's singleton db helper objection
	 *
	 * @param context the content provider's context
	 * @return a SQLite db helper object
	 */
	static AppDb getInstance(Context context) {
		if(instance == null) {
			Context appC = null;
			try {
				appC = context.getApplicationContext();
			} catch(Exception ignored) {
			}
			if(appC != null) context = appC;
			instance = new AppDb(context);
		}
		return instance;
	}

	@Override public void onCreate(SQLiteDatabase db) {
		Tasks.create(db);
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion) {
		default:
			break;
		}
	}
}
