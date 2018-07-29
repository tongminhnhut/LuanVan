package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongminhnhut.luanvan.BLL.ItemClickListener;
import com.tongminhnhut.luanvan.R;

public class BrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtName;
    public ImageView img;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BrandViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtName_itemBrand);
        img= itemView.findViewById(R.id.imgBrand_itemBrand);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
