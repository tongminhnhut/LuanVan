package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.R;

public class DongHoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView txtGia, txtTen;
    public ImageView btnCart, imgHinh;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener ;
    }



    public DongHoViewHolder(View itemView) {
        super(itemView);
        txtGia = itemView.findViewById(R.id.txtPrice_itemDongHo);
        txtTen = itemView.findViewById(R.id.txtTen_itemDongHo);
        imgHinh = itemView.findViewById(R.id.imgDongHo_itemDongHo);
        btnCart = itemView.findViewById(R.id.btnCart_itemDongHo);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
