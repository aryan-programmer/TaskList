package com.mcad.mini_project.tasklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mcad.mini_project.tasklist.db.AppDb;
import com.mcad.mini_project.tasklist.db.Tasks;
import java.security.InvalidParameterException;

public class FragmentMain
	extends Fragment
	implements LoaderManager.LoaderCallbacks<Cursor>,
	           TaskCursorRVAdapter.TaskClickListener {
	private static final String TAG = "FragmentMain";

	private static final int LOADER_ID = 119;

	private TaskCursorRVAdapter                   adapter;
	private TaskCursorRVAdapter.TaskClickListener adapterClickListener;

	// region ...Lifecycle
	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Activity     activity     = getActivity();
		View         view         = inflater.inflate(R.layout.fragment_main, container, false);
		RecyclerView recyclerView = view.findViewById(R.id.task_list);
		recyclerView.setLayoutManager(new LinearLayoutManager(activity));
		if(activity instanceof TaskCursorRVAdapter.TaskClickListener) adapterClickListener = (TaskCursorRVAdapter.TaskClickListener) activity;
		if(adapter == null) adapter = new TaskCursorRVAdapter(null, this);
		else adapter.setListener(this);
		recyclerView.setAdapter(adapter);
		return view;
	}

	@Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
	}
	// endregion Lifecycle

	void restartLoader() {
		getLoaderManager().restartLoader(LOADER_ID, null, this);
	}

	// region ...Listener interface implementations
	@NonNull @Override public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
		if(id != LOADER_ID) throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id " + id);
		String[] proj      = {Tasks._id, Tasks.Name, Tasks.Description, Tasks.SortOrder};
		String   sortOrder = Tasks.SortOrder + "," + Tasks.Name + " COLLATE NOCASE";
		return new CustomCursorLoader(requireContext(), proj, sortOrder);
	}

	static class CustomCursorLoader extends AsyncTaskLoader<Cursor> {
		String[] proj;
		String   sortOrder;

		public CustomCursorLoader(@NonNull Context context, String[] proj, String sortOrder) {
			super(context);
			this.proj      = proj;
			this.sortOrder = sortOrder;
			onContentChanged();
		}

		@Override public Cursor loadInBackground() {
			return AppDb.getInstance(getContext()).queryTasks(null, proj, null, null, sortOrder);
		}

		@Override protected void onStartLoading() {
			if(takeContentChanged()) forceLoad();
		}

		@Override protected void onStopLoading() {
			cancelLoad();
		}
	}

	@SuppressLint("Range") @Override
	public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override public void onTaskLongClick(@NonNull Task task) {
		if(adapterClickListener != null) adapterClickListener.onTaskLongClick(task);
	}

	@Override public void onTaskEditClick(@NonNull Task task) {
		if(adapterClickListener != null) adapterClickListener.onTaskEditClick(task);
	}

	@Override public void onTaskDeleteClick(@NonNull Task task) {
		if(adapterClickListener != null) adapterClickListener.onTaskDeleteClick(task);
	}
	// endregion Listener interface implementations
}