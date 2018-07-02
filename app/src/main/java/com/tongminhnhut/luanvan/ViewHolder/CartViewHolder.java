package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView txtTen, txtGia;
    public ImageView imgDH;
    public ElegantNumberButton btnCount ;

    public RelativeLayout relativeLayout;
    public LinearLayout linearLayout ;
    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);
        txtTen = itemView.findViewById(R.id.txtTenDongHo_cartItem);
        txtGia = itemView.findViewById(R.id.txtGia_cartItem);
        imgDH = itemView.findViewById(R.id.img_cartItem);
        btnCount = itemView.findViewById(R.id.btnCount_cartItem);
//        itemView.setOnClickListener(this);
        relativeLayout = itemView.findViewById(R.id.view_background_cartItem);
        linearLayout = itemView.findViewById(R.id.background_CartItem);
//        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {
//        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Vui lòng chọn !");
        menu.add(0,0,getAdapterPosition(),"Delete");
    }
}