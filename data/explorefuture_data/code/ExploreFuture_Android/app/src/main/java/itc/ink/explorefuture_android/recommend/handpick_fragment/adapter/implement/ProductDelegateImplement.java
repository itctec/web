package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.implement;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_product.ProductDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ProductDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
    private Context mContext;
    @Override
    public void handleTransaction(Context mContext, HandPickWrapperAdapter.WrapperVH mHolder, Object mData) {
        this.mContext=mContext;

        final ArrayList<ProductDataMode> productData=(ArrayList<ProductDataMode>)mData;

        RequestOptions options = new RequestOptions();

        mHolder.productRecommendTopTextView.setOnClickListener(new ProductRecommendTopTextViewClickListener());
        mHolder.productLevelALayout.setOnClickListener(new ProductLevelALayoutClickListener());
        mHolder.productLevelATitleTextView.setText(productData.get(0).getTitle());
        mHolder.productLevelASummaryTextView.setText(productData.get(0).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(0).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(0).getImageurl()).apply(options).into(mHolder.productLevelAImageImageView);

        mHolder.productSubjectSortOneLayout.setOnClickListener(new ProductSubjectSortOneLayoutClickListener());
        mHolder.productSubjectSortOneTitleTextView.setText(productData.get(1).getTitle());
        mHolder.productSubjectSortOneSummaryTextView.setText(productData.get(1).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(1).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(1).getImageurl_left()).apply(options).into(mHolder.productSubjectSortOneLeftImageImageView);
        options.signature(new ObjectKeyCanNull(productData.get(1).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(1).getImageurl_right()).apply(options).into(mHolder.productSubjectSortOneRightImageImageView);

        mHolder.productSubjectSortTwoLayout.setOnClickListener(new ProductSubjectSortTwoLayoutClickListener());
        mHolder.productSubjectSortTwoTitleTextView.setText(productData.get(2).getTitle());
        mHolder.productSubjectSortTwoSummaryTextView.setText(productData.get(2).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(2).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(2).getImageurl_left()).apply(options).into(mHolder.productSubjectSortTwoLeftImageImageView);
        options.signature(new ObjectKeyCanNull(productData.get(2).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(2).getImageurl_right()).apply(options).into(mHolder.productSubjectSortTwoRightImageImageView);

        mHolder.productSubjectBannerOneLayout.setOnClickListener(new ProductSubjectBannerOneLayoutClickListener());
        mHolder.productSubjectBannerOneTitleTextView.setText(productData.get(3).getTitle());
        mHolder.productSubjectBannerOneBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mHolder.productSubjectBannerOneBanner.setImageLoader(new MyLoader());
        List<Map<String,String>> productSubjectBannerOneImageList=new ArrayList<Map<String,String>>();
        Map<String,String> productSubjectBannerOneImageLeftMap=new HashMap<>();
        productSubjectBannerOneImageLeftMap.put("image_url",productData.get(3).getImageurl_left());
        productSubjectBannerOneImageLeftMap.put("image_update_datetime",productData.get(3).getImage_left_update_datetime());
        Map<String,String> productSubjectBannerOneImageRightMap=new HashMap<>();
        productSubjectBannerOneImageRightMap.put("image_url",productData.get(3).getImageurl_right());
        productSubjectBannerOneImageRightMap.put("image_update_datetime",productData.get(3).getImage_right_update_datetime());
        productSubjectBannerOneImageList.add(productSubjectBannerOneImageLeftMap);
        productSubjectBannerOneImageList.add(productSubjectBannerOneImageRightMap);
        mHolder.productSubjectBannerOneBanner.setImages(productSubjectBannerOneImageList);
        mHolder.productSubjectBannerOneBanner.setBannerAnimation(Transformer.Default);
        mHolder.productSubjectBannerOneBanner.isAutoPlay(true).start();
        mHolder.productSubjectBannerOneBanner.setOnBannerListener(new ProductSubjectBannerOneBannerBannerClickListener());

        mHolder.productSubjectBannerTwoLayout.setOnClickListener(new ProductSubjectBannerTwoLayoutClickListener());
        mHolder.productSubjectBannerTwoTitleTextView.setText(productData.get(4).getTitle());
        mHolder.productSubjectBannerTwoBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mHolder.productSubjectBannerTwoBanner.setImageLoader(new MyLoader());
        List<Map<String,String>> productSubjectBannerTwoImageList=new ArrayList<Map<String,String>>();
        Map<String,String> productSubjectBannerTwoImageLeftMap=new HashMap<>();
        productSubjectBannerTwoImageLeftMap.put("image_url",productData.get(4).getImageurl_left());
        productSubjectBannerTwoImageLeftMap.put("image_update_datetime",productData.get(4).getImage_left_update_datetime());
        Map<String,String> productSubjectBannerTwoImageRightMap=new HashMap<>();
        productSubjectBannerTwoImageRightMap.put("image_url",productData.get(4).getImageurl_right());
        productSubjectBannerTwoImageRightMap.put("image_update_datetime",productData.get(4).getImage_right_update_datetime());
        productSubjectBannerTwoImageList.add(productSubjectBannerTwoImageLeftMap);
        productSubjectBannerTwoImageList.add(productSubjectBannerTwoImageRightMap);
        mHolder.productSubjectBannerTwoBanner.setImages(productSubjectBannerTwoImageList);
        mHolder.productSubjectBannerTwoBanner.setBannerAnimation(Transformer.Default);
        mHolder.productSubjectBannerTwoBanner.isAutoPlay(true).start();
        mHolder.productSubjectBannerTwoBanner.setOnBannerListener(new ProductSubjectBannerTwoBannerBannerClickListener());

        mHolder.productLevelBOneLayout.setOnClickListener(new ProductLevelBOneLayoutClickListener());
        mHolder.productLevelBOneTitleTextView.setText(productData.get(5).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(5).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(5).getImageurl()).apply(options).into(mHolder.productLevelBOneImageImageView);

        mHolder.productLevelBTwoLayout.setOnClickListener(new ProductLevelBTwoLayoutClickListener());
        mHolder.productLevelBTwoTitleTextView.setText(productData.get(6).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(6).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(6).getImageurl()).apply(options).into(mHolder.productLevelBTwoImageImageView);

        mHolder.productLevelBThreeLayout.setOnClickListener(new ProductLevelBThreeLayoutClickListener());
        mHolder.productLevelBThreeTitleTextView.setText(productData.get(7).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(7).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(7).getImageurl()).apply(options).into(mHolder.productLevelBThreeImageImageView);

        mHolder.productLevelBFourLayout.setOnClickListener(new ProductLevelBFourLayoutClickListener());
        mHolder.productLevelBFourTitleTextView.setText(productData.get(8).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(8).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(8).getImageurl()).apply(options).into(mHolder.productLevelBFourImageImageView);
    }

    class ProductRecommendTopTextViewClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"产品推荐榜单被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductLevelALayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"A级产品被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectSortOneLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"产品专题1被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectSortTwoLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"产品专题2被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectBannerOneLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"产品专题Banner1被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectBannerTwoLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"产品专题Banner2被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductLevelBOneLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"B级产品1被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductLevelBTwoLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"B级产品2被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductLevelBThreeLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"B级产品3被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductLevelBFourLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext,"B级产品4被点击",Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectBannerOneBannerBannerClickListener implements OnBannerListener {

        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(mContext, "ProductBannerOne" + position + "被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSubjectBannerTwoBannerBannerClickListener implements OnBannerListener {

        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(mContext, "ProductBannerTwo" + position + "被点击", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            Map<String,String> productSubjectBannerOneImageMap=(Map<String,String>)path;

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(productSubjectBannerOneImageMap.get("image_update_datetime")).getObject());

            Glide.with(context).load(productSubjectBannerOneImageMap.get("image_url")).apply(options).into(imageView);
        }
    }
}
