package itc.ink.explorefuture_android.common_unit.topic_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.mind_details.MindDetailsActivity;
import itc.ink.explorefuture_android.common_unit.topic_details.adapter.TopicDetailsWrapAdapter;
import itc.ink.explorefuture_android.common_unit.topic_details.adapter.ViewPointDetailsWrapAdapter;
import itc.ink.explorefuture_android.common_unit.topic_details.mode.ViewPointDataMode;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class ViewPointDetailsActivity extends Activity {
    public static final String KEY_TOPIC_ITEM="topic_item";
    public static final String KEY_VIEW_POINT_ITEM="view_point_item";

    private View topNavigationBarBg;
    private TextView topNavigationBarTitle;
    private View topNavigationBarBottomLine;

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private ViewPointDetailsWrapAdapter viewPointDetailsWrapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        TopicListDataMode topicItem=(TopicListDataMode)intent.getSerializableExtra(KEY_TOPIC_ITEM);
        ViewPointDataMode viewPointItem=(ViewPointDataMode)intent.getSerializableExtra(KEY_VIEW_POINT_ITEM);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_view_point_details_activity);

        topNavigationBarBg = findViewById(R.id.view_Point_Details_Top_Navigation_Bg);
        topNavigationBarBg.setOnClickListener(null);
        topNavigationBarTitle = findViewById(R.id.view_Point_Details_Top_Navigation_Title_Text);
        topNavigationBarBottomLine = findViewById(R.id.view_Point_Details_Top_Navigation_BottomLine);

        ImageView backBtn=findViewById(R.id.view_Point_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentRecyclerView=findViewById(R.id.view_Point_Details_RecyclerView);
        viewPointDetailsWrapAdapter = new ViewPointDetailsWrapAdapter(this, topicItem,viewPointItem);
        contentRecyclerView.setAdapter(viewPointDetailsWrapAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
        contentRecyclerView.addOnScrollListener(new ContentRecyclerViewScrollListener());

        ConstraintLayout acceptBtn=findViewById(R.id.view_Point_Details_Bottom_Bar_Accept_Btn);
        acceptBtn.setTag(viewPointItem.getId());
        acceptBtn.setOnClickListener(new AcceptBtnClickListener());
        ConstraintLayout opposeBtn=findViewById(R.id.view_Point_Details_Bottom_Bar_Oppose_Btn);
        opposeBtn.setTag(viewPointItem.getId());
        opposeBtn.setOnClickListener(new OpposeBtnClickListener());
        ConstraintLayout commentBtn=findViewById(R.id.view_Point_Details_Bottom_Bar_Comment_Btn);
        commentBtn.setTag(viewPointItem.getId());
        commentBtn.setOnClickListener(new CommentBtnClickListener());

    }

    class BackBtnClickListener implements View.OnClickListener{
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
                    alpha = alpha + (float) dy / 300;
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

    class AcceptBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String id_Str=(String)view.getTag();
            Toast.makeText(ViewPointDetailsActivity.this,id_Str+"被认可",Toast.LENGTH_SHORT).show();
        }
    }

    class OpposeBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String id_Str=(String)view.getTag();
            Toast.makeText(ViewPointDetailsActivity.this,id_Str+"被反对",Toast.LENGTH_SHORT).show();
        }
    }

    class CommentBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String id_Str=(String)view.getTag();
            Toast.makeText(ViewPointDetailsActivity.this,id_Str+"被评论",Toast.LENGTH_SHORT).show();
        }
    }



}
