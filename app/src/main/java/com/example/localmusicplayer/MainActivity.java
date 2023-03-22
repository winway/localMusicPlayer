package com.example.localmusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mNameTV;
    private TextView mSingerTV;
    private ImageView mPrevIV;
    private ImageView mPlayIV;
    private ImageView mNextIV;
    private RecyclerView mMusicListRV;

    private MusicRVAdapter mMusicRVAdapter;
    private List<SongBean> mMusicRVAdapterData;

    private int currentPosition = -1;

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
                currentPosition = position;

                SongBean songBean = mMusicRVAdapterData.get(position);
                mNameTV.setText(songBean.getName());
                mSingerTV.setText(songBean.getSinger());

                stopPlayer();

                startPlayer(songBean);
            }
        });
    }

    private void startPlayer(SongBean songBean) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        mMediaPlayer.reset();

        try {
            mMediaPlayer.setDataSource(songBean.getPath());
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mPlayIV.setImageResource(R.mipmap.icon_pause);
    }

    private void stopPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
            mMediaPlayer.stop();
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
        mNextIV.setOnClickListener(this);
        mNextIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_music_controller_prev_iv:
                break;
            case R.id.main_music_controller_play_iv:
                break;
            case R.id.main_music_controller_next_iv:
                break;
        }
    }
}