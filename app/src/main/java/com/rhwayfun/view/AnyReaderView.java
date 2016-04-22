package com.rhwayfun.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rhwayfun.activity.MainActivity;
import com.rhwayfun.dao.PageDao;
import com.rhwayfun.util.GlobalUtil;

import java.util.Map;

/**
 * Created by rhwayfun on 16-4-22.
 */
public class AnyReaderView extends View {

    //private String content = "盖闻天地之数，有十二万九千六百岁为一元。将一元分为十二会，乃子、丑、寅、卯、辰、巳、午、未、申、酉、戌、亥之十二支也。每会该一万八百岁。且就一日而论：子时得阳气，而丑则鸡鸣；寅不通光，而卯则日出；辰时食后，而巳则挨排；日午天中，而未则西蹉；申时晡而日落酉；戌黄昏而人定亥。譬于大数，若到戌会之终，则天地昏蒙而万物否矣。再去五千四百岁，交亥会之初，则当黑暗，而两间人物俱无矣，故曰混沌。又五千四百岁，亥会将终，贞下起元，近子之会，而复逐渐开明。邵康节曰：“冬至子之半，天心无改移。一阳初动处，万物未生时。”到此，天始有根。再五千四百岁，正当子会，轻清上腾，有日，有月，有星，有辰。日、月、星、辰，谓之四象。故曰，天开于子。又经五千四百岁，子会将终，近丑之会，而逐渐坚实。";

    private String content;
    private MainActivity activity;
    private float startX;
    private float nowX;
    private String preContent;
    private String nextContent;

    public AnyReaderView(Context context) {
        super(context);
    }

    public AnyReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity)context;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    nowX = event.getX();
                    postInvalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs(nowX - startX) >= 50) {
                        if (nowX - startX > 0) {
                            // 上一页
                            if (activity.getNowPage() > 1) {
                                activity.setNowPage(activity.getNowPage() - 1);
                                changeData();
                            }
                        }
                        if (nowX - startX < 0) {
                            // 下一页
                            if (activity.getNowPage() < activity.getPageNum()) {
                                activity.setNowPage(activity.getNowPage() + 1);
                                changeData();
                            }
                        }
                    }
                    startX = 0;
                    nowX = 0;
                    postInvalidate();
                }

                return false;
            }
        });
    }

    public AnyReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void changeData(){
        Map<String,Object> txtMap = activity.getTxtMap();
        content = PageDao.findPageData((int)txtMap.get("txt_id"), activity.getNowPage());

        if (activity.getNowPage() > 1){
            preContent = PageDao.findPageData((Integer) txtMap.get("txt_id"),activity.getNowPage() - 1);
        }else {
            preContent = null;
        }

        if (activity.getNowPage() < activity.getPageNum()){
            nextContent = PageDao.findPageData((Integer) txtMap.get("txt_id"),activity.getNowPage() + 1);
        }else {
            nextContent = null;
        }
        activity.getHandler().sendEmptyMessage(1);
        //执行该方法可以调用onDraw方法
        super.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(GlobalUtil.CHAR_SIZE);

        if (content != null){

            String[] pageArray = content.split(GlobalUtil.LINE_END_FLAG);

            for (int i = 0; i < pageArray.length; i++){
                for (int j = 0; j < pageArray[i].length(); j++){
                    //如果绘制的字符在content范围内就绘制
                    if (j + i * GlobalUtil.LINE_CHAR_COUNT < content.length()){
                        canvas.drawText(String.valueOf(pageArray[i].charAt(j))
                                , GlobalUtil.PAGE_SEP + j * (GlobalUtil.CHAR_SIZE + GlobalUtil.CHAR_SEP + (startX - nowX))
                                , (GlobalUtil.CHAR_SIZE + GlobalUtil.LINE_SEP) * (i + 1)
                                , paint);
                    }
                }
            }

            //下一页
            if (startX - nowX > 0){
                pageArray = nextContent.split(GlobalUtil.LINE_END_FLAG);
                for (int i = 0; i < pageArray.length; i++){
                    for (int j = 0; j < pageArray[i].length(); j++){
                        //如果绘制的字符在content范围内就绘制
                        if (j + i * GlobalUtil.LINE_CHAR_COUNT < content.length()){
                            canvas.drawText(String.valueOf(pageArray[i].charAt(j))
                                    , GlobalUtil.PAGE_SEP + j * (GlobalUtil.CHAR_SIZE + GlobalUtil.CHAR_SEP + (startX - nowX) + GlobalUtil.SCREEN_WIDTH)
                                    , (GlobalUtil.CHAR_SIZE + GlobalUtil.LINE_SEP) * (i + 1)
                                    , paint);
                        }
                    }
                }
            }

            //上一页
            if (startX - nowX < 0){
                pageArray = preContent.split(GlobalUtil.LINE_END_FLAG);
                for (int i = 0; i < pageArray.length; i++){
                    for (int j = 0; j < pageArray[i].length(); j++){
                        //如果绘制的字符在content范围内就绘制
                        if (j + i * GlobalUtil.LINE_CHAR_COUNT < content.length()){
                            canvas.drawText(String.valueOf(pageArray[i].charAt(j))
                                    , GlobalUtil.PAGE_SEP + j * (GlobalUtil.CHAR_SIZE + GlobalUtil.CHAR_SEP + (startX - nowX) - GlobalUtil.SCREEN_WIDTH)
                                    , (GlobalUtil.CHAR_SIZE + GlobalUtil.LINE_SEP) * (i + 1)
                                    , paint);
                        }
                    }
                }
            }
        }
    }
}
