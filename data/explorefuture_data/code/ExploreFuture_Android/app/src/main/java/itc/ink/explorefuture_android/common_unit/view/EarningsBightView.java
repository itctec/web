package itc.ink.explorefuture_android.common_unit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yangwenjiang on 2018/10/25.
 */

public class EarningsBightView extends View {
    private int canvasWidth=0;
    private int canvasHeight=0;

    private Paint paint=new Paint();
    private Rect textRect = new Rect();

    public EarningsBightView(Context context) {
        this(context,null);
    }

    public EarningsBightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EarningsBightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(1);
        paint.setTextSize(30);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        canvasWidth=right-left;
        canvasHeight=bottom-top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint.getTextBounds(""+200, 0, (""+200).length(), textRect);
        canvas.drawText("200",20,canvasHeight/5+textRect.height()/2,paint);
        canvas.drawLine(100,canvasHeight/5,canvasWidth-50,canvasHeight/5,paint);
        canvas.drawText("180",20,canvasHeight*2/5+textRect.height()/2,paint);
        canvas.drawLine(100,canvasHeight*2/5,canvasWidth-50,canvasHeight*2/5,paint);
        canvas.drawText("160",20,canvasHeight*3/5+textRect.height()/2,paint);
        canvas.drawLine(100,canvasHeight*3/5,canvasWidth-50,canvasHeight*3/5,paint);
        canvas.drawText("140",20,canvasHeight*4/5+textRect.height()/2,paint);
        canvas.drawLine(100,canvasHeight*4/5,canvasWidth-50,canvasHeight*4/5,paint);
    }
}
