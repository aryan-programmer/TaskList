package com.mcad.mini_project.tasklist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import androidx.annotation.Nullable;

public class AppDb extends SQLiteOpenHelper {
	private static final String TAG = "AppDb";

	public static final String DB_NAME           = "TaskTList.db";
	public static final int    DB_VERSION__TASKS = 1;
	public static final int    DB_VERSION        = DB_VERSION__TASKS;

	private static AppDb instance = null;

	private AppDb(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static AppDb getInstance(Context context) {
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
	}

	@Nullable
	public Cursor queryTasks(@Nullable Long id, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Tasks.Table);
		if(id != null) qb.appendWhere(Tasks._id + " = " + id);
		SQLiteDatabase db = getReadableDatabase();
		return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	}

	public long insertTask(@Nullable ContentValues values) {
		SQLiteDatabase db       = getWritableDatabase();
		long           recordId = db.insert(Tasks.Table, null, values);
		if(recordId >= 0) return recordId;
		else throw new SQLException("Failed to insert into tasks table");
	}

	public int deleteTasks(@Nullable Long id, @Nullable String selection, @Nullable String[] selectionArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int            count;
		if(id == null) count = db.delete(Tasks.Table, selection, selectionArgs);
		else {
			String selectionCriteria = Tasks._id + " = " + id;
			if(selection != null && selection.length() > 0) {
				selectionCriteria += " AND (" + selection + ")";
			}
			count = db.delete(Tasks.Table, selectionCriteria, selectionArgs);
		}
		return count;
	}

	public int updateTasks(@Nullable Long id, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int            count;
		if(id == null) count = db.delete(Tasks.Table, selection, selectionArgs);
		else {
			String selectionCriteria = Tasks._id + " = " + id;
			if(selection != null && selection.length() > 0) {
				selectionCriteria += " AND (" + selection + ")";
			}
			count = db.update(Tasks.Table, values, selectionCriteria, selectionArgs);
		}
		return count;
	}
}
