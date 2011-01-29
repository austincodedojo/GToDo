package my.apps;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static my.apps.TaskService.REQUEST_URL;
import static my.apps.TaskService.SERVICE_URL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

public class TaskServiceTest {
    private TaskService taskService;

    @Mock
    private HttpService httpService;
    @Mock
    private AuthTokenProvider authTokenProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        taskService = new TaskService(httpService, authTokenProvider);
    }

    @Test
    public void testAddFromInitialState() throws IOException {
        String listName = "Test 1";
        String expectedToken = "token";
        when(authTokenProvider.getToken()).thenReturn(expectedToken);

        when(httpService.get(argThat(hasParameters(HttpParameters.with(SERVICE_URL)
                .cookie("GTL", expectedToken)))))
                .thenReturn(
                        "<meta http-equiv=\"X-UA-Compatible\" content=\"chrome=1\">\n" +
                        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                        "<title>Tasks</title>\n" +
                        "<link rel=\"icon\" href=\"res/favicon.png\">\n" +
                        "<link rel=\"shortcut icon\" href=\"res/favicon.ico\">\n" +
                        "<style type=\"text/css\">\n" +
                        "      html{overflow:auto}\n" +
                        "      </style>\n" +
                        "\n" +
                        "<script type=\"text/javascript\" src=\"res/555272492-gadget.js\"></script>\n" +
                        "\n" +
                        "<script>function _init(){_setup({" +
                        "\"f\":{\"v\":true,\"d\":true,\"e\":true,\"r\":true,\"x\":true}," +
                        "\"v\":12959848," +
                        "\"bp\":\"/tasks\"," +
                        "\"t\":{\"latest_sync_point\":1296325412178000,\"response_time\":1296326883,\"results\":[]," +
                        "\"tasks\":[{\"id\":\"06357666721862918015:0:17\",\"last_modified\":1292249276523,\"name\":\"\",\"type\":\"TASK\",\"deleted\":false,\"list_id\":[\"06357666721862918015:0:0\"],\"completed\":false},{\"id\":\"06357666721862918015:0:16\",\"last_modified\":1292249029134,\"name\":\"\",\"type\":\"TASK\",\"deleted\":false,\"list_id\":[\"06357666721862918015:0:0\"],\"completed\":false}]," +
                        "\"user\":{\"id\":\"06357666721862918015\",\"show_tips\":false,\"auto_clear\":false,\"mobile_default_list_id\":\"06357666721862918015:0:0\",\"requested_list_id\":\"06357666721862918015:0:0\",\"default_list_id\":\"06357666721862918015:0:0\"}," +
                        "\"groups\":[]," +
                        "\"lists\":[" +
                        "{\"id\":\"06357666721862918015:0:0\",\"child_entity\":[{\"id\":\"06357666721862918015:0:17\"},{\"id\":\"06357666721862918015:0:16\"}],\"last_modified\":1292249276523,\"list_metadata\":{\"1\":1269909761257},\"name\":\"List 1\",\"type\":\"GROUP\"}," +
                        "{\"id\":\"06357666721862918015:4:0\",\"last_modified\":1280271489705,\"list_metadata\":{\"1\":1278866896114},\"name\":\"List 2\",\"type\":\"GROUP\"}," +
                        "{\"id\":\"06357666721862918015:5:0\",\"last_modified\":1289455848096,\"list_metadata\":{\"1\":1278729713191},\"name\":\"List 3\",\"type\":\"GROUP\"}]}," +
                        "\"e\":\"foo@bar.com\",\"s\":\"res/3251958030-gadget.css\",\"i\":false})}</script></head>\n" +
                        "<body onload=\"_init()\" style=\"margin:0\"><div></div></body></html>");

        String expectedId = "06357666721862918015:28:0";
        when(httpService.post(argThat(hasParameters(HttpParameters.with(REQUEST_URL)
                .formParameter("r", "{\"action_list\":[" +
                        "{\"action_id\":\"1\",\"index\":3,\"action_type\":\"create\"," +
                        "\"entity_delta\":{\"name\":\"" + listName + "\",\"entity_type\":\"GROUP\"}}]," +
                        "\"client_version\":12959848}")
                .header("AT", "1")
                .cookie("GTL", expectedToken)))))
                .thenReturn("{\"response_time\":1296325411,\"results\":[" +
                        "{\"result_type\":\"new_entity\",\"action_id\":1,\"new_id\":\"" + expectedId + "\"}]}");

        String actualId = taskService.addList(listName);

        assertEquals(expectedId, actualId);
    }

    private Matcher<HttpParameters> hasParameters(final HttpParameters expectedParameters) {
        return new ArgumentMatcher<HttpParameters>() {
            @Override
            public boolean matches(Object o) {
                HttpParameters actualParameters = (HttpParameters) o;
                assertEquals(expectedParameters.url, actualParameters.url);
                assertEquals(expectedParameters.formParameters, actualParameters.formParameters);
                assertEquals(expectedParameters.headers, actualParameters.headers);
                assertEquals(expectedParameters.cookies, actualParameters.cookies);
                return true;
            }
        };
    }
}
