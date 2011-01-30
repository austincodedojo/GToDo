package my.apps;

import android.database.Cursor;

public class Controller {
    public CursorResults getTaskLists() {
        return new CursorResults() {
            public Cursor getImmediate() {
                return null;
            }

            public Cursor getDeferred() {
                return null;
            }
        };
    }
}
