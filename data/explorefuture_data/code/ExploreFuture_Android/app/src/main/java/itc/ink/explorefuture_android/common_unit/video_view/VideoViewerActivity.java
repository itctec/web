package itc.ink.explorefuture_android.common_unit.video_view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;

/**
 * Created by yangwenjiang on 2018/10/8.
 */

public class VideoViewerActivity extends Activity {
    private final String LOG_TAG = ExploreFutureApplication.LOG_TAG + "ViewerActivity";
    public static final String KEY_VIDEO_URL = "content_video_url";
    public static final String KEY_CONTENT_TEXT = "content_text";
    private final int MSG_LOADED_FIRST_FRAME = 0x01;
    private Bitmap firstFrame;

    private VideoView videoViewerVideoView;
    private ImageView loadingImage;
    private ImageView closeBtn;
    private TextView contentTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, false);

        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra(KEY_VIDEO_URL);
        String contentText = intent.getStringExtra(KEY_CONTENT_TEXT);

        setContentView(R.layout.common_unit_video_view_activity);

        videoViewerVideoView = findViewById(R.id.video_Viewer_VideoView);
        videoViewerVideoView.setVideoPath(videoUrl);
        videoViewerVideoView.requestFocus();
        videoViewerVideoView.start();
        videoViewerVideoView.setOnTouchListener(new VideoViewerVideoViewTouchListener());
        videoViewerVideoView.setOnPreparedListener(new VideoViewerVideoViewPreparedListener());

        loadingImage = findViewById(R.id.video_Viewer_Loading_Image);

        closeBtn = findViewById(R.id.video_Viewer_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());

        contentTextView = findViewById(R.id.video_Viewer_Content_Text);
        contentTextView.setText(contentText);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Glide.with(VideoViewerActivity.this).load(R.drawable.video_loading).into(loadingImage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingImage.setVisibility(View.GONE);
    }

    class VideoViewerVideoViewPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //Toast.makeText(VideoViewerActivity.this,"视频加载完毕",Toast.LENGTH_SHORT).show();
            loadingImage.setVisibility(View.GONE);
        }
    }

    class VideoViewerVideoViewTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d(LOG_TAG,"Action->"+motionEvent.getAction());
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                Log.d(LOG_TAG,"Action->"+motionEvent.getAction());
                if (videoViewerVideoView.isPlaying()) {
                    videoViewerVideoView.pause();
                } else {
                    videoViewerVideoView.start();
                }
            }
            return false;
        }
    }

    class CloseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
