package itc.ink.explorefuture_android.common_unit.image_view;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.R;


/**
 * Created by yangwenjiang on 2018/9/18.
 */

public class ImageViewerActivityViewPagerAdapter extends PagerAdapter {
    private WeakReference<Context> mWeakContextReference;
    private ArrayList<String> mContentData;

    public ImageViewerActivityViewPagerAdapter(Context mContext, Object mContentData) {
        this.mWeakContextReference = new WeakReference<>(mContext);
        this.mContentData = (ArrayList<String>) mContentData;
    }

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mContentData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = View.inflate(getContext(), R.layout.common_unit_image_view_viewpager_item, null);
        ImageView contentImage = rootView.findViewById(R.id.image_Viewer_Item_ImageView);
        contentImage.setOnClickListener(new ContentImageClickListener());
        Glide.with(getContext()).load(mContentData.get(position)).into(contentImage);
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

    class ContentImageClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Message msg=ImageViewerActivity.mHandler.obtainMessage();
            msg.what=ImageViewerActivity.MSG_FINISH_ACTIVITY;
            ImageViewerActivity.mHandler.dispatchMessage(msg);
        }
    }
}
