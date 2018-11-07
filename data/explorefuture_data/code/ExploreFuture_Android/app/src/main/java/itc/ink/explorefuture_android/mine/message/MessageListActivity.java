package itc.ink.explorefuture_android.mine.message;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.app_level.ObjectKeyCanNull;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.SQLiteDBHelper;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.login.LoginStateInstance;

/**
 * Created by yangwenjiang on 2018/10/29.
 */

public class MessageListActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "SettingsActivity";
    private ImageView backBtn;
    private ConstraintLayout acceptLayout;
    private ConstraintLayout commentLayout;
    private ConstraintLayout supportLayout;
    private ConstraintLayout sellLayout;
    private ConstraintLayout platformLayout;
    private ConstraintLayout logisticsLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        setContentView(R.layout.mine_message_list_activity);

        ItemLayoutClickListener itemLayoutClickListener=new ItemLayoutClickListener();
        backBtn=findViewById(R.id.message_List_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());
        acceptLayout=findViewById(R.id.message_List_Item_Accept_Layout);
        acceptLayout.setOnClickListener(itemLayoutClickListener);
        commentLayout=findViewById(R.id.message_List_Item_Comment_Layout);
        commentLayout.setOnClickListener(itemLayoutClickListener);
        supportLayout=findViewById(R.id.message_List_Item_Support_Layout);
        supportLayout.setOnClickListener(itemLayoutClickListener);
        sellLayout=findViewById(R.id.message_List_Item_Sell_Layout);
        sellLayout.setOnClickListener(itemLayoutClickListener);
        platformLayout=findViewById(R.id.message_List_Item_Platform_Layout);
        platformLayout.setOnClickListener(itemLayoutClickListener);
        logisticsLayout=findViewById(R.id.message_List_Item_Logistics_Layout);
        logisticsLayout.setOnClickListener(itemLayoutClickListener);

    }


    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    class ItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.message_List_Item_Accept_Layout:
                    Toast.makeText(MessageListActivity.this,"赞被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_List_Item_Comment_Layout:
                    Toast.makeText(MessageListActivity.this,"评论被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_List_Item_Support_Layout:
                    Toast.makeText(MessageListActivity.this,"打赏被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_List_Item_Sell_Layout:
                    Toast.makeText(MessageListActivity.this,"售出被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_List_Item_Platform_Layout:
                    Toast.makeText(MessageListActivity.this,"平台消息被点击",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.message_List_Item_Logistics_Layout:
                    Toast.makeText(MessageListActivity.this,"物流通知被点击",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }


}
