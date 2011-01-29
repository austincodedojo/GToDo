package my.apps;

import android.test.ActivityInstrumentationTestCase2;

public class GToDoActivityTest extends ActivityInstrumentationTestCase2<GToDoActivity> {
    private DatabaseDriver database;
    private ServiceDriver todo;
    private UIDriver ui;

    private String list1;
    private String list2;

    public GToDoActivityTest() {
        super("my.apps", GToDoActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        database = new DatabaseDriver(getActivity());
        todo = new ServiceDriver();
        ui = new UIDriver();

        long first = System.currentTimeMillis();
        list1 = "List " + first;
        list2 = "List " + (first + 1);
    }

    public void testShowsLists() {
        database.startWithCleanSlate();
        todo.addList(list1);
        todo.addList(list2);
        ui.firstShowsNoLists();
        ui.thenShowsLists(list1, list2);
    }
}
