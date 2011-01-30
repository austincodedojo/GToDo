package my.apps;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalRepository extends SQLiteOpenHelper {
    private static int VERSION = 1;
    private static String DB_NAME = "todo";
    private static final String TABLE_LISTS = "lists";

    public LocalRepository(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_LISTS + "(" +
                Lists._ID + " int, " +
                Lists.ID + " text, " +
                Lists.NAME + " text, " +
                "constraint unique_id unique (" + Lists.ID + ") on conflict replace" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_LISTS);
        onCreate(db);
    }

    public void insertLists(String[] columns, String[] values) {
        SQLiteDatabase db = getWritableDatabase();
        for(int v = 0; v < values.length; v++) {
            ContentValues cvs = new ContentValues();
            for(int c = 0; c < columns.length; c++) {
                cvs.put(columns[c], values[v]);
            }
            db.insert(TABLE_LISTS, null, cvs);
        }
    }
}
