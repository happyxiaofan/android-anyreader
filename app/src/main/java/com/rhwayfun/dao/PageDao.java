package com.rhwayfun.dao;

import android.database.Cursor;

import com.rhwayfun.util.GlobalUtil;

import java.util.Map;

/**
 * Created by rhwayfun on 16-4-22.
 */
public class PageDao {

    public static void insertData(Map<String,Object> map){
        String sql = "insert into page(txt_id, page_num, content) values(?, ?, ?)";
        GlobalUtil.dbUtil.getWritableDatabase().execSQL(sql,
                new Object[]{map.get("txt_id"),
                        map.get("page_num"),
                        map.get("content").toString()});
    }

    public static String findPageData(int txtid, int pagenum){
        String sql = "select content from page where txt_id = ? and page_num = ?";
        Cursor cursor = GlobalUtil.dbUtil.getReadableDatabase().rawQuery(sql,new String[]{"" + txtid, "" + pagenum});
        cursor.moveToFirst();
        String content = cursor.getString(0);
        return content != null ? content : null;
    }

    public static int findPageCount(int txt_id) {
        String sql = "select count(id) from page where txt_id = ?";
        Cursor c = GlobalUtil.dbUtil.getReadableDatabase().rawQuery(sql,new String[]{"" + txt_id});
        c.moveToFirst();
        int pageNum = c.getInt(0);
        return pageNum;
    }
}
