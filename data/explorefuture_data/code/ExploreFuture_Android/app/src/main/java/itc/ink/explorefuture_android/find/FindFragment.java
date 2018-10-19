package itc.ink.explorefuture_android.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.find.mode.DataLoad;
import itc.ink.explorefuture_android.find.mode.mode_banner.BannerDataMode;


/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class FindFragment extends Fragment {
    private DataLoad mDataLoad;
    private ArrayList<BannerDataMode> mBannerListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataLoad = new DataLoad();
        //Judge Data Prepare
        if (mDataLoad.outService.prepareData()) {
            mBannerListData = (ArrayList<BannerDataMode>) mDataLoad.outService.loadBannerData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.find_fragment,container,false);
        Banner findBanner=rootView.findViewById(R.id.find_Banner);
        if(mBannerListData.size() >= 0){
            findBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            findBanner.setImageLoader(new MyLoader());
            findBanner.setImages(mBannerListData);
            findBanner.setBannerAnimation(Transformer.Default);
            findBanner.isAutoPlay(true);
            findBanner.setIndicatorGravity(BannerConfig.CENTER).start();
            findBanner.setOnBannerListener(new FindBannerListener());
        }

        
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
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

    class FindBannerListener implements OnBannerListener{
        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(getContext(), "FindBanner" + position + "被点击", Toast.LENGTH_SHORT).show();
        }
    }
}
