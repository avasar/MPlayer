package com.weavedin.mplayer.ui.player;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weavedin.mplayer.R;
import com.weavedin.mplayer.database.DBManager;
import com.weavedin.mplayer.ui.favorite.FavoriteActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    @BindView(R.id.toolbar_player)
    Toolbar toolbar_player;
    @BindView(R.id.seekbar_player)
    SeekBar seekbar_player;
    @BindView(R.id.ib_player_fav)
    ImageButton ib_player_fav;
    @BindView(R.id.ib_player_add2fav)
    ImageButton ib_player_add2fav;
    @BindView(R.id.ib_player_list)
    ImageButton ib_player_list;
    @BindView(R.id.ib_player_play_pause)
    ImageButton ib_player_play_pause;
    @BindView(R.id.rlv_player_play_pause)
    RelativeLayout rlv_player_play_pause;
    @BindView(R.id.tv_player_title)
    TextView tv_player_title;
    @BindView(R.id.tv_player_body)
    TextView tv_player_body;
    @BindView(R.id.tv_player_elapsed_time)
    TextView tv_player_elapsed_time;
    @BindView(R.id.tv_player_duration)
    TextView tv_player_duration;
    @BindView(R.id.iv_player)
    ImageView iv_player;

    private Handler mHandler;
    private MediaPlayer mMediaPlayer = null;
    private Uri uri;
    private DBManager dbManager;
    private int trackId;
    private String trackTitle, artistTitle, collectionTitle, imageUrl, previewUrl;
    private boolean isFavorite = false;
    private DownloadManager mgr = null;
    private final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        setupViews();
        setupData();
    }

    private void setupViews() {
        dbManager = new DBManager(this);
        dbManager.open();
        setSupportActionBar(toolbar_player);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlv_player_play_pause.setOnClickListener(this);
        mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        ib_player_list.setOnClickListener(this);
        ib_player_add2fav.setOnClickListener(this);
        ib_player_fav.setOnClickListener(this);
        seekbar_player.setEnabled(false);
        seekbar_player.setProgress(0);
        seekbar_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }

    private void setupData() {

        if (getIntent().getExtras() != null) {
            trackId = getIntent().getIntExtra("TRACK_ID", 0);
            imageUrl = getIntent().getStringExtra("IMAGE_URL");
            previewUrl = getIntent().getStringExtra("PREVIEW_URL");
            trackTitle = getIntent().getStringExtra("TRACK_TITLE");
            artistTitle = getIntent().getStringExtra("ARTIST_TITLE");
            collectionTitle = getIntent().getStringExtra("COLLECTION_TITLE");

            uri = Uri.parse(previewUrl);


            final File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/MPlayer/" + "/" + trackTitle + trackId + ".m4a");

            if (file == null || !file.exists()) {
                init(false, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initInfos(false, null, previewUrl);
                    }
                }, 1000);
            } else {

                init(true, file.getAbsolutePath());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initInfos(true, file.getAbsolutePath(), previewUrl);
                    }
                }, 1000);

            }


            tv_player_title.setText(getIntent().getStringExtra("TRACK_TITLE"));
            tv_player_body.setText(getIntent().getStringExtra("ARTIST_TITLE") + " | " +
                    getIntent().getStringExtra("COLLECTION_TITLE"));
            Glide.with(PlayerActivity.this)
                    .load(getIntent().getStringExtra("IMAGE_URL"))
                    .into(iv_player);

            if (dbManager.isFavorite(trackId)) {
                isFavorite = true;
                ib_player_add2fav.setBackgroundResource(R.drawable.shape_heart_red);
            } else {
                isFavorite = false;
                ib_player_add2fav.setBackgroundResource(R.drawable.shape_heart);
            }
        }
    }

    public void init(boolean isFromFile, String filePath) {
        stop();
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            if (isFromFile) {

                mMediaPlayer.setDataSource(filePath);
            } else {

                mMediaPlayer.setDataSource(getApplicationContext(), uri);
            }
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    private void initInfos(boolean isFromFile, String filePath, String url) {
        MediaMetadataRetriever mData = new MediaMetadataRetriever();
        if (isFromFile) {
            mData.setDataSource(filePath);
        } else {
            mData.setDataSource(url, new HashMap<String, String>());
        }
        int duration = Integer.parseInt(mData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        tv_player_duration.setText(secondsToString(duration));
        seekbar_player.setMax(duration);
        seekbar_player.setEnabled(true);

    }

    private String secondsToString(int time) {
        time = time / 1000;
        return String.format("%2d:%02d", time / 60, time % 60);
    }

    private void updateElapsedTime(int elapsedTime) {
        seekbar_player.setProgress(elapsedTime);
        tv_player_elapsed_time.setText(secondsToString(elapsedTime));
    }


    private void playPauseTapped() {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        int resId;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            params.setMargins(15, 0, 0, 0);
            resId = R.drawable.triangle;
            pause();
        } else {
            params.setMargins(0, 0, 0, 0);
            resId = R.drawable.combined_shape_2;
            play();
        }
        ib_player_play_pause.setLayoutParams(params);
        ib_player_play_pause.setBackgroundResource(resId);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 0);
        ib_player_play_pause.setLayoutParams(params);
        ib_player_play_pause.setBackgroundResource(R.drawable.combined_shape_2);
        mp.start();
        mHandler = new Handler();
        mHandler.post(runnable);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateElapsedTime(mMediaPlayer.getCurrentPosition());
            mHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        seekbar_player.setProgress(0);
        tv_player_elapsed_time.setText("0:00");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15, 0, 0, 0);
        ib_player_play_pause.setLayoutParams(params);
        ib_player_play_pause.setBackgroundResource(R.drawable.triangle);

    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void play() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            if (mHandler == null) {
                mHandler = new Handler();
                mHandler.post(runnable);
            }
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            uri = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == rlv_player_play_pause) {
            playPauseTapped();
        } else if (v == ib_player_list) {
            onBackPressed();
        } else if (v == ib_player_fav) {
            startActivity(new Intent(PlayerActivity.this, FavoriteActivity.class));
            onBackPressed();
        } else if (v == ib_player_add2fav) {
            if (!isFavorite) {
                isFavorite = true;
                ib_player_add2fav.setBackgroundResource(R.drawable.shape_heart_red);
                dbManager.insertFavoriteTrack(trackId, trackTitle, artistTitle, collectionTitle, imageUrl, previewUrl);
                Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
            }


            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/MPlayer/" + "/" + trackTitle + trackId + ".m4a");

            if (file == null || !file.exists()) {

                isStoragePermissionGranted();
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                fileDownalod(previewUrl);
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            fileDownalod(previewUrl);
            return true;
        }
    }





    public void fileDownalod(String url) {
        Uri uri = Uri.parse(url);
        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        Test test = new Test();
        test.methodA().methodB();




        mgr.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(trackTitle)
                .setDescription(artistTitle + " | " + collectionTitle)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/MPlayer/" + "/" + trackTitle + trackId + ".m4a"));

    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(getApplicationContext(), "Download complete!", Toast.LENGTH_SHORT).show();

        }
    };


    public class Test{

        public Test(){

        }

        public Test methodA(){
            return this;
        }

        public Test methodB(){
            return this;
        }
    }

    @Override
    protected void onResume() {
        play();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        stop();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        unregisterReceiver(onComplete);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            fileDownalod(previewUrl);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
