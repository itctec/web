package itc.ink.explorefuture_android.common_unit.topic_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.topic_details.adapter.TopicDetailsWrapAdapter;
import itc.ink.explorefuture_android.recommend.mind_fragment.mode.mode_topic.TopicListDataMode;

/**
 * Created by yangwenjiang on 2018/10/31.
 */

public class TopicDetailsActivity extends Activity {
    public static final String KEY_TOPIC_ITEM="topic_item";

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager contentRvLayoutManager;
    private TopicDetailsWrapAdapter topicDetailsWrapAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        TopicListDataMode topicItem=(TopicListDataMode)intent.getSerializableExtra(KEY_TOPIC_ITEM);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.common_unit_topic_details_activity);

        ImageView backBtn=findViewById(R.id.topic_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentRecyclerView=findViewById(R.id.topic_Details_RecyclerView);
        topicDetailsWrapAdapter = new TopicDetailsWrapAdapter(this, topicItem);
        contentRecyclerView.setAdapter(topicDetailsWrapAdapter);
        contentRvLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setLayoutManager(contentRvLayoutManager);
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

}
