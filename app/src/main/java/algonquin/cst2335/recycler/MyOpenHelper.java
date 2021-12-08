package algonquin.cst2335.recycler;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";
    public static final int version = 1;
    public static final String TABLE_NAME = "Vehicle_Makes";
    public static final String col_type = "Type";
    public static final String col_attributes = "Attributes";
    public static final String col_id = "id";



    public MyOpenHelper(CarbonActivity context) {

        super(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + col_type + " TEXT, "
                + col_attributes + " TEXT, "
                + col_id + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
