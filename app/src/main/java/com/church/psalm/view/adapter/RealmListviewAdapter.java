package com.church.psalm.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickInterface;
import com.church.psalm.model.Song;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by darrengu on 3/27/16.
 */
public class RealmListviewAdapter extends RealmBaseAdapter<Song> implements ListAdapter, SectionIndexer{
    private OnClickInterface _listener;
    private HashMap<Character, Integer> _sectionMap;
    private Character[] _sections;

    public RealmListviewAdapter(Context context, RealmResults<Song> realmResults) {
        super(context, realmResults);
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
        Song song = getItem(position);
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
        //Log.d("Song", song.get_trackNumber() + " " + song.get_title());
        //Log.d("Bind view", "pinyin = " + song.get_pinyin());
        return convertView;
    }

    public void updateRealmResults(RealmResults<Song> queryResults) {
        super.updateData(queryResults);
        Log.d("Adapter", "UpdateRealmResults is called");
    }

    public OrderedRealmCollection<Song> getRealmResults() {
        return adapterData;
    }

    public void setListener(OnClickInterface listener) {
        _listener = listener;
    }

    @Override
    public Object[] getSections() {
        return _sections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return _sectionMap.get(_sections[sectionIndex]);
    }

    @Override
    public int getSectionForPosition(int position) {
        char c = getRealmResults().get(position).get_pinyin().charAt(0);
        for (int i = 0; i < _sections.length; i++) {
            if (_sections[i] == c) {
                return i;
            }
        }
        return 0;
    }

    public void setSectionData(HashMap<Character, Integer> map, Character[] sections) {
        _sectionMap = map;
        _sections = sections;
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
            //getRealmResults().get(position)
            _listener.onClickedFav(position);
        }

        @OnClick(R.id.item_row)
        public void onClickItem() {
            _listener.onClickedItem(position);
        }
    }
}
