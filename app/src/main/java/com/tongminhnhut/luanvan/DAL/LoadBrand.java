package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.BLL.ItemClickListener;
import com.tongminhnhut.luanvan.Model.Brand;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.ViewHolder.BrandViewHolder;

public class LoadBrand {
    static DatabaseReference db_Brand = FirebaseDatabase.getInstance().getReference("Brand");
    public static FirebaseRecyclerAdapter<Brand, BrandViewHolder> adapter ;

    public static void loadBrand (final Intent intent, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, final Context context){
        FirebaseRecyclerOptions<Brand> options = new FirebaseRecyclerOptions.Builder<Brand>()
                .setQuery(db_Brand, Brand.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Brand, BrandViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BrandViewHolder holder, final int position, @NonNull Brand model) {
                holder.txtName.setText(model.getName());
                Picasso.with(context).load(model.getImage())
                        .into(holder.img);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int positon, boolean isLongClick) {
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ID", adapter.getRef(position).getKey());
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
                return new BrandViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
