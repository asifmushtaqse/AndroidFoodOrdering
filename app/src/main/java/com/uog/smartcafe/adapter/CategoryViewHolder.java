package com.uog.smartcafe.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uog.smartcafe.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView categoryName;
    public ImageView categoryImage;
    public View mItemView;


    public CategoryViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        categoryName = (TextView)itemView.findViewById(R.id.category_name);
        categoryImage = (ImageView)itemView.findViewById(R.id.category_image);
    }
}
