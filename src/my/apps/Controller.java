package my.apps;

import android.database.Cursor;

import java.io.IOException;

public class Controller {
    private LocalRepository localRepository;
    private Synchronizer synchronizer;

    public Controller(LocalRepository localRepository, Synchronizer synchronizer) {
        this.localRepository = localRepository;
        this.synchronizer = synchronizer;
    }

    public CursorResults getTaskLists(final String[] projection, final String sortOrder) {
        return new CursorResults() {
            public Cursor getImmediate() {
                return localRepository.getLists(projection, sortOrder);
            }

            public Cursor getDeferred() throws IOException {
                synchronizer.synchronizeLists();
                return localRepository.getLists(projection, sortOrder);
            }
        };
    }
}
