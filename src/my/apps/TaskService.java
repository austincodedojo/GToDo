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
    private long clientVersion;
    private int actionId;

    public TaskService(HttpService httpService, AuthTokenProvider authTokenProvider) {
        this.httpService = httpService;
        this.authTokenProvider = authTokenProvider;
    }

    public String addList(String listName) throws IOException {
        try {
            int insertIndex = getLists().length();
            JSONObject action = createAddListAction(listName, insertIndex);
            JSONObject request = createRequest(action);
            JSONObject response = executeRequest(request);
            return getNewId(response);
        } catch (JSONException ex) {
            throw new IOException("Unrecognized response", ex);
        }
    }

    private JSONArray getLists() throws IOException {
        JSONArray lists;
        String initialResponse = httpService.get(HttpParameters.with(SERVICE_URL)
              .cookie("GTL", authTokenProvider.getToken()));
        Matcher m = INITIAL_STATE_PATTERN.matcher(initialResponse);
        if(m.find()) {
            try {
                JSONObject initialState = new JSONObject(m.group(1));
                clientVersion = initialState.getLong("v");
                actionId = 1;
                lists = initialState.getJSONObject("t").getJSONArray("lists");
            } catch (JSONException ex) {
                throw new IOException("Unrecognized response", ex);
            }
        }
        else {
            throw new IOException("Unrecognized response", new IllegalArgumentException(initialResponse));
        }
        return lists;
    }

    private JSONObject createAddListAction(String listName, int insertIndex) throws JSONException {
        JSONObject action = new JSONObject();
        action.put("action_type", "create");
        action.put("action_id", Integer.toString(actionId));
        action.put("index", insertIndex);
        action.put("entity_delta", createListDelta(listName));
        return action;
    }

    private JSONObject createListDelta(String listName) throws JSONException {
        JSONObject entityDelta = new JSONObject();
        entityDelta.put("name", listName);
        entityDelta.put("entity_type", "GROUP");
        return entityDelta;
    }

    private JSONObject executeRequest(JSONObject request) throws JSONException, IOException {
        return new JSONObject(httpService.post(HttpParameters.with(REQUEST_URL)
                        .formParameter("r", request.toString())
                        .header("AT", "1")
                        .cookie("GTL", authTokenProvider.getToken())));
    }

    private JSONObject createRequest(JSONObject action) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("action_list", new JSONArray(Arrays.asList(action)));
        request.put("client_version", clientVersion);
        return request;
    }

    private String getNewId(JSONObject response) throws JSONException {
        JSONObject results = response.getJSONArray("results").getJSONObject(0);
        return results.getString("new_id");
    }
}
