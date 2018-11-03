package itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.implement;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.common_unit.content_details.ContentDetailsActivity;
import itc.ink.explorefuture_android.common_unit.content_list.ContentListActivity;
import itc.ink.explorefuture_android.recommend.handpick_fragment.adapter.HandPickWrapperAdapter;
import itc.ink.explorefuture_android.recommend.handpick_fragment.mode.mode_product.ProductDataMode;

/**
 * Created by yangwenjiang on 2018/9/14.
 */

public class ProductDelegateImplement implements HandPickWrapperAdapter.DelegateInterface {
    private WeakReference<Context> mWeakContextReference;

    private Context getContext() {
        if (mWeakContextReference.get() != null) {
            return mWeakContextReference.get();
        }
        return null;
    }

    @Override
    public void handleTransaction(Context mContext, HandPickWrapperAdapter.WrapperVH mHolder, Object mData) {
        this.mWeakContextReference = new WeakReference<>(mContext);

        final ArrayList<ProductDataMode> productData = (ArrayList<ProductDataMode>) mData;

        RequestOptions options = new RequestOptions();

        mHolder.productRecommendTopTextView.setOnClickListener(new ProductRecommendTopTextViewClickListener());

        ProductClickListener productClickListener = new ProductClickListener();
        mHolder.productLevelALayout.setTag(productData.get(0).getId());
        mHolder.productLevelALayout.setOnClickListener(productClickListener);
        mHolder.productLevelATitleTextView.setText(productData.get(0).getTitle());
        mHolder.productLevelASummaryTextView.setText(productData.get(0).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(0).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(0).getImageurl()).apply(options).into(mHolder.productLevelAImageImageView);

        ProductSortClickListener productSortClickListener = new ProductSortClickListener();
        mHolder.productSubjectSortOneLayout.setTag(productData.get(1));
        mHolder.productSubjectSortOneLayout.setOnClickListener(productSortClickListener);
        mHolder.productSubjectSortOneTitleTextView.setText(productData.get(1).getTitle());
        mHolder.productSubjectSortOneSummaryTextView.setText(productData.get(1).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(1).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(1).getImageurl_left()).apply(options).into(mHolder.productSubjectSortOneLeftImageImageView);
        options.signature(new ObjectKeyCanNull(productData.get(1).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(1).getImageurl_right()).apply(options).into(mHolder.productSubjectSortOneRightImageImageView);

        mHolder.productSubjectSortTwoLayout.setTag(productData.get(2));
        mHolder.productSubjectSortTwoLayout.setOnClickListener(productSortClickListener);
        mHolder.productSubjectSortTwoTitleTextView.setText(productData.get(2).getTitle());
        mHolder.productSubjectSortTwoSummaryTextView.setText(productData.get(2).getSummary());
        options.signature(new ObjectKeyCanNull(productData.get(2).getImage_left_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(2).getImageurl_left()).apply(options).into(mHolder.productSubjectSortTwoLeftImageImageView);
        options.signature(new ObjectKeyCanNull(productData.get(2).getImage_right_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(2).getImageurl_right()).apply(options).into(mHolder.productSubjectSortTwoRightImageImageView);

        mHolder.productSubjectBannerOneTitleTextView.setText(productData.get(3).getTitle());
        mHolder.productSubjectBannerOneBanner.setOnBannerListener(new ProductBannerListener(productData.get(3)));
        mHolder.productSubjectBannerOneBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mHolder.productSubjectBannerOneBanner.setImageLoader(new MyLoader());
        List<Map<String, String>> productSubjectBannerOneImageList = new ArrayList<Map<String, String>>();
        Map<String, String> productSubjectBannerOneImageLeftMap = new HashMap<>();
        productSubjectBannerOneImageLeftMap.put("image_url", productData.get(3).getImageurl_left());
        productSubjectBannerOneImageLeftMap.put("image_update_datetime", productData.get(3).getImage_left_update_datetime());
        Map<String, String> productSubjectBannerOneImageRightMap = new HashMap<>();
        productSubjectBannerOneImageRightMap.put("image_url", productData.get(3).getImageurl_right());
        productSubjectBannerOneImageRightMap.put("image_update_datetime", productData.get(3).getImage_right_update_datetime());
        productSubjectBannerOneImageList.add(productSubjectBannerOneImageLeftMap);
        productSubjectBannerOneImageList.add(productSubjectBannerOneImageRightMap);
        mHolder.productSubjectBannerOneBanner.setImages(productSubjectBannerOneImageList);
        mHolder.productSubjectBannerOneBanner.setBannerAnimation(Transformer.Default);
        mHolder.productSubjectBannerOneBanner.isAutoPlay(true).start();

        mHolder.productSubjectBannerTwoTitleTextView.setText(productData.get(4).getTitle());
        mHolder.productSubjectBannerTwoBanner.setOnBannerListener(new ProductBannerListener(productData.get(4)));
        mHolder.productSubjectBannerTwoBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mHolder.productSubjectBannerTwoBanner.setImageLoader(new MyLoader());
        List<Map<String, String>> productSubjectBannerTwoImageList = new ArrayList<Map<String, String>>();
        Map<String, String> productSubjectBannerTwoImageLeftMap = new HashMap<>();
        productSubjectBannerTwoImageLeftMap.put("image_url", productData.get(4).getImageurl_left());
        productSubjectBannerTwoImageLeftMap.put("image_update_datetime", productData.get(4).getImage_left_update_datetime());
        Map<String, String> productSubjectBannerTwoImageRightMap = new HashMap<>();
        productSubjectBannerTwoImageRightMap.put("image_url", productData.get(4).getImageurl_right());
        productSubjectBannerTwoImageRightMap.put("image_update_datetime", productData.get(4).getImage_right_update_datetime());
        productSubjectBannerTwoImageList.add(productSubjectBannerTwoImageLeftMap);
        productSubjectBannerTwoImageList.add(productSubjectBannerTwoImageRightMap);
        mHolder.productSubjectBannerTwoBanner.setImages(productSubjectBannerTwoImageList);
        mHolder.productSubjectBannerTwoBanner.setBannerAnimation(Transformer.Default);
        mHolder.productSubjectBannerTwoBanner.isAutoPlay(true).start();

        mHolder.productLevelBOneLayout.setTag(productData.get(5).getId());
        mHolder.productLevelBOneLayout.setOnClickListener(productClickListener);
        mHolder.productLevelBOneTitleTextView.setText(productData.get(5).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(5).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(5).getImageurl()).apply(options).into(mHolder.productLevelBOneImageImageView);

        mHolder.productLevelBTwoLayout.setTag(productData.get(6).getId());
        mHolder.productLevelBTwoLayout.setOnClickListener(productClickListener);
        mHolder.productLevelBTwoTitleTextView.setText(productData.get(6).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(6).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(6).getImageurl()).apply(options).into(mHolder.productLevelBTwoImageImageView);

        mHolder.productLevelBThreeLayout.setTag(productData.get(7).getId());
        mHolder.productLevelBThreeLayout.setOnClickListener(productClickListener);
        mHolder.productLevelBThreeTitleTextView.setText(productData.get(7).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(7).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(7).getImageurl()).apply(options).into(mHolder.productLevelBThreeImageImageView);

        mHolder.productLevelBFourLayout.setTag(productData.get(8).getId());
        mHolder.productLevelBFourLayout.setOnClickListener(productClickListener);
        mHolder.productLevelBFourTitleTextView.setText(productData.get(8).getTitle());
        options.signature(new ObjectKeyCanNull(productData.get(8).getImage_update_datetime()).getObject());
        Glide.with(mContext).load(productData.get(8).getImageurl()).apply(options).into(mHolder.productLevelBFourImageImageView);
    }

    class ProductRecommendTopTextViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "产品推荐榜单被点击", Toast.LENGTH_SHORT).show();
        }
    }

