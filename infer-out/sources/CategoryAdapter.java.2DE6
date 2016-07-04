package com.church.psalm.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by darrengu on 6/19/16.
 */
public class CategoryAdapter extends BaseAdapter {
    private static final int VIEW_HOLDER_HEADER = 0;
    private static final int VIEW_HOLDER_SONG = 1;
    private List<Song> songList;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context) {
        songList = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (getItemViewType(position) == VIEW_HOLDER_HEADER) {
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

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).get_id() == Constants.HEADER_ID) {
            return VIEW_HOLDER_HEADER;
        } else {
            return VIEW_HOLDER_SONG;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
