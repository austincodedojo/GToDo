package my.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

public class GToDoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new AuthTokenTask().execute();
    }

    public class AuthTokenTask extends AsyncTask<Void, Void, String> {
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
                new AuthTokenTask().execute();
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
