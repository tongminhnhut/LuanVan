package com.tongminhnhut.luanvan.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.CartActivity;
import com.tongminhnhut.luanvan.DAL.Database;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.Model.Order;
import com.tongminhnhut.luanvan.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>  {
    private List<Order> list = new ArrayList<>();
    private CartActivity cartActivity;


    public CartAdapter(List<Order> list, CartActivity cartActivity) {
        this.list = list;
        this.cartActivity = cartActivity;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cartActivity).inflate(R.layout.item_cart, parent, false );
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        holder.btnCount.setNumber(list.get(position).getQuantity());
        holder.txtTen.setText(list.get(position).getProductName());
        DecimalFormat fm = new DecimalFormat("#,###,###");
        Locale locale = new Locale("vn", "VN");
//        NumberFormat fm = NumberFormat.getCurrencyInstance(locale);
        holder.txtGia.setText(list.get(position).getPrice());
        Picasso.with(cartActivity.getBaseContext())
                .load(list.get(position).getImage())
                .into(holder.imgDH);
        //total
        int price = (Integer.parseInt(list.get(position).getPrice())*Integer.parseInt(list.get(position).getQuantity()));
        holder.btnCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = list.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cartActivity).updateCart(order);

                //total
                int total = 0;
                List<Order> orderList = new Database(cartActivity).getCart(SignIn_DAL.curentUser.getPhone());
                for (Order item:orderList){
                    total+=(Integer.parseInt(order.getPrice())*Integer.parseInt(item.getQuantity()));
                }

//                Locale locale = new Locale("vn", "VN");
//                NumberFormat fm = NumberFormat.getCurrencyInstance(locale);
                DecimalFormat fm = new DecimalFormat("#,###,###");
                cartActivity.txtTotal.setText(fm.format(total)+" VNĐ");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Order getItem (int position){
        return list.get(position);
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order order,int position){
        list.add(position,order);
        notifyItemInserted(position);
    }
}
