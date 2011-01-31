package my.apps;

import android.database.Cursor;

import java.io.IOException;

public interface CursorResults {
    public Cursor getImmediate();

    public Cursor getDeferred() throws IOException;
}
