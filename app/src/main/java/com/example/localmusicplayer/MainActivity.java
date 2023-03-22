package com.example.localmusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "MainActivity";

    private TextView mNameTV;
    private TextView mSingerTV;
    private ImageView mPrevIV;
    private ImageView mPlayIV;
    private ImageView mNextIV;
    private RecyclerView mMusicListRV;

    private MusicRVAdapter mMusicRVAdapter;
    private List<SongBean> mMusicRVAdapterData;

    private int mCurrentPosition = -1;
    private int mProgress = 0;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initMusicRVAdapter();

        setupMusicRVAdapter();

        loadMusicData();

        refreshMusicRVAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    private void setupMusicRVAdapter() {
        mMusicRVAdapter.setOnItemClickListener(new MusicRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentPosition = position;

                changeMusic(position);
            }
        });
    }

    private void changeMusic(int position) {
        SongBean songBean = mMusicRVAdapterData.get(position);

        mNameTV.setText(songBean.getName());
        mSingerTV.setText(songBean.getSinger());

        stopPlayer();

        startPlayer(songBean);
    }

    private void startPlayer(SongBean songBean) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
        }

        mMediaPlayer.reset();

        try {
            mMediaPlayer.setDataSource(songBean.getPath());
            mMediaPlayer.prepare();
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayer() {
        mProgress = 0;
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(mProgress);
            mMediaPlayer.stop();
        }
        mPlayIV.setImageResource(R.mipmap.icon_play);
    }

    private void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(mProgress);
            mMediaPlayer.start();
        }

        mPlayIV.setImageResource(R.mipmap.icon_pause);
    }

    private void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mProgress = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
        }
        mPlayIV.setImageResource(R.mipmap.icon_play);
    }

    private void refreshMusicRVAdapter() {
        mMusicRVAdapter.notifyDataSetChanged();
    }

    private void loadMusicData() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        int counter = 1;
        while (cursor.moveToNext()) {
            String id = String.valueOf(counter);
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long durationSeconds = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String duration = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(durationSeconds));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            SongBean songBean = new SongBean(id, name, singer, album, duration, path);
            mMusicRVAdapterData.add(songBean);

            counter += 1;
        }
    }

    private void initMusicRVAdapter() {
        mMusicRVAdapterData = new ArrayList<>();
        mMusicRVAdapter = new MusicRVAdapter(this, mMusicRVAdapterData);
        mMusicListRV.setAdapter(mMusicRVAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMusicListRV.setLayoutManager(layoutManager);
    }

    private void initView() {
        mNameTV = findViewById(R.id.main_music_controller_name_tv);
        mSingerTV = findViewById(R.id.main_music_controller_singer_tv);
        mPrevIV = findViewById(R.id.main_music_controller_prev_iv);
        mPlayIV = findViewById(R.id.main_music_controller_play_iv);
        mNextIV = findViewById(R.id.main_music_controller_next_iv);
        mMusicListRV = findViewById(R.id.main_music_list_rv);

        mPrevIV.setOnClickListener(this);
        mPlayIV.setOnClickListener(this);
        mNextIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_music_controller_prev_iv:
                if (mCurrentPosition == 0) {
                    Toast.makeText(this, "duang!duang!duang!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentPosition -= 1;
                changeMusic(mCurrentPosition);
                break;
            case R.id.main_music_controller_play_iv:
                Log.i(TAG, "onClick: ");
                if (mCurrentPosition == -1) {
                    Toast.makeText(this, "请选择要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mMediaPlayer.isPlaying()) {
                    Log.i(TAG, "onClick: 1");
                    pause();
                } else {
                    Log.i(TAG, "onClick: 2");
                    play();
                }
                break;
            case R.id.main_music_controller_next_iv:
                if (mCurrentPosition == mMusicRVAdapterData.size() - 1) {
                    Toast.makeText(this, "duang!duang!duang!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentPosition += 1;
                changeMusic(mCurrentPosition);
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mProgress = 0;
        mPlayIV.setImageResource(R.mipmap.icon_play);
    }
}