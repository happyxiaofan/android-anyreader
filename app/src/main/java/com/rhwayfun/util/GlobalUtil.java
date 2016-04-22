package com.rhwayfun.util;

import android.app.Activity;

/**
 * Created by rhwayfun on 16-4-22.
 */
public class GlobalUtil {

    //屏幕的高度
    public static int SCREEN_WIDTH;
    //屏幕的宽度
    public static int SCREEN_HEIGHT;

    //每行显示多少个字
    public static final int LINE_CHAR_COUNT = 20;
    //页边距
    public static final int PAGE_SEP = 2;
    //行边距
    public static final int LINE_SEP = 2;
    //字符间距
    public static final int CHAR_SEP = 1;

    //字符大小
    public static int CHAR_SIZE;
    //显示的行数
    public static int LINE_COUNT;

    //DBUtil
    public static DBUtil dbUtil;

    //行分隔符
    public static final String LINE_END_FLAG = "LINE_END_FLAG";

    public static void init(Activity activity){
        dbUtil = new DBUtil(activity);

        SCREEN_HEIGHT = activity.getWindowManager().getDefaultDisplay().getHeight();
        SCREEN_WIDTH = activity.getWindowManager().getDefaultDisplay().getWidth();

        CHAR_SIZE = (SCREEN_WIDTH - PAGE_SEP * 2 - (LINE_CHAR_COUNT - 1) * CHAR_SEP) / LINE_CHAR_COUNT;
        LINE_COUNT = SCREEN_HEIGHT * 4 / 5 / (CHAR_SIZE + LINE_SEP);
    }
}
