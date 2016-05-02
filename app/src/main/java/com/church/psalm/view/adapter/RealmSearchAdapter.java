package com.church.psalm.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.model.Song;

import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by darrengu on 4/3/16.
 */
public class RealmSearchAdapter extends RealmBaseAdapter<Song> implements ListAdapter {
    private OnClickInterface _listener;

    public RealmSearchAdapter(Context context, RealmResults<Song> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongResultViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.eachsong_search_result, parent, false);
            holder = new SongResultViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SongResultViewHolder) convertView.getTag();
        }
        Song song = super.getItem(position);
        holder.track.setText(String.valueOf(song.get_trackNumber()));
        holder.title.setText(song.get_title());

        int occurrence = song.get_firstOccurrence();
        if (occurrence != -1) {
            String lyrics = song.get_lyrics();
            int start = lyrics.lastIndexOf("\n", occurrence - 1) + 2;
            int end = lyrics.indexOf("\n", occurrence + 1);
            if (start < end && start * end != 1) {
                holder.lyrics.setText(lyrics.substring(start, end));
                holder.lyrics.setVisibility(View.VISIBLE);
            } else {
                holder.lyrics.setVisibility(View.GONE);
            }
        } else {
            holder.lyrics.setVisibility(View.GONE);
        }
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

    @Override
    public int getCount() {
        if (realmResults == null) {
            return 0;
        } else {
            if (realmResults.size() > 50) {
                return 50;
            } else {
                return realmResults.size();
            }
        }
    }

    class SongResultViewHolder {
        @Bind(R.id.tracknumber)
        TextView track;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.lyrics)
        TextView lyrics;

        public SongResultViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

/*        @OnClick(R.id.item_row)
        public void onClickItem() {
            int trackNum = -1;
            try {
                String trackNumber = track.getText().toString();
                trackNum = Integer.valueOf(trackNumber);
            } catch (ClassCastException e){
                e.printStackTrace();
            }
            if (trackNum != -1) {
                _listener.onClickedItem(trackNum);
            }
        }*/
    }
}
