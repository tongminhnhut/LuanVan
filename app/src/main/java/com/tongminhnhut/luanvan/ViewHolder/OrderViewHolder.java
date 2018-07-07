package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtId, txtPhone, txtStatus, txtName, txtAddress ;
    private ItemClickListener itemClickListener;
    public ImageView btnDelete ;


    public OrderViewHolder(View itemView) {
        super(itemView);
        txtId = itemView.findViewById(R.id.txtId_orderStatus);
        txtPhone = itemView.findViewById(R.id.txtPhone_orderStatus);
        txtStatus = itemView.findViewById(R.id.txtStatus_orderStatus);
        txtName = itemView.findViewById(R.id.txtName_orderStatus);
        txtAddress = itemView.findViewById(R.id.txtAddress_orderStatus);
        btnDelete = itemView.findViewById(R.id.btnDelete_OrderStatus);

//        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener ;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
