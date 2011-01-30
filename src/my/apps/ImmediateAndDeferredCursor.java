package my.apps;

import android.database.Cursor;

public interface ImmediateAndDeferredCursor {
    public Cursor getImmediate();

    public Cursor getDeferred();
}
