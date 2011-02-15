package my.apps;

import android.app.Application;

public class GToDoApplication extends Application {
    private Controller controller;
    private LocalRepository localRepository;
    private Synchronizer synchronizer;
    private TaskService taskService;
    private HttpService httpService;
    private AuthTokenProvider authTokenProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        httpService = new HttpService();
        authTokenProvider = new AuthTokenProvider(getApplicationContext());
        taskService = new TaskService(httpService, authTokenProvider);
        localRepository = new LocalRepository(getApplicationContext());
        synchronizer = new Synchronizer(taskService, localRepository);
        this.controller = new Controller(localRepository, synchronizer);
    }

    public Controller getController() {
        return controller;
    }

    public AuthTokenProvider getAuthTokenProvider() {
        return authTokenProvider;
    }
}
