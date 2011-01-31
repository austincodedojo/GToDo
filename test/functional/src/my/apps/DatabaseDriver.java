package my.apps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDriver {
    private LocalRepository localRepository;

    public DatabaseDriver(Context context) {
        localRepository = new LocalRepository(context);
    }

    public void startWithCleanSlate() {
        SQLiteDatabase db = localRepository.getWritableDatabase();
        localRepository.onUpgrade(db, -1, -1);
        db.close();
    }

    public void addList(String listId, String listName) {
        SQLiteDatabase db = localRepository.getWritableDatabase();
        localRepository.insertLists(new String[] { Lists.ID, Lists.NAME }, new String[] { listId, listName });
        db.close();
    }
}
