package my.apps;

import android.app.Application;

public class GToDoApplication extends Application {
    private Controller controller;

    public GToDoApplication() {
        this.controller = new Controller();
    }

    public Controller getController() {
        return controller;
    }
}
