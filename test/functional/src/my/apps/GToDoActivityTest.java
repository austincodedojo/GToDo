package my.apps;

import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;

public class GToDoActivityTest extends ActivityInstrumentationTestCase2<GToDoActivity> {
    private DatabaseDriver database;
    private ServiceDriver todo;
    private UIDriver ui;

    private String list1;
    private String list2;
    private String list3;
    private String list4;

    public GToDoActivityTest() {
        super("my.apps", GToDoActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        database = new DatabaseDriver(getActivity());
        todo = new ServiceDriver(getActivity());
        ui = new UIDriver(getActivity());

        long first = System.currentTimeMillis();
        list1 = "List " + first;
        list2 = "List " + (first + 1);
        list3 = "List " + (first + 2);
        list4 = "List " + (first + 3);
    }

    public void testShowsLists() throws IOException, InterruptedException {
        database.startWithCleanSlate();
        database.addList(list1);
        database.addList(list2);
        todo.addList(list3);
        todo.addList(list4);
        ui.showsLists(list1, list2);
        ui.showsLists(list1, list2, list3, list4);
    }
}
