package com.example.android.popularmovies1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies1.Data.ReviewData;
import com.example.android.popularmovies1.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by the Bumbs on 08/03/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;

    private List<ReviewData> mReviewList;

    public ReviewAdapter(@NonNull Context context, @NonNull List<ReviewData> reviewList) {
        this.mContext = context;
        this.mReviewList = reviewList;
    }

    public void setReviewList(List<ReviewData> reviewList) {
        if(reviewList != null) {
            mReviewList = new ArrayList<>(reviewList);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.username_text)
        TextView userName;
        @BindView(R.id.review_text)
        TextView reviewText;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.review_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
            ReviewData reviewData = mReviewList.get(position);
            holder.userName.setText(reviewData.getReviewAuthor());
            holder.reviewText.setText(reviewData.getReviewContent());
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null) {
            return 0;
        } else {
            return mReviewList.size();
        }
    }
}
