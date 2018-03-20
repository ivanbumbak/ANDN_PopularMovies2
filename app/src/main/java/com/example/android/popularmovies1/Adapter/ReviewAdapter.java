package com.example.android.popularmovies1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies1.Data.ReviewData;
import com.example.android.popularmovies1.R;

import java.util.ArrayList;

/**
 * Created by the Bumbs on 15/03/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<ReviewData> mReviewList;
    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<ReviewData> reviewList) {
        this.mContext = context;
        this.mReviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item_list,
                parent, false);
        view.setFocusable(true);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {
        if(mReviewList != null) {
            ReviewData reviewData = mReviewList.get(position);
            holder.reviewAuthor.setText(reviewData.getAuthor());
            holder.reviewContent.setText(reviewData.getContent());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null) {
            return  0;
        } else {
            return mReviewList.size();
        }
    }

    public void setReviewList(ArrayList<ReviewData> reviewList) {
        if(reviewList != null) {
            mReviewList = new ArrayList<>(reviewList);
        }
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthor;
        TextView reviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            reviewContent = itemView.findViewById(R.id.review_content);
        }
    }
}
