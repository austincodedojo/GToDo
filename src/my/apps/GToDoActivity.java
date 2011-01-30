package my.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import javax.sound.midi.ControllerEventListener;

public class GToDoActivity extends Activity {
    private ListView taskLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        taskLists = (ListView) findViewById(R.id.task_lists);
        taskLists.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, new String[] { Lists.NAME }, new int[] { android.R.id.text1 }));
    }

    @Override
    protected void onResume() {
        super.onResume();

        new TaskListsTask().execute();
    }

    private Controller getController() {
        return ((GToDoApplication) getApplication()).getController();
    }

    public class TaskListsTask extends AsyncTask<Void, Void, String> {
        private Intent userInteration;
        private Exception fatalException;

        @Override
        protected String doInBackground(Void... voids) {
            AuthTokenProvider authTokenProvider = new AuthTokenProvider(GToDoActivity.this);
            try {
                return authTokenProvider.getToken();
            }
            catch (UserInteractionRequiredException ex) {
                userInteration = ex.getUserInteraction();
            }
            catch (Exception ex) {
                fatalException = ex;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String token) {
            if(userInteration != null) {
                GToDoActivity.this.startActivityForResult(userInteration, 0);
                new TaskListsTask().execute();
            }
            else if(fatalException != null) {
                Toast.makeText(GToDoActivity.this, fatalException.getMessage(), Toast.LENGTH_SHORT).show();
                GToDoActivity.this.finish();
            }
            else {
                Toast.makeText(GToDoActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
