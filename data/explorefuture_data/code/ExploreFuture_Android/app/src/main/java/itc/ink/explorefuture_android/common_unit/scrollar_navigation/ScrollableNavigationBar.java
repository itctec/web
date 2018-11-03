package itc.ink.explorefuture_android.common_unit.scrollar_navigation;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.UnitConversionUtil;
import itc.ink.explorefuture_android.sort.mode.DataLoad;
import itc.ink.explorefuture_android.sort.mode.mode_navigation.NavigationBarDataMode;
import itc.ink.explorefuture_android.sort.mode.mode_sort.SortListDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class ScrollableNavigationBar extends ConstraintLayout {
    private LinearLayout sortNavigationBarLayout;

    private OutCallBack mOutCallBack;

    public ScrollableNavigationBar(Context context) {
        this(context, null);
    }

    public ScrollableNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollableNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.common_unit_scrollar_navigationbar, this);

        sortNavigationBarLayout = findViewById(R.id.profession_NavigationBar_Layout);
    }

    public void setDataAndCallBack(Context context, ArrayList<NavigationBarDataMode> mSortTitleData, OutCallBack mOutCallBack) {
        for (int i = 0; i < mSortTitleData.size(); i++) {
            NavigationBarDataMode navigationBarDataMode = mSortTitleData.get(i);
            TextView titleItemTextView = new TextView(context);
            titleItemTextView.setTag(navigationBarDataMode.getId());
            titleItemTextView.setText(navigationBarDataMode.getTitle());
            titleItemTextView.setTextSize(13);
            titleItemTextView.setTextColor(Color.GRAY);
            titleItemTextView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams titleItemLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                titleItemTextView.setBackgroundResource(R.drawable.app_scorllar_navigation_item_bg);
                titleItemTextView.setTextColor(context.getColor(R.color.sort_top_navigation_text_selected_color));
            }
            titleItemTextView.setPadding(30, 0, 30, 0);

            titleItemTextView.setLayoutParams(titleItemLayoutParams);
            titleItemTextView.setOnClickListener(new titleItemTextViewClickListener(context));
            sortNavigationBarLayout.addView(titleItemTextView);
        }


        this.mOutCallBack = mOutCallBack;
    }

    public void setCurrentFocusItem(View view, int position) {
        for (int i = 0; i < sortNavigationBarLayout.getChildCount(); i++) {
            TextView titleItemTextView = (TextView) sortNavigationBarLayout.getChildAt(i);
            titleItemTextView.setTextColor(getContext().getColor(R.color.sort_top_navigation_text_unselected_color));
            titleItemTextView.setBackgroundResource(R.drawable.app_scorllar_navigation_item_bg_empty);
            titleItemTextView.setPadding(30, 0, 30, 0);
        }
        TextView titleItemTextView;
        if (view != null) {
            titleItemTextView = (TextView) view;
        }else{
            titleItemTextView = (TextView) sortNavigationBarLayout.getChildAt(position);
        }

        titleItemTextView.setTextColor(getContext().getColor(R.color.sort_top_navigation_text_selected_color));
        titleItemTextView.setBackgroundResource(R.drawable.app_scorllar_navigation_item_bg);
        titleItemTextView.setPadding(30, 0, 30, 0);
    }

    class titleItemTextViewClickListener implements OnClickListener {
        WeakReference<Context> mWeakContextReference;

        public titleItemTextViewClickListener(Context mContext) {
            this.mWeakContextReference = new WeakReference<>(mContext);
        }

        private Context getContext() {
            if (mWeakContextReference.get() != null) {
                return mWeakContextReference.get();
            }
            return null;
        }

        @Override
        public void onClick(View view) {
            setCurrentFocusItem(view,0);

            mOutCallBack.onTitleClick((String) view.getTag());
        }
    }

    public interface OutCallBack {
        void onTitleClick(String id);
    }
}
