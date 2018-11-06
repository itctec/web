package itc.ink.explorefuture_android.common_unit.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import itc.ink.explorefuture_android.app.utils.UnitConversionUtil;

/**
 * Created by yangwenjiang on 2018/10/25.
 */

public class EarningsBightView extends View {
    private int canvasWidth = 0;
    private int canvasHeight = 0;

    private Paint paint = new Paint();
    private Rect textRect = new Rect();

    private ArrayList<Float> earningsData;
    private float minValue = 0, maxValue = 0;
    private String valueStr1 = "0", valueStr2 = "0", valueStr3 = "0", valueStr4 = "0";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");

    private String today, preToday1, preToday2, preToday3, preToday4, preToday5, preToday6;

    public EarningsBightView(Context context) {
        this(context, null);
    }

    public EarningsBightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EarningsBightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(1);
        paint.setTextSize(UnitConversionUtil.dip2px(12));

        today = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday1 = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday2 = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday3 = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday4 = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday5 = simpleDateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        preToday6 = simpleDateFormat.format(calendar.getTime());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        canvasWidth = right - left;
        canvasHeight = bottom - top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.GRAY);
        paint.getTextBounds("" + 100, 0, ("" + 100).length(), textRect);
        canvas.drawText(valueStr4, 0, canvasHeight / 5 + textRect.height() / 2, paint);
        canvas.drawText(valueStr3, 0, canvasHeight * 2 / 5 + textRect.height() / 2, paint);
        canvas.drawText(valueStr2, 0, canvasHeight * 3 / 5 + textRect.height() / 2, paint);
        canvas.drawText(valueStr1, 0, canvasHeight * 4 / 5 + textRect.height() / 2, paint);
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(UnitConversionUtil.dip2px(50), canvasHeight / 5, canvasWidth - UnitConversionUtil.dip2px(10), canvasHeight / 5, paint);
        canvas.drawLine(UnitConversionUtil.dip2px(50), canvasHeight * 2 / 5, canvasWidth - UnitConversionUtil.dip2px(10), canvasHeight * 2 / 5, paint);
        canvas.drawLine(UnitConversionUtil.dip2px(50), canvasHeight * 3 / 5, canvasWidth - UnitConversionUtil.dip2px(10), canvasHeight * 3 / 5, paint);
        canvas.drawLine(UnitConversionUtil.dip2px(50), canvasHeight * 4 / 5, canvasWidth - UnitConversionUtil.dip2px(10), canvasHeight * 4 / 5, paint);

        paint.setColor(Color.GRAY);
        paint.getTextBounds(preToday6, 0, preToday6.length(), textRect);
        canvas.drawText(preToday6, UnitConversionUtil.dip2px(50), canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(preToday5, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()) / 6, canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(preToday4, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()) * 2 / 6, canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(preToday3, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()) * 3 / 6, canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(preToday2, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()) * 4 / 6, canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(preToday1, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()) * 5 / 6, canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);
        canvas.drawText(today, UnitConversionUtil.dip2px(50) + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width()), canvasHeight * 4 / 5 + textRect.height() + UnitConversionUtil.dip2px(10), paint);

        paint.setColor(Color.rgb(0x55,0x88,0xff));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(UnitConversionUtil.dip2px(1));

        ArrayList <Point> mPointList=new ArrayList<>();
        for(int i=0;i<7;i++){
            Point point =new Point();
            point.set((int)getCenterX(i), (int)getCenterY(i));
            mPointList.add(point);
        }

        ArrayList <Point> mControlPointList=calculateControlPoint(mPointList);

        Path path=new Path();
        path.moveTo(getCenterX(0), getCenterY(0));
        for(int i=0;i<6;i++){
            path.cubicTo(mControlPointList.get(i*2).x,mControlPointList.get(i*2).y,mControlPointList.get(i*2+1).x,mControlPointList.get(i*2+1).y,mPointList.get(i+1).x,mPointList.get(i+1).y);
        }
        canvas.drawPath(path,paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawCircle(getCenterX(0), getCenterY(0), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(1), getCenterY(1), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(2), getCenterY(2), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(3), getCenterY(3), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(4), getCenterY(4), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(5), getCenterY(5), UnitConversionUtil.dip2px(2), paint);
        canvas.drawCircle(getCenterX(6), getCenterY(6), UnitConversionUtil.dip2px(2), paint);

    }

    private ArrayList <Point> calculateControlPoint(ArrayList <Point> mPointList) {
        ArrayList <Point> mControlPointList=new ArrayList<>();
        float rate=0.4f;
        if (mPointList.size() <= 1) {
            return null;
        }
        for (int i=0;i<mPointList.size();i++) {
            if(i==0) {
                Point nextPoint = mPointList.get(i+1);
                Point controlPoint=new Point();
                controlPoint.x=(int)(mPointList.get(i).x+(nextPoint.x - mPointList.get(i).x) * rate);
                controlPoint.y=mPointList.get(i).y;
                mControlPointList.add(controlPoint);
            }else if(i==mPointList.size()-1){
                Point lastPoint = mPointList.get(i);
                Point controlPoint=new Point();
                controlPoint.x=(int)(mPointList.get(i).x-( mPointList.get(i).x - lastPoint.x) * rate);
                controlPoint.y=mPointList.get(i).y;
                mControlPointList.add(controlPoint);
            }else{
                Point prePoint = mPointList.get(i-1);
                Point nextPoint = mPointList.get(i+1);

                float k=(nextPoint.y - prePoint.y) / (nextPoint.x - prePoint.x);
                float b = mPointList.get(i).y - k * mPointList.get(i).x;

                Point preControlPoint=new Point();
                preControlPoint.x=(int)(mPointList.get(i).x-(mPointList.get(i).x - prePoint.x) * rate);
                preControlPoint.y=(int)(k*preControlPoint.x+b);
                mControlPointList.add(preControlPoint);

                Point nextControlPoint=new Point();
                nextControlPoint.x=(int)(mPointList.get(i).x+(nextPoint.x - mPointList.get(i).x) * rate);
                nextControlPoint.y=(int)(k*nextControlPoint.x+b);
                mControlPointList.add(nextControlPoint);

            }
        }

        return mControlPointList;
    }

    private float getCenterX(int i){
        return UnitConversionUtil.dip2px(50) + textRect.width() / 2 + (canvasWidth - UnitConversionUtil.dip2px(60) - textRect.width())*i / 6;
    }

    private float getCenterY(int i){
        return canvasHeight * 4 / 5 - (earningsData.get(i) - minValue) / (maxValue - minValue) * canvasHeight * 3 / 5;
    }

    public void updateData(ArrayList<Float> earningsData) {
        this.earningsData = earningsData;

        minValue = Math.min(earningsData.get(0), earningsData.get(1));
        maxValue = Math.max(earningsData.get(0), earningsData.get(1));
        for (int i = 0; i < earningsData.size(); i++) {
            minValue = Math.min(minValue, earningsData.get(i));
            maxValue = Math.max(maxValue, earningsData.get(i));
        }

        DecimalFormat decimalFormat = new DecimalFormat("00.00");
        if (minValue == maxValue) {
            valueStr1 = decimalFormat.format(minValue);
            valueStr2 = decimalFormat.format(minValue + 20);
            valueStr3 = decimalFormat.format(minValue + 40);
            valueStr4 = decimalFormat.format(minValue + 60);
        } else {
            valueStr1 = decimalFormat.format(minValue);
            valueStr4 = decimalFormat.format(maxValue);
            valueStr2 = decimalFormat.format(minValue + (maxValue - minValue) / 3);
            valueStr3 = decimalFormat.format(minValue + (maxValue - minValue) * 2 / 3);
        }

        invalidate();
    }
}
