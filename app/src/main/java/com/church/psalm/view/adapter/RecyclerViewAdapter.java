package com.church.psalm.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.church.psalm.DbAdapter;
import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.view.activity.ScoreActivity;
import com.church.psalm.model.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Darren Gu on 10/1/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    @Bind(R.id.fav_star)
    ImageButton favStar;
    private List<Song> _data;
    private Context _context;
    private OnClickInterface _listener;

    public RecyclerViewAdapter(Context context, OnClickInterface listener) {
        _context = context;
        _listener = listener;
        _data = new ArrayList<>();
    }

    public void setData(List<Song> data) {
        _data.clear();
        _data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.eachsong, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Drawable favImage;
        Log.d("Recyclerview", "onBindViewHolder is called" + position);
        Song song = _data.get(position);
        holder.track.setText(song.getTrackNumber());
        holder.title.setText(song.getTitle());
        int freqInt = song.getFrequency();
        String freqNumber = _context.getResources().getQuantityString(R.plurals.frequency_number
                , freqInt, freqInt);
        holder.freq.setText(freqNumber);
        if (song.getFavorite() != 0) {
            holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

    public void setFavStar(int position, int value) {
        _data.get(position).setFavorite(value);
        notifyItemChanged(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tracknumber)
        TextView track;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.freq)
        TextView freq;
        @Bind(R.id.fav_star)
        ImageView fav;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _listener.onClicked(getAdapterPosition());
                }
            });
        }
    }
}
