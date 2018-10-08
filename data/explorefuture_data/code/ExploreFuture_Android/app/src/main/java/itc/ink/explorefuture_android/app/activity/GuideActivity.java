package itc.ink.explorefuture_android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.adapter.GuideActivityViewPagerAdapter;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SharedPreferenceUtil;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;

/**
 * Created by yangwenjiang on 2018/9/25.
 */

public class GuideActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "GuideActivity";
    private ViewPager appGuideViewPager;
    private View appGuideViewPagerIndicatorOne;
    private View appGuideViewPagerIndicatorTwo;
    private View appGuideViewPagerIndicatorFive;
    private Button startFutureBtn;

    private ArrayList<Integer> guideImageResourceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.app_guide_activity);

        guideImageResourceList=initData();

        appGuideViewPager = findViewById(R.id.app_Guide_ViewPager);
        appGuideViewPager.setAdapter(new GuideActivityViewPagerAdapter(GuideActivity.this,guideImageResourceList));
        appGuideViewPager.addOnPageChangeListener(new AppGuideViewPagerChangeListener());

        appGuideViewPagerIndicatorOne=findViewById(R.id.app_Guide_ViewPager_Indicator_One);
        appGuideViewPagerIndicatorTwo=findViewById(R.id.app_Guide_ViewPager_Indicator_Two);
        appGuideViewPagerIndicatorFive=findViewById(R.id.app_Guide_ViewPager_Indicator_Five);

        startFutureBtn=findViewById(R.id.app_Guide_Start_Future_Btn);
        startFutureBtn.setOnClickListener(new StartFutureBtnClickListener());
    }

    private ArrayList<Integer> initData(){
        ArrayList<Integer> guideImageResourceList=new ArrayList<>();
        guideImageResourceList.add(R.drawable.app_guide_item_one);
        guideImageResourceList.add(R.drawable.app_guide_item_two);
        guideImageResourceList.add(R.drawable.app_guide_item_three);
        guideImageResourceList.add(R.drawable.app_guide_item_four);

        return guideImageResourceList;
    }

    class AppGuideViewPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

            float stepLength=(appGuideViewPagerIndicatorTwo.getX()+appGuideViewPagerIndicatorTwo.getWidth())
                    -(appGuideViewPagerIndicatorOne.getX()+appGuideViewPagerIndicatorOne.getWidth());

            ConstraintLayout.LayoutParams layoutParams=(ConstraintLayout.LayoutParams)appGuideViewPagerIndicatorFive.getLayoutParams();

            int width=(int)(appGuideViewPagerIndicatorOne.getWidth()+i*stepLength+v*stepLength);
            if(width!=0){
                layoutParams.width=width;
            }
            appGuideViewPagerIndicatorFive.setLayoutParams(layoutParams);

            if(i>=2){
                startFutureBtn.setAlpha((i-2)+v);
            }
        }

        @Override
        public void onPageSelected(int i) {
            if(i==3){
                startFutureBtn.setVisibility(View.VISIBLE);
            }else{
                startFutureBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class StartFutureBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            SharedPreferenceUtil.putBoolean("app_first_launch",false);

            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }
    }



}
