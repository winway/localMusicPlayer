package com.example.localmusicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @PackageName: com.example.localmusicplayer
 * @ClassName: MusicAdapter
 * @Author: winwa
 * @Date: 2023/3/22 7:55
 * @Description:
 **/
public class MusicRVAdapter extends RecyclerView.Adapter<MusicRVAdapter.MusicRVViewHolder> {

    private Context mContext;
    private List<SongBean> mData;

    private OnItemClickListener mOnItemClickListener;

    public MusicRVAdapter(Context context, List<SongBean> data) {
        mContext = context;
        mData = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MusicRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rvitem_main_music, parent, false);
        MusicRVViewHolder viewHolder = new MusicRVViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicRVViewHolder holder, int position) {
        SongBean songBean = mData.get(position);
        holder.mIdTV.setText(songBean.getId());
        holder.mNameTV.setText(songBean.getName());
        holder.mSingerTV.setText(songBean.getSinger());
        holder.mAlbumTV.setText(songBean.getAlbum());
        holder.mDurationTV.setText(songBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class MusicRVViewHolder extends RecyclerView.ViewHolder {

        TextView mIdTV;
        TextView mNameTV;
        TextView mSingerTV;
        TextView mAlbumTV;
        TextView mDurationTV;

        public MusicRVViewHolder(@NonNull View itemView) {
            super(itemView);

            mIdTV = itemView.findViewById(R.id.rvitem_main_music_id_tv);
            mNameTV = itemView.findViewById(R.id.rvitem_main_music_name_tv);
            mSingerTV = itemView.findViewById(R.id.rvitem_main_music_singer_tv);
            mAlbumTV = itemView.findViewById(R.id.rvitem_main_music_album_tv);
            mDurationTV = itemView.findViewById(R.id.rvitem_main_music_duration_tv);
        }
    }
}
