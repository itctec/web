package itc.ink.explorefuture_android.common_unit.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yangwenjiang on 2018/10/25.
 */

public class TextViewWithIndicator extends AppCompatTextView {
    private int canvasWidth=0;
    private int canvasHeight=0;

    private boolean showIndicator=false;

    private Paint paint=new Paint();

    public TextViewWithIndicator(Context context) {
        this(context,null);
    }

    public TextViewWithIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewWithIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint.setAntiAlias(true);
        paint.setColor(Color.argb(150,255,0,0));
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

        if(showIndicator){
            canvas.drawCircle(canvasWidth/2+40+dp2px(3),dp2px(3),dp2px(3),paint);
        }
    }

    private float dp2px(int dp){
        return Resources.getSystem().getDisplayMetrics().density *dp;
    }

    public void showIndicator(boolean showIndicator){
        this.showIndicator=showIndicator;
    }
}
