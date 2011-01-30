package my.apps;

import android.app.Application;

public class GToDoApplication extends Application {
    private Controller controller;

    public Controller getController() {
        return controller;
    }
}
