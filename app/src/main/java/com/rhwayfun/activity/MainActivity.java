package com.rhwayfun.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rhwayfun.dao.PageDao;
import com.rhwayfun.dao.TxtDao;
import com.rhwayfun.util.GlobalUtil;
import com.rhwayfun.view.AnyReaderView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Map<String, Object> txtMap;
    private Map<String,Object> pageMap = new HashMap<>();
    private String fullPath;
    private StringBuilder builder = new StringBuilder();
    private int lineCount;
    private int pageNum;
    private Handler handler;
    private AnyReaderView myView;
    private TextView pageView;
    private int nowPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalUtil.init(this);
        setContentView(R.layout.activity_main);

        myView = (AnyReaderView) findViewById(R.id.content_view);
        pageView = (TextView) findViewById(R.id.page_view);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    // 表示为完成分页，将分页的信息显示出来
                    pageView.setText("已分页" + (pageNum - 1));
                }else if (msg.what == 1){
                    // 表示已经完成了分页
                    pageView.setText("已完成分页" + nowPage + "/" + pageNum);
                }
            }
        };

        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiyou.txt";
        TxtDao.insertData(fullPath);

        txtMap = TxtDao.findDataByFullPath(fullPath);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    if ((int)txtMap.get("over_flag") == 0) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath)));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null){
                            while (line.length() > GlobalUtil.LINE_CHAR_COUNT){
                                String str = line.substring(0,GlobalUtil.LINE_CHAR_COUNT);
                                addToLine(str);
                                line = line.substring(GlobalUtil.LINE_CHAR_COUNT);
                            }
                            addToLine(line);
                        }
                        bufferedReader.close();
                        handler.sendEmptyMessage(1);
                        // 更新分页的信息
                        TxtDao.updateOverFlag(fullPath);
                    } else {
                        // 跳转到指定的页数
                        pageNum = PageDao.findPageCount((Integer) txtMap.get("txt_id"));
                        nowPage = (int) txtMap.get("now_page");
                        myView.changeData();
                        //handler.sendEmptyMessage(1);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void addToLine(String str) throws InterruptedException {
        builder.append(str);
        builder.append(GlobalUtil.LINE_END_FLAG);
        lineCount++;
        if (lineCount == GlobalUtil.LINE_COUNT){
            pageMap.put("txt_id",txtMap.get("txt_id"));
            pageMap.put("page_num",pageNum++);
            pageMap.put("content",builder.toString());
            PageDao.insertData(pageMap);
            if (pageNum == 10){
                myView.changeData();
            }
            TimeUnit.MILLISECONDS.sleep(10);
            handler.sendEmptyMessage(0);
            lineCount = 0;
            builder = new StringBuilder();
        }

        TxtDao.updateOverFlag(fullPath);
    }

    public Map<String, Object> getTxtMap() {
        return txtMap;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getPageNum() {
        return pageNum;
    }

    @Override
    protected void onDestroy() {
        TxtDao.updateNowPage(nowPage, fullPath);
        super.onDestroy();
    }
}
