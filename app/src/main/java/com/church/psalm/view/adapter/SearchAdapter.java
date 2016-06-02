package com.church.psalm.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.church.psalm.R;
import com.church.psalm.interfaces.OnClickSongInterface;
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
    OnClickSongInterface listener;

    public SearchAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    public void setOnClickListener(OnClickSongInterface listener) {
        this.listener = listener;
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
        if (songMatch.isInTitle()) {
            Spannable spannable = new SpannableString(song.get_title());
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                    songMatch.getStart(), songMatch.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD),
                    songMatch.getStart(), songMatch.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.title.setText(spannable);
            holder.lyrics.setVisibility(View.GONE);
        } else {
            String partialLyrics = songMatch.getPartialLyrics();
            Spannable spannable = new SpannableString(partialLyrics);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                    songMatch.getStart(), songMatch.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD),
                    songMatch.getStart(), songMatch.getEnd(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.title.setText(song.get_title());
            holder.lyrics.setText(spannable);
            holder.lyrics.setVisibility(View.VISIBLE);

        }
        if (listener != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickedItem(position);
                }
            });
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

    static class SongResultViewHolder {
        @Bind(R.id.tracknumber)
        TextView track;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.lyrics)
        TextView lyrics;
        @Bind(R.id.cardview)
        CardView cardView;

        public SongResultViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
