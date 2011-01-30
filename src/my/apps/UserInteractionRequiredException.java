package my.apps;

import android.content.Intent;

import java.io.IOException;

public class UserInteractionRequiredException extends IOException {
    private Intent userInteraction;

    public UserInteractionRequiredException(Intent userInteraction) {
        this.userInteraction = userInteraction;
    }

    public Intent getUserInteraction() {
        return userInteraction;
    }
}
