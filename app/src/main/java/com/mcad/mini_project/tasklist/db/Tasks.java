package com.mcad.mini_project.tasklist.db;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class Tasks {
	public static final String Table       = "Tasks";
	public static final String _id         = BaseColumns._ID;
	public static final String Name        = "Name";
	public static final String Description = "Description";
	public static final String SortOrder   = "SortOrder";

	private static final String CREATE_TABLE =
		"CREATE TABLE " + Table + " (" +
			/**/_id /*        */ + " INTEGER PRIMARY KEY NOT NULL, " +
			/**/Name /*       */ + " TEXT " + /*     */ "NOT NULL, " +
			/**/Description /**/ + " TEXT, " +
			/**/SortOrder /*  */ + " INTEGER" +
		");";

	static void create(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	private Tasks() {
	}
}
