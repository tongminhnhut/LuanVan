package com.tongminhnhut.luanvan.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.Model.Order;
import com.tongminhnhut.luanvan.R;

import java.util.ArrayList;
import java.util.List;

class ShowCartViewHolder extends RecyclerView.ViewHolder{
    public TextView txtTen, txtGia;
    public ImageView imgHinh ;
    public ElegantNumberButton btnCount ;

    public ShowCartViewHolder(View itemView) {
        super(itemView);

        txtGia = itemView.findViewById(R.id.txtGia_showCart);
        txtTen = itemView.findViewById(R.id.txtTenDongHo_showCart);
        imgHinh = itemView.findViewById(R.id.img_showCart);
        btnCount = itemView.findViewById(R.id.btnCount_showCart);

    }
}
public class ShowCartAdapter extends RecyclerView.Adapter<ShowCartViewHolder> {
    List<Order> listOrder = new ArrayList<>();
    Context context ;

    public ShowCartAdapter(List<Order> listOrder, Context context) {
        this.listOrder = listOrder;
        this.context = context;
    }

    @Override
    public ShowCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_show_cart, parent, false);
        return new ShowCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowCartViewHolder holder, int position) {
        Order order = listOrder.get(position);
        holder.txtTen.setText(order.getProductName());
        holder.txtGia.setText(order.getPrice());
        Picasso.with(context).load(order.getImage()).into(holder.imgHinh);

    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}
