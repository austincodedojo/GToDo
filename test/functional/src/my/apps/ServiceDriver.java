package my.apps;

import android.content.Context;

import java.io.IOException;

public class ServiceDriver {
    private TaskService taskService;

    public ServiceDriver(Context context) {
        this.taskService = new TaskService(new HttpService(), new AuthTokenProvider(context));
    }

    public void addList(String listName) throws IOException {
        taskService.addList(listName);
    }
}
