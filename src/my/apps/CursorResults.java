package my.apps;

import android.database.Cursor;

public interface CursorResults {
    public Cursor getImmediate();

    public Cursor getDeferred();
}
