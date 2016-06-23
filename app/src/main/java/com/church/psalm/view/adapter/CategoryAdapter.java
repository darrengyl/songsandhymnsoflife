package com.church.psalm.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.model.Song;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by darrengu on 6/19/16.
 */
public class CategoryAdapter extends RealmBaseAdapter<Song> {

    public CategoryAdapter(Context context, OrderedRealmCollection<Song> data) {
        super(context, data);
    }

    public void setData(RealmResults<Song> data) {
        super.updateData(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song current = getItem(position);
        SongHolder songHolder;
        HeaderHolder headerHolder;

        if (current.get_id() == 0) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.header_category, parent, false);
                headerHolder = new HeaderHolder(convertView);
                convertView.setTag(headerHolder);
            } else {
                headerHolder = (HeaderHolder)convertView.getTag();
            }
            headerHolder.header.setText(current.get_title());
        } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.eachsong_category, parent, false);
                songHolder = new SongHolder(convertView);
                convertView.setTag(songHolder);
            } else {
                songHolder = (SongHolder)convertView.getTag();
            }

            songHolder.track.setText(current.get_trackNumber());
            songHolder.title.setText(current.get_title());
        }
        return convertView;
    }

    static class HeaderHolder {
        @Bind(R.id.header)
        TextView header;

        public HeaderHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    static class SongHolder {
        @Bind(R.id.track)
        TextView track;
        @Bind(R.id.title)
        TextView title;

        public SongHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
