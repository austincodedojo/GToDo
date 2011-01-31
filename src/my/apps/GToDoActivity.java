package my.apps;

import android.database.Cursor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.IOException;

public class GToDoActivity extends Activity {
    private ListView taskLists;
    private SimpleCursorAdapter taskListsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        taskLists = (ListView) findViewById(R.id.task_lists);
        taskListsAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, new String[] { Lists.NAME }, new int[] { android.R.id.text1 });
        taskLists.setAdapter(taskListsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new TaskListsTask().execute();
    }

    private Controller getController() {
        return ((GToDoApplication) getApplication()).getController();
    }

    public class TaskListsTask extends AsyncTask<Void, Cursor, Cursor> {
        private Intent userInteration;
        private Exception fatalException;

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                CursorResults results = getController().getTaskLists(
                    new String[] { Lists._ID, Lists.NAME }, Lists.NAME + " asc");
                publishProgress(results.getImmediate());
                return results.getDeferred();
            } catch (IOException ex) {
                fatalException = ex;
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Cursor... values) {
            taskListsAdapter.changeCursor(values[0]);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(userInteration != null) {
                GToDoActivity.this.startActivityForResult(userInteration, 0);
                new TaskListsTask().execute();
            }
            else if(fatalException != null) {
                Toast.makeText(GToDoActivity.this, fatalException.getMessage(), Toast.LENGTH_SHORT).show();
                GToDoActivity.this.finish();
            }
            taskListsAdapter.changeCursor(cursor);
        }
    }
}
