package itc.ink.explorefuture_android.recommend.mind_fragment.adapter.implement;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.common_unit.topic_details.TopicDetailsActivity;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_banner.BannerDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.adapter.MindWrapAdapter;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class TopicDelegateImplement implements MindWrapAdapter.DelegateInterface {
    private WeakReference<Context> mWeakContextReference;

    private Context getContext() {
        if(mWeakContextReference.get() != null){
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public void handleTransaction(Context mContext, MindWrapAdapter.WrapperVH mHolder,Object mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);

        mHolder.topicBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mHolder.topicBanner.setImageLoader(new MyLoader());
        mHolder.topicBanner.setImages((ArrayList<TopicListDataMode>) mData);
        mHolder.topicBanner.setBannerAnimation(Transformer.Default);
        mHolder.topicBanner.isAutoPlay(true);
        mHolder.topicBanner.setIndicatorGravity(BannerConfig.RIGHT).start();
        mHolder.topicBanner.setOnBannerListener(new TopicBannerClickListener((ArrayList<TopicListDataMode>) mData));
    }

    class TopicBannerClickListener implements OnBannerListener {
        private ArrayList<TopicListDataMode> mData;

        public TopicBannerClickListener(ArrayList<TopicListDataMode> mData) {
            this.mData = mData;
        }

        @Override
        public void OnBannerClick(int position) {
            Intent intent =new Intent(getContext(), TopicDetailsActivity.class);
            intent.putExtra(TopicDetailsActivity.KEY_TOPIC_ITEM,mData.get(position));
            getContext().startActivity(intent);
        }
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            TopicListDataMode topicData=(TopicListDataMode)path;

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(topicData.getImage_update_datetime()).getObject());

            Glide.with(context).load(topicData.getImage_url()).apply(options).into(imageView);
        }
    }
}
