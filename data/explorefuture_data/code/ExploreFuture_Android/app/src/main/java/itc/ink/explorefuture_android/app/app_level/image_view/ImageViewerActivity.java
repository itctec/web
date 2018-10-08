package itc.ink.explorefuture_android.app.app_level.image_view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itc.ink.explorefuture_android.R;

/**
 * Created by yangwenjiang on 2018/10/8.
 */

public class ImageViewerActivity extends Activity {
    public static final int MSG_FINISH_ACTIVITY=0x01;
    public static final String KEY_IMAGE_URL_LIST="content_image_urls";
    public static final String KEY_CONTENT_TEXT="content_text";
    public static final String KEY_CURRENT_IMAGE_POSITION="position";
    public static Handler mHandler;

    private ViewPager imageViewerViewPager;
    private ArrayList<String> contentImageUrlList;
    private ImageView closeBtn;
    private TextView countText;
    private TextView contentTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MSG_FINISH_ACTIVITY) {
                    finish();
                }
            }
        };

        Intent intent=getIntent();
        contentImageUrlList=intent.getStringArrayListExtra(KEY_IMAGE_URL_LIST);
        int position=intent.getIntExtra(KEY_CURRENT_IMAGE_POSITION,0);
        String contentText=intent.getStringExtra(KEY_CONTENT_TEXT);

        setContentView(R.layout.app_level_image_view_activity);

        imageViewerViewPager=findViewById(R.id.image_Viewer_ViewPager);
        imageViewerViewPager.setAdapter(new ImageViewerActivityViewPagerAdapter(ImageViewerActivity.this,contentImageUrlList));
        imageViewerViewPager.setCurrentItem(position);
        imageViewerViewPager.addOnPageChangeListener(new ImageViewerViewPagerChangeListener());

        closeBtn=findViewById(R.id.image_Viewer_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());

        countText=findViewById(R.id.image_Viewer_Count_Text);
        countText.setText((position+1)+"/"+contentImageUrlList.size());

        contentTextView=findViewById(R.id.image_Viewer_Content_Text);
        contentTextView.setText(contentText);
    }

    class ImageViewerViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            countText.setText((i+1)+"/"+contentImageUrlList.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class CloseBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }


}

