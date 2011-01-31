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
    private String list1Id;
    private String list2Id;

    public GToDoActivityTest() {
        super("my.apps", GToDoActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        long first = System.currentTimeMillis();
        list1 = "List " + first;
        list2 = "List " + (first + 1);
        list3 = "List " + (first + 2);
        list4 = "List " + (first + 3);

        list1Id = Long.toString(first);
        list2Id = Long.toString(first + 1);

        todo = new ServiceDriver(getInstrumentation().getContext());
        todo.addLists(list3, list4);

        database = new DatabaseDriver(getActivity());
        ui = new UIDriver(getActivity());
    }

    public void testShowsLists() throws IOException, InterruptedException {
        database.startWithCleanSlate();
        database.addList(list1Id, list1);
        database.addList(list2Id, list2);
        ui.showsLists(list1, list2);
        ui.showsLists(list1, list2, list3, list4);
    }
}
