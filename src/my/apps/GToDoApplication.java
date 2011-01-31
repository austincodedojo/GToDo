package my.apps;

import android.app.Application;

public class GToDoApplication extends Application {
    private Controller controller;
    private LocalRepository localRepository;
    private Synchronizer synchronizer;

    @Override
    public void onCreate() {
        super.onCreate();

        localRepository = new LocalRepository(getApplicationContext());
        synchronizer = new Synchronizer();
        this.controller = new Controller(localRepository, synchronizer);
    }

    public Controller getController() {
        return controller;
    }
}
