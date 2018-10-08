package itc.ink.explorefuture_android.app.app_level.video_view;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;

/**
 * Created by yangwenjiang on 2018/10/8.
 */

public class VideoViewerActivity extends Activity {
    public static final String KEY_VIDEO_URL = "content_video_url";
    public static final String KEY_CONTENT_TEXT = "content_text";

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

        setContentView(R.layout.app_level_video_view_activity);

        videoViewerVideoView = findViewById(R.id.video_Viewer_VideoView);
        videoViewerVideoView.setVideoPath(videoUrl);
        videoViewerVideoView.requestFocus();
        videoViewerVideoView.start();
        videoViewerVideoView.setOnPreparedListener(new VideoViewerVideoViewPreparedListener());
        videoViewerVideoView.setOnClickListener(new VideoViewerVideoViewClickListener());

        loadingImage = findViewById(R.id.video_Viewer_Loading_Image);
        Glide.with(VideoViewerActivity.this).load(R.drawable.video_loading_one).into(loadingImage);

        closeBtn = findViewById(R.id.video_Viewer_Close_Btn);
        closeBtn.setOnClickListener(new CloseBtnClickListener());

        contentTextView = findViewById(R.id.video_Viewer_Content_Text);
        contentTextView.setText(contentText);
    }

    class VideoViewerVideoViewPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //Toast.makeText(VideoViewerActivity.this,"视频加载完毕",Toast.LENGTH_SHORT).show();
            loadingImage.setVisibility(View.GONE);
        }
    }

    class VideoViewerVideoViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Toast.makeText(VideoViewerActivity.this,"视频被点击",Toast.LENGTH_SHORT).show();
            if (videoViewerVideoView.isPlaying()) {
                videoViewerVideoView.pause();
            } else {
                videoViewerVideoView.start();
            }
        }
    }

    class CloseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
