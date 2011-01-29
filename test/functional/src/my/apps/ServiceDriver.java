package my.apps;

import java.io.IOException;

public class ServiceDriver {
    private TaskService taskService;

    public ServiceDriver() {
        this.taskService = new TaskService(new HttpService(), new AuthTokenProvider());
    }

    public void addList(String listName) throws IOException {
        taskService.addList(listName);
    }
}
