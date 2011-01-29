package my.apps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskService {
    protected static final String SERVICE_URL = "https://mail.google.com/tasks/ig";
    protected static final String REQUEST_URL = "https://mail.google.com/tasks/r/ig";
    protected static final Pattern INITIAL_STATE_PATTERN = Pattern.compile("_setup\\((.*)\\)");

    private HttpService httpService;
    private AuthTokenProvider authTokenProvider;

    public TaskService(HttpService httpService, AuthTokenProvider authTokenProvider) {
        this.httpService = httpService;
        this.authTokenProvider = authTokenProvider;
    }

    public String addList(String listName) throws IOException {
        String initialResponse = httpService.get(HttpParameters.with(SERVICE_URL)
              .cookie("GTL", authTokenProvider.getToken()));
        Matcher m = INITIAL_STATE_PATTERN.matcher(initialResponse);
        if(m.find()) {
            try {
                JSONObject initialState = new JSONObject(m.group(1));
                JSONArray lists = initialState.getJSONObject("t").getJSONArray("lists");

                JSONObject entityDelta = new JSONObject();
                entityDelta.put("name", listName);
                entityDelta.put("entity_type", "GROUP");

                JSONObject action = new JSONObject();
                action.put("action_type", "create");
                action.put("action_id", Integer.toString(1));
                action.put("index", lists.length());
                action.put("entity_delta", entityDelta);

                JSONObject request = new JSONObject();
                request.put("action_list", new JSONArray(Arrays.asList(action)));
                request.put("client_version", initialState.getLong("v"));
                JSONObject response = new JSONObject(httpService.post(HttpParameters.with(REQUEST_URL)
                        .formParameter("r", request.toString())
                        .header("AT", "1")
                        .cookie("GTL", authTokenProvider.getToken())));
                return response.getJSONArray("results").getJSONObject(0).getString("new_id");
            } catch (JSONException ex) {
                throw new IOException("Unrecognized response", ex);
            }
        }
        throw new IOException("Unrecognized response", new IllegalArgumentException(initialResponse));
    }
}
