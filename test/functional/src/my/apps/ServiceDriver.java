package my.apps;

public class ServiceDriver {
    private TaskService taskService;

    public ServiceDriver() {
        this.taskService = new TaskService(new HttpService());
    }

    public void addList(String listName) {
        taskService.addList(listName);
    }
}
