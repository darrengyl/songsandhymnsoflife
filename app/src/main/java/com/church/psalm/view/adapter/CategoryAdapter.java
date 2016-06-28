package com.church.psalm.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.model.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by darrengu on 6/19/16.
 */
public class CategoryAdapter extends BaseAdapter {
    Context context;
    List<Song> songList;

    public CategoryAdapter(Context context) {
        this.context = context;
        songList = new ArrayList<>();
    }

    public void setData(List<Song> data) {
        songList.clear();
        songList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (songList == null) {
            return 0;
        } else {
            return songList.size();
        }
    }

    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song current = getItem(position);
        SongHolder songHolder;
        HeaderHolder headerHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            songHolder.track.setText(String.valueOf(current.get_trackNumber()));
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
