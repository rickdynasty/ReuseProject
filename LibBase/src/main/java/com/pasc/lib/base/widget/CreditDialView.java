package com.pasc.lib.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.pasc.lib.base.R;

/**
 * Created by huangtebian535 on 2018/06/05.
 */
public class CreditDialView extends View {
    public static final int COUNT = 44;
    private String scoreStr = "";
    private String creditStr = "";
    private String dateStr = "";
    private int num = 0;

    public CreditDialView(Context context) {
        super(context);
    }

    public CreditDialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CreditDialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRoundRect(canvas, COUNT, "#08979C", 5, 200);
        drawImaginaryLine(canvas, COUNT, "#5CDBD3", 5, 200);
        drawRoundRect(canvas, num, "#FFFFFF", 5, 200);
        drawText(canvas);
    }

    public void drawRoundRect(Canvas canvas, int num, String color, float interval, int angle) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(color));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        int x = getWidth() / 2;
        int y = getHeight() / 2;

        canvas.translate(x, y);

        int c = -angle;
        canvas.rotate(c);
        for (int i = 0; i < num; i++) {
            RectF rectF = new RectF(x - 40, 0, x, 14);
            canvas.drawRoundRect(rectF, 8, 8, paint);
            canvas.rotate(interval);
            canvas.save();
        }
        canvas.rotate(-(num * interval - angle));
        canvas.translate(-x, -y);
        canvas.save();
    }

    public void drawImaginaryLine(Canvas canvas, int num, String color, int interval, int angle) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(color));

        int x = getWidth() / 2;
        int y = getHeight() / 2;

        canvas.translate(x, y);

        int c = -angle;
        canvas.rotate(c);
        for (int i = 0; i < num; i++) {
            RectF rectF = new RectF(x - 59, 0, x - 54, 8);
            canvas.drawRoundRect(rectF, 8, 8, paint);
            canvas.rotate(interval);
            canvas.save();
        }
        canvas.rotate(-(num * interval - angle));
        canvas.translate(-x, -y);
        canvas.save();
    }

    /**
     * 画文字
     *
     * @param canvas Canvas
     */
    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white_ffffff));

        //信用等级
        paint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_14));
        Rect creditRect = new Rect();
        paint.getTextBounds(creditStr, 0, creditStr.length(), creditRect);
        canvas.drawText(creditStr, (getWidth() - creditRect.width()) / 2, (getHeight() + creditRect.height()) / 3.1F, paint);

        //信用评分
        paint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_35));
        Rect scoreRect = new Rect();
        paint.getTextBounds(scoreStr, 0, scoreStr.length(), scoreRect);
        canvas.drawText(scoreStr, (getWidth() - scoreRect.width()) / 2, (getHeight() + scoreRect.height()) / 1.8F, paint);

        //查询时间
        paint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_13));
        Rect dateRect = new Rect();
        paint.getTextBounds(dateStr, 0, dateStr.length(), dateRect);
        canvas.drawText(dateStr, (getWidth() - dateRect.width()) / 2, (getHeight() + dateRect.height()) / 1.32F, paint);
    }

    public String getScoreStr() {
        return scoreStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
    }

    public String getCreditStr() {
        return creditStr;
    }

    public void setCreditStr(String creditStr) {
        this.creditStr = creditStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        if (num > COUNT) {
            this.num = COUNT;
            return;
        }
        this.num = num;
    }
}
