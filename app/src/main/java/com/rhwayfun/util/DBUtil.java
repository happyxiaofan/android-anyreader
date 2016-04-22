package com.rhwayfun.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rhwayfun on 16-4-22.
 */
public class DBUtil extends SQLiteOpenHelper {

    public DBUtil(Context context){
        super(context,"reader.db",null,1);
    }

    public DBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建小说表 --txt
        // full_path：小说所在的位置
        // now_page：小说当前的页数
        // over_flag：小说是否分页的标志（0表示未分页，1表示已分页）
        String sql = "create table txt(" +
                "   id integer primary key," +
                "   full_path txt," +
                "   now_page integer," +
                "   over_flag integer" +
                ")";
        db.execSQL(sql);
        // 创建每个小说的分页表 --page
        // txt_id：小说的id，是一个外键约束的关系
        // page_num：小说的第几页
        // content：小说在第page_num页的内容
        String sql2 = "create table page(" +
                "   id integer primary key," +
                "   txt_id integer," +
                "   page_num integer," +
                "   content txt" +
                ")";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
