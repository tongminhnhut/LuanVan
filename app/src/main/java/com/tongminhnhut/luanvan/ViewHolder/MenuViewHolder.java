package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName;
    public ImageView img ;
    public ItemClickListener itemClickListener ;

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txtMenu_itemHome);
        img = itemView.findViewById(R.id.imgMenu_itemHome);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener ;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
