package my.apps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Synchronizer {
    private TaskService taskService;
    private LocalRepository localRepository;

    public Synchronizer(TaskService taskService, LocalRepository localRepository) {
        this.taskService = taskService;
        this.localRepository = localRepository;
    }

    public void synchronizeLists() throws IOException {
        try {
            JSONArray remoteLists = taskService.getLists();
            String[] values = new String[remoteLists.length() * 2];
            int v = 0;
            for(int l = 0; l < remoteLists.length(); l++) {
                JSONObject listObject =  remoteLists.getJSONObject(l);
                values[v++] = listObject.getString("id");
                values[v++] = listObject.getString("name");
            }
            localRepository.insertLists(new String[] { Lists.ID, Lists.NAME }, values);
        } catch (JSONException ex) {
            throw new IOException(ex);
        }
    }
}
