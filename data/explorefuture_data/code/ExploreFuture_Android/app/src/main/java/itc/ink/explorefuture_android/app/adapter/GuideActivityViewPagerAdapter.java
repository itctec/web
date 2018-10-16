package itc.ink.explorefuture_android.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import itc.ink.explorefuture_android.R;


/**
 * Created by yangwenjiang on 2018/9/18.
 */

public class GuideActivityViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> mContentData;

    public GuideActivityViewPagerAdapter(Context mContext, Object mContentData) {
        this.mContext = mContext;
        this.mContentData = (ArrayList<Integer>) mContentData;
    }

    @Override
    public int getCount() {
        return mContentData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = View.inflate(mContext, R.layout.app_guide_viewpager_item, null);
        ImageView contentImage = rootView.findViewById(R.id.app_Guide_ViewPager_ImageView);
        contentImage.setImageResource(mContentData.get(position));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
