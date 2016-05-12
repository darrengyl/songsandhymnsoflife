package com.church.psalm.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.model.Constants;
import com.church.psalm.model.Song;
import com.church.psalm.model.SongMatch;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by darrengu on 5/10/16.
 */
public class SearchAdapter extends ArrayAdapter<SongMatch> {
    Context context;
    List<SongMatch> songMatchList;

    public SearchAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongResultViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.eachsong_search_result, parent, false);
            holder = new SongResultViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SongResultViewHolder) convertView.getTag();
        }
        SongMatch songMatch = getItem(position);
        Song song = songMatch.getSong();
        holder.track.setText(String.valueOf(song.get_trackNumber()));
        holder.title.setText(song.get_title());

        int occurrence = song.get_firstOccurrence();
        if (songMatch.getStart() != -1) {
            String lyrics = song.get_lyrics();
            int start = lyrics.lastIndexOf("\n", occurrence - 1) + 1;
            int end = lyrics.indexOf("\n", occurrence);
            if (end == -1) {
                end = lyrics.length();
            }
            if (start < end && start * end != 1) {
                String partialLyrics = lyrics.substring(start, end);
/*                Spannable spanText = Spannable.Factory.getInstance().newSpannable(partialLyrics);
                spanText.setSpan(new android.text.style.StyleSpan(Typeface.BOLD)
                        , partialLyrics.indexOf());*/
                holder.lyrics.setText(partialLyrics);
                holder.lyrics.setVisibility(View.VISIBLE);
            } else {
                holder.lyrics.setVisibility(View.GONE);
            }
        } else {
            holder.lyrics.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        if (songMatchList == null) {
            return 0;
        } else {
            return songMatchList.size();
        }
    }

    public void setData(List<SongMatch> list) {
        songMatchList = list;
        clear();
        addAll(list);
        notifyDataSetChanged();
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
    }
}