    class ProductSortClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ProductDataMode productDataItem= (ProductDataMode) view.getTag();
            Intent intent =new Intent(getContext(), ContentListActivity.class);
            intent.putExtra(ContentListActivity.KEY_SORT_ID,productDataItem.getId());
            intent.putExtra(ContentListActivity.KEY_SORT_TITLE,productDataItem.getTitle());
            getContext().startActivity(intent);
        }
    }

    class ProductBannerListener implements OnBannerListener{
        ProductDataMode productDataItem;

        public ProductBannerListener(ProductDataMode productDataItem) {
            this.productDataItem = productDataItem;
        }

        @Override
        public void OnBannerClick(int position) {
            Intent intent = new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID, productDataItem.getId());
            getContext().startActivity(intent);
        }
    }

    class ProductClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String content_id = (String) view.getTag();
            Intent intent = new Intent(getContext(), ContentDetailsActivity.class);
            intent.putExtra(ContentDetailsActivity.KEY_CONTENT_ID, content_id);
            getContext().startActivity(intent);
        }
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            Map<String, String> productSubjectBannerOneImageMap = (Map<String, String>) path;

            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKeyCanNull(productSubjectBannerOneImageMap.get("image_update_datetime")).getObject());

            Glide.with(context).load(productSubjectBannerOneImageMap.get("image_url")).apply(options).into(imageView);
        }
    }
}
