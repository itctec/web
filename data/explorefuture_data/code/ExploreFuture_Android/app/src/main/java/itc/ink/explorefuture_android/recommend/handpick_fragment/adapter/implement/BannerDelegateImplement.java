package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.implement;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_banner.BannerDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class BannerDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
    private Context mContext;

    @Override
    public void handleTransaction(Context mContext, HandPickWrapperAdapter.WrapperVH mHolder, Object mData) {
        this.mContext = mContext;

        mHolder.handpickBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mHolder.handpickBanner.setImageLoader(new MyLoader());
        mHolder.handpickBanner.setImages((ArrayList<BannerDataMode>) mData);
        mHolder.handpickBanner.setBannerAnimation(Transformer.Default);
        mHolder.handpickBanner.isAutoPlay(true);
        mHolder.handpickBanner.setIndicatorGravity(BannerConfig.RIGHT).start();
        mHolder.handpickBanner.setOnBannerListener(new ChoicenessBannerClickListener());
    }

    class ChoicenessBannerClickListener implements OnBannerListener {

        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(mContext, "HandpickBanner" + position + "被点击", Toast.LENGTH_SHORT).show();
        }


    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerDataMode bannerData=(BannerDataMode)path;

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(bannerData.getImage_update_datetime()).getObject());

            Glide.with(context).load(bannerData.getImageurl()).apply(options).into(imageView);
        }
    }
}
