package com.weavedin.mplayer.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weavedin.mplayer.R;
import com.weavedin.mplayer.ui.search.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.et_splash)
    EditText et_splash;
    @BindView(R.id.tv_splash)
    TextView tv_splash;
    private static int SPLASH_TIME_OUT = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setupViews();
    }

    private void setupViews() {
        et_splash.setFocusable(false);
        et_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handler!=null){
                    handler.removeCallbacksAndMessages(null);
                }
                openSearchActivity();
            }
        });
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              et_splash.setVisibility(View.VISIBLE);
              tv_splash.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);
    }

    public void openSearchActivity() {
        startActivity(new Intent(SplashActivity.this, SearchActivity.class));
        finish();
    }
}
