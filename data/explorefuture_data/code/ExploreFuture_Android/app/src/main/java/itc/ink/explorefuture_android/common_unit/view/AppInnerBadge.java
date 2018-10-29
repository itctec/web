package itc.ink.explorefuture_android.common_unit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import itc.ink.explorefuture_android.R;

/**
 * Created by yangwenjiang on 2018/10/25.
 */

public class AppInnerBadge extends View {
    private int canvasWidth=0;
    private int canvasHeight=0;

    private Paint paint=new Paint();
    private Rect textRect = new Rect();
    private int count=0;

    public AppInnerBadge(Context context) {
        this(context,null);
    }

    public AppInnerBadge(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppInnerBadge(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        canvasWidth=right-left-2;
        canvasHeight=bottom-top-2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.argb(150,255,0,0));
        paint.setTextSize(canvasHeight-6);
        canvas.drawRoundRect(1,1,canvasWidth,canvasHeight,canvasHeight/2,canvasHeight/2,paint);

        paint.setColor(Color.WHITE);
        paint.getTextBounds(""+count, 0, (""+count).length(), textRect);
        int textWidth = textRect.width();
        int textHeight = textRect.height();
        canvas.drawText(""+count,canvasWidth/2-textWidth/2,canvasHeight/2+textHeight/2,paint);
    }

    public void setCount(int count){
        if (count<=0){
            this.count=count;
            invalidate();
            this.setVisibility(View.GONE);
        }else{
            this.setVisibility(View.VISIBLE);
            this.count=count;
            invalidate();
        }
    }
}
