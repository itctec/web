package itc.ink.explorefuture_android.common_unit.user_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.user_details.adapter.UserDetailsWrapAdapter;
import itc.ink.explorefuture_android.common_unit.user_details.mode.SimpleUserInfoDataMode;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class UserDetailsActivity extends Activity {
    public static final String KEY_PERSON_INFO_ITEM="person_info_item";

    private View topNavigationBarBg;
    private TextView topNavigationBarTitle;
    private View topNavigationBarBottomLine;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private UserDetailsWrapAdapter userDetailsWrapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent=getIntent();
        SimpleUserInfoDataMode personInfoItem=(SimpleUserInfoDataMode)intent.getSerializableExtra(KEY_PERSON_INFO_ITEM);
        Toast.makeText(this, "ID:" + personInfoItem.getId(), Toast.LENGTH_SHORT).show();

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_user_details_activity);

        topNavigationBarBg = findViewById(R.id.user_Details_Top_Navigation_Bg);
        topNavigationBarBg.setOnClickListener(null);
        topNavigationBarTitle = findViewById(R.id.user_Details_Top_Navigation_Title_Text);
        topNavigationBarTitle.setText(String.format(getResources().getString(R.string.person_details_top_navigation_title_text), personInfoItem.getNickname()));
        topNavigationBarBottomLine = findViewById(R.id.user_Details_Top_Navigation_BottomLine);

        ImageView backBtn = findViewById(R.id.user_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentRecyclerView = findViewById(R.id.user_Details_RecyclerView);
        userDetailsWrapAdapter = new UserDetailsWrapAdapter(this, personInfoItem.getId());
        contentRecyclerView.setAdapter(userDetailsWrapAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        contentRecyclerView.addOnScrollListener(new ContentRecyclerViewScrollListener());

    }


    class BackBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class ContentRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        float alpha = 0.0f;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

            if (manager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0) {
                    alpha = alpha + (float) dy / 400;
                    if (alpha < 0) {
                        alpha = 0.0f;
                    }
                    if (alpha > 1) {
                        alpha = 1.0f;
                    }
                }else{
                    alpha = 1.0f;
                }
                topNavigationBarBg.setAlpha(alpha);
                topNavigationBarTitle.setAlpha(alpha);
                topNavigationBarBottomLine.setAlpha(alpha);
            }
        }
    }

}
