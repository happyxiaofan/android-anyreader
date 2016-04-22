package com.rhwayfun.dao;

import android.database.Cursor;

import com.rhwayfun.util.GlobalUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rhwayfun on 16-4-22.
 */
public class TxtDao {

    public static void insertData(String fullPath){
        String sql = "select id from txt where full_path = ?";
        Cursor cursor = GlobalUtil.dbUtil.getReadableDatabase().rawQuery(sql,new String[]{fullPath});
        // 如果没有在数据库插入数据才执行插入
        if (!cursor.moveToFirst()){
            sql = "insert into txt(full_path, now_page, over_flag) values(?,1,0)";
            GlobalUtil.dbUtil.getWritableDatabase().execSQL(sql,new Object[]{fullPath});
        }
    }

    public static Map<String,Object> findDataByFullPath(String fullPath){
        Map<String,Object> map = new HashMap<>();

        String sql = "select id, now_page, over_flag from txt where full_path = ?";
        Cursor cursor = GlobalUtil.dbUtil.getReadableDatabase().rawQuery(sql,new String[]{fullPath});
        cursor.moveToFirst();
        map.put("txt_id", cursor.getInt(0));
        map.put("now_page", cursor.getInt(1));
        map.put("over_flag",cursor.getInt(2));

        return map;
    }

    public static void updateOverFlag(String fullPath) {
        String sql = "update txt set over_flag = 1 where full_path = ?";
        GlobalUtil.dbUtil.getWritableDatabase().execSQL(sql, new Object[]{fullPath});
    }

    public static void updateNowPage(int nowPage, String fullPath) {
        String sql = "update txt set now_page = ? where full_path = ?";
        GlobalUtil.dbUtil.getWritableDatabase().execSQL(sql,new Object[]{nowPage, fullPath});
    }
}
