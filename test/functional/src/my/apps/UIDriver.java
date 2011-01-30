package my.apps;

import android.app.Activity;
import android.database.DataSetObserver;
import android.database.Cursor;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class UIDriver {
    private Activity activity;
    private LinkedBlockingQueue<List<String>> observedListNames;
    private ListView taskLists;

    public UIDriver(Activity activity) {
        this.activity = activity;
        taskLists = (ListView) activity.findViewById(R.id.task_lists);

        observedListNames = new LinkedBlockingQueue<List<String>>();
        taskLists.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                int n = taskLists.getAdapter().getCount();
                List<String> currentListNames = new ArrayList<String>(n);
                for(int i = 0; i < n; i++) {
                    Cursor c = (Cursor) taskLists.getAdapter().getItem(i);
                    String listName = c.getString(c.getColumnIndex(Lists.NAME));
                    currentListNames.add(listName);
                }
                observedListNames.offer(currentListNames);
            }
        });
    }

    public void firstShowsNoLists() throws InterruptedException {
        List<String> actualLists = observedListNames.poll(2, TimeUnit.SECONDS);

        assertNotNull(actualLists);
        assertEquals(Collections.emptyList(), actualLists);
    }

    public void thenShowsLists(String... expectedLists) throws InterruptedException {
        List<String> actualLists = observedListNames.poll(2, TimeUnit.SECONDS);
        assertTrue(actualLists.containsAll(Arrays.asList(expectedLists)));
    }
}
