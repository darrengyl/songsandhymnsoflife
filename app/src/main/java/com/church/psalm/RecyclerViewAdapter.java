package com.church.psalm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Darren Gu on 10/1/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    ArrayList<Song> data;
    Context context;

    public RecyclerViewAdapter(Context context){
        inflator = LayoutInflater.from(context);
    }

    public RecyclerViewAdapter(Context context, ArrayList<Song> data) {
        inflator = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.eachsong, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Drawable favImage;
        System.out.println("onBindViewHolder is called" + position);

        holder.track.setText(data.get(position).getTrackNumber() + "");
        holder.title.setText(data.get(position).getTitle());
        int freqInt = data.get(position).getFrequency();
        String freqNumber = context.getResources().getQuantityString(R.plurals.frequency_number
                , freqInt, freqInt);

        holder.freq.setText(freqNumber);


        if (data.get(position).getFavorite() != 0){
            favImage = context.getDrawable(R.drawable.ic_favorite_black_36dp);
        } else {
            favImage = context.getDrawable(R.drawable.ic_favorite_border_black_36dp);
        }
        favImage.setTint(context.getResources().getColor(R.color.colorPinkHeart));
        /*loop.setIcon(setTintDrawable(getResources()
                .getDrawable(R.drawable.ic_repeat_one_black_24dp)));*/
        holder.fav.setImageDrawable(favImage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            fav.setOnClickListener(this);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            DBAdapter dbAdapter = new DBAdapter(v.getContext());
            if (v.getId() == R.id.fav_star){

                dbAdapter.flipFav(getLayoutPosition()+1);
                int favInt = dbAdapter.getFav(getLayoutPosition()+1)? 1: 0;
                Song song = data.get(getLayoutPosition());
                song.setFavorite(favInt);
                data.set(getLayoutPosition(), song);

            } else {
                if (isNetworkConnected()){
                    System.out.println("Pressed zero based " + getLayoutPosition());
                    dbAdapter.incrementFreq(getAdapterPosition() + 1);
                    int freqInt = dbAdapter.getFreq(getAdapterPosition()+1);
                    Song song = data.get(getLayoutPosition());
                    song.setFrequency(freqInt);
                    data.set(getLayoutPosition(), song);
                    Intent intent = new Intent(v.getContext(), ScoreActivity.class);
                    intent.putExtra("trackNumber", getLayoutPosition() + 1);
                    v.getContext().startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(v, v.getContext()
                            .getString(R.string.network_error)
                            , Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(v.getContext().getResources()
                            .getColor(R.color.colorAccent));
                    snackbar.setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.getContext().startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                            .setActionTextColor(v.getResources().getColor(R.color.white));
                    snackbar.show();
                }


            }
            notifyItemChanged(getAdapterPosition());

        }
    }


    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
                context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }
}
