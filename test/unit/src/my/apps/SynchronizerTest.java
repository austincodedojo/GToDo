package my.apps;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SynchronizerTest {
    private Synchronizer synchronizer;
    @Mock
    private TaskService taskService;
    @Mock
    private LocalRepository localRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        synchronizer = new Synchronizer(taskService, localRepository);
    }

    @Test
    public void testSynchronizeLists() throws IOException, JSONException {
        JSONArray lists = new JSONArray(
                "[{\"id\":\"06357666721862918015:0:0\",\"child_entity\":[{\"id\":\"06357666721862918015:0:17\"},{\"id\":\"06357666721862918015:0:16\"}],\"last_modified\":1292249276523,\"list_metadata\":{\"1\":1269909761257},\"name\":\"List 1\",\"type\":\"GROUP\"}," +
                 "{\"id\":\"06357666721862918015:4:0\",\"last_modified\":1280271489705,\"list_metadata\":{\"1\":1278866896114},\"name\":\"List 2\",\"type\":\"GROUP\"}," +
                 "{\"id\":\"06357666721862918015:5:0\",\"last_modified\":1289455848096,\"list_metadata\":{\"1\":1278729713191},\"name\":\"List 3\",\"type\":\"GROUP\"}]");
        when(taskService.getLists()).thenReturn(lists);

        synchronizer.synchronizeLists();

        verify(localRepository).insertLists(
                new String[] { Lists.ID, Lists.NAME },
                new String[] {
                    "06357666721862918015:0:0", "List 1",
                    "06357666721862918015:4:0", "List 2",
                    "06357666721862918015:5:0", "List 3"
                });
    }
}
