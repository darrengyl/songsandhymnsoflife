package com.church.psalm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.model.Song;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by darrengu on 3/26/16.
 */
public class SongsAdapter extends RealmRecyclerViewAdapter<Song> {
    private Context _context;
    private OnClickInterface _listener;

    public SongsAdapter(Context context) {
        _context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.eachsong, parent, false);
        SongViewHolder holder = new SongViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SongViewHolder viewHolder = (SongViewHolder) holder;
        Song song = getItem(position);
        viewHolder.track.setText(String.valueOf(song.get_trackNumber()));
        viewHolder.title.setText(song.get_title());
        int freqInt = song.get_frequency();
        String freqNumber = _context.getResources().getQuantityString(R.plurals.frequency_number
                , freqInt, freqInt);
        viewHolder.freq.setText(freqNumber);
        if (song.is_favorite()) {
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        if (getRealmBaseAdapter() != null) {
            return getRealmBaseAdapter().getCount();
        } else {
            return 0;
        }
    }

    public void setListener(OnClickInterface listener) {
        _listener = listener;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tracknumber)
        TextView track;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.freq)
        TextView freq;
        @Bind(R.id.fav_star)
        ImageView fav;

        public SongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.fav_star_holder)
        public void onClickFav() {
            _listener.onClickedFav(getAdapterPosition());
        }

        @OnClick(R.id.item_row)
        public void onClickItem() {
            _listener.onClickedItem(getAdapterPosition());
        }
    }
}
