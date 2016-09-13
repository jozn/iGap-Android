package com.iGap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.iGap.adapter.G;
import com.iGap.instruments.Utils;


/**
 * 
 * internal webView for show web site
 *
 */

public class WebBrowser extends Activity {

    private Button      btnMenu;
    private Button      btnShare;
    private Button      btnClose;

    private TextView    txtTitleSite, txtLinkSite;
    private ProgressBar progressBar;
    private WebView     webView;
    private String      title = "", link = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.checkLanguage(this);
        setContentView(R.layout.web_browser);

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");

        init();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.checkLanguage(this);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        Utils.checkLanguage(this);
        super.onResume();
    }


    private void init() {

        btnMenu = (Button) findViewById(R.id.wb_btn_menu);
        btnMenu.setTypeface(G.fontAwesome);
        btnMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openLayoutMenu();
            }
        });

        btnShare = (Button) findViewById(R.id.wb_btn_share);
        btnShare.setTypeface(G.fontAwesome);
        btnShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                share();
            }
        });

        btnClose = (Button) findViewById(R.id.wb_btn_close);
        btnClose.setTypeface(G.fontAwesome);
        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                close();
            }
        });

        txtTitleSite = (TextView) findViewById(R.id.wb_txt_title_site);
        txtLinkSite = (TextView) findViewById(R.id.wb_txt_link_site);

        if (link != null) {
            txtLinkSite.setText(link);
        }

        progressBar = (ProgressBar) findViewById(R.id.wb_progressBar1);
        progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);

        webView = (WebView) findViewById(R.id.wb_webView1);
        if (link != null) {
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.loadUrl(link);
        }
        repeat();

        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress >= 99) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                txtTitleSite.setText(view.getTitle());
            }
        });
    }


    private void repeat() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                title = webView.getTitle();
                if (title != null) {
                    if ( !title.equals("")) {
                        txtTitleSite.setText(webView.getTitle());
                        return;
                    }
                }

                repeat();
            }
        }, 500);
    }


    private void openLayoutMenu() {
        Intent intent = new Intent(WebBrowser.this, WebBrowser.class);
        intent.putExtra("link", link);
        startActivity(intent);
        finish();
    }


    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private void close() {
        finish();
    }

}
