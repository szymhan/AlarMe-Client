package pl.szymonhanzel.alarmeclient.component;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

public class DatabaseAdapter {
    private static final String TAG = "DatabaseAdapter";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private final static String DB_TABLE = "alarms_history";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_VEHICLE_TYPE= "vehicle_type";
    public static final String VEHICLE_TYPE_OPTIONS = "TEXT NOT NULL";
    public static final int VEHICLE_TYPE_COLUMN =1;
    public static final String KEY_ADDRESS = "address";
    public static final String ADDRESS_OPTIONS = "TEXT NOT NULL";
    public static final int ADDRESS_COLUMN =2;
    public static final String KEY_DATE = "date";
    public static final String DATE_OPTIONS = "TEXT NOT NULL";
    public static final int DATE_COLUMN = 3;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    public DatabaseAdapter(Context context){
        this.context = context;
    }

    private static final String DB_CREATE_TABLE =
            "CREATE TABLE "+ DB_TABLE + " ( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_VEHICLE_TYPE + " " + VEHICLE_TYPE_OPTIONS + ", " +
                    KEY_ADDRESS + " " + ADDRESS_OPTIONS + ", "+
                    KEY_DATE + " " + DATE_OPTIONS + " );";

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + DB_TABLE;

    public DatabaseAdapter open() {
        dbHelper = new DatabaseHelper(context,DB_NAME,null,DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert (String vehicleType, String address, String date) {
        ContentValues newContentValues =  new ContentValues();
        newContentValues.put(KEY_VEHICLE_TYPE,vehicleType);
        newContentValues.put(KEY_ADDRESS,address);
        newContentValues.put(KEY_DATE,date);
        return db.insert(DB_TABLE,null,newContentValues);
    }

    public boolean delete (long id) {
        String where = KEY_ID +"=" + id;
        return db.delete(DB_TABLE,where,null) >0;
    }

    public boolean deleteAll () {
        return db.delete(DB_TABLE,null,null) >0;
    }

    public Cursor getAll () {
        String[]columns = {KEY_ID,KEY_VEHICLE_TYPE,KEY_ADDRESS,KEY_DATE};
        return db.query(DB_TABLE,columns,null,null,null,null,KEY_ID);
    }


private static class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context,name,cursorFactory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE);

        Log.d(TAG, "Database creating...");
        Log.d(TAG, "Table " + DB_TABLE + " ver." + DB_VERSION + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE);

        Log.d(TAG, "Database updating...");
        Log.d(TAG, "Table " + DB_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
        Log.d(TAG, "All data is lost.");

        onCreate(db);

    }
}
}
