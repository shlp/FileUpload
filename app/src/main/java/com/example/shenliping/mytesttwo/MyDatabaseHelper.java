package com.example.shenliping.mytesttwo;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_STUDENT="create table student ("
            +"id integer primary key autoincrement,  "
            +"name text,  "+"number text,  "+"sex text,  "+"hobby text, "+"city text)";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //当数据库的版本发生了变化 ,此时onUpgrade方法会自动的被调用
        db.execSQL("Drop table if exists student");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表student
        db.execSQL(CREATE_STUDENT);
        //Toast.makeText(mContext,"Create successed",Toast.LENGTH_SHORT).show();
    }

}
