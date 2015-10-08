package com.church.psalm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Darren Gu on 10/1/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    ArrayList<Song> data;

    public RecyclerViewAdapter(Context context){
        inflator = LayoutInflater.from(context);
    }

    public RecyclerViewAdapter(Context context, ArrayList<Song> data) {
        inflator = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.eachsong, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.track.setText(data.get(position).getTrackNumber()+"");
        holder.title.setText(data.get(position).getTitle());
        holder.freq.setText("This song has been played " + data.get(position).getFrequency()+" time(s)");
        if (data.get(position).getFavorite() != 0){
            holder.fav.setImageResource(R.drawable.ic_favorite_black_36dp);
        } else {
            holder.fav.setImageResource(R.drawable.ic_favorite_border_black_36dp);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView track;
        TextView title;
        TextView freq;
        ImageView fav;
        public MyViewHolder(View itemView) {
            super(itemView);
            track = (TextView)itemView.findViewById(R.id.tracknumber);
            title = (TextView)itemView.findViewById(R.id.title);
            freq = (TextView)itemView.findViewById(R.id.freq);
            fav = (ImageView)itemView.findViewById(R.id.fav_star);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "clicked on" + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    int counter = Integer.valueOf(freq.getText().toString());
                    freq.setText(counter++);
                    Intent intent = new Intent(v.getContext(), ScoreActivity.class);
                    intent.putExtra("trackNumber", getLayoutPosition() + 1);
                    v.getContext().startActivity(intent);
                    DBAdapter dbAdapter = new DBAdapter(v.getContext());
                    dbAdapter.incrementFreq(getLayoutPosition()+1);
                    notifyItemChanged(getLayoutPosition());


                }
            });
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "clicked on fav" + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    DBAdapter dbAdapter = new DBAdapter(v.getContext());
                    dbAdapter.flipFav(getLayoutPosition()+1);
                    if (dbAdapter.isFav(getLayoutPosition()+1)){
                        fav.setImageResource(R.drawable.ic_favorite_black_36dp);
                    } else {
                        fav.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    }
                    notifyItemChanged(getLayoutPosition());
                }
            });

        }



    }
}
