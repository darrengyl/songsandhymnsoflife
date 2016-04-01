package com.church.psalm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.model.Song;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by darrengu on 3/27/16.
 */
public class RealmListviewAdapter extends RealmBaseAdapter<Song> implements ListAdapter{
    private OnClickInterface _listener;
    public RealmListviewAdapter(Context context, RealmResults<Song> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.eachsong, parent, false);
            viewHolder = new SongViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SongViewHolder) convertView.getTag();
        }
        Song song = realmResults.get(position);
        viewHolder.track.setText(String.valueOf(song.get_trackNumber()));
        viewHolder.title.setText(song.get_title());
        int freqInt = song.get_frequency();
        String freqNumber = context.getResources().getQuantityString(R.plurals.frequency_number
                , freqInt, freqInt);
        viewHolder.freq.setText(freqNumber);
        if (song.is_favorite()) {
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        viewHolder.position = position;
        return convertView;
    }

    public void updateRealmResults(RealmResults<Song> queryResults) {
        super.updateRealmResults(queryResults);
    }

    public RealmResults<Song> getRealmResults() {
        return realmResults;
    }

    public void setListener(OnClickInterface listener) {
        _listener = listener;
    }

    class SongViewHolder{
        @Bind(R.id.tracknumber)
        TextView track;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.freq)
        TextView freq;
        @Bind(R.id.fav_star)
        ImageView fav;
        int position;

        public SongViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.fav_star_holder)
        public void onClickFav() {
            _listener.onClickedFav(position);
        }

        @OnClick(R.id.item_row)
        public void onClickItem() {
            _listener.onClickedItem(position);
        }
    }
}
