package itc.ink.explorefuture_android.common_unit.content_details;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import itc.ink.explorefuture_android.R;
import itc.ink.explorefuture_android.app.activity.MainActivity;
import itc.ink.explorefuture_android.app.utils.StatusBarUtil;
import itc.ink.explorefuture_android.common_unit.common_dialog.CommonDialog;
import itc.ink.explorefuture_android.mind.edit_activity.MindEditActivity;

/**
 * Created by yangwenjiang on 2018/10/29.
 */

public class ContentDetailsActivity extends Activity {
    private ImageView backBtn;
    private WebView contentWebView;
    private ProgressBar contentLoadingProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StatusBar Style
        StatusBarUtil.setStatusBarFullTransparent(this);
        //StatusBar Text And Icon Style
        StatusBarUtil.setAndroidNativeLightStatusBar(this, true);

        Intent intent = getIntent();
        String content_id = intent.getStringExtra("content_id");
        String content_url = "";
        if (content_id != null && !content_id.isEmpty()) {
            switch (content_id) {
                case "a_01_01_s_00001":
                    content_url = "http://m.uqlsi.top/h.3jIUEu6";
                    break;
                case "a_01_01_s_00002":
                    content_url = "http://m.uqlsi.top/h.3jIUEu6";
                    break;
                case "a_01_01_s_00003":
                    content_url = "http://m.uqlsi.top/h.3jIUEu6";
                    break;
                case "a_01_01_s_00004":
                    content_url = "http://m.uqlsi.top/h.3jIUEu6";
                    break;
                default:
                    content_url = "http://www.itc.ink";
            }

        }

        setContentView(R.layout.common_unit_content_details_activity);

        backBtn=findViewById(R.id.content_Details_Top_Navigation_Back_Btn);
        backBtn.setOnClickListener(new BackBtnClickListener());

        contentLoadingProgressBar = findViewById(R.id.content_Details_Loading_ProgressBar);

        contentWebView = findViewById(R.id.content_Details_WebView);
        contentWebView.loadUrl(content_url);
        contentWebView.setWebChromeClient(webChromeClient);
        contentWebView.setWebViewClient(webViewClient);

        WebSettings webSettings = contentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentWebView.destroy();
        contentWebView = null;
    }

    class BackBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            contentLoadingProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            contentLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        boolean confirmResult = false;

        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {

            new CommonDialog(ContentDetailsActivity.this, message, new CommonDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    confirmResult = confirm;
                    dialog.dismiss();
                }
            }).setTitle(getString(R.string.mind_edit_release_dialog_title_text))
                    .setNegativeButton(getString(R.string.dialog_negative_btn_text))
                    .setPositiveButton(getString(R.string.mind_edit_release_dialog_positive_btn_text))
                    .show();

            if (confirmResult) {
                result.confirm();
            }
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            contentLoadingProgressBar.setProgress(newProgress);
        }
    };


}
