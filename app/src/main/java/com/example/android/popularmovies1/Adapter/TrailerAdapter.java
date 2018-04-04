package com.example.android.popularmovies1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies1.Data.TrailerData;
import com.example.android.popularmovies1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by the Bumbs on 23/03/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final static String THUMBNAIL_PATH = "http://img.youtube.com/vi/";
    private final static String THUMB_PARAM = "/1.jpg";

    private List<TrailerData> mTrailerList;
    private Context mContext;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onTrailerClick(TrailerData trailer);
    }

    public TrailerAdapter(Context context, List<TrailerData> trailerList,
                          TrailerAdapterOnClickHandler clickHandler) {
        this.mContext = context;
        this.mTrailerList = trailerList;
        this.mClickHandler = clickHandler;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailerThumbnail;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            trailerThumbnail = itemView.findViewById(R.id.trailer_image);
        }

        @Override
        public void onClick(View v) {
            TrailerData trailer = mTrailerList.get(getAdapterPosition());
            mClickHandler.onTrailerClick(trailer);
        }
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item_list,
                parent, false);
        view.setFocusable(true);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        TrailerData trailer = mTrailerList.get(position);
        Picasso.with(mContext).load(THUMBNAIL_PATH + trailer + THUMB_PARAM)
                .placeholder(R.drawable.trailer)
                .into(holder.trailerThumbnail);
    }

    @Override
    public int getItemCount() {
        if(mTrailerList == null) {
            return 0;
        } else return mTrailerList.size();
    }
}
