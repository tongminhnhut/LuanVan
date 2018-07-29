package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.DongHoActivity;
import com.tongminhnhut.luanvan.Interface.ItemClickListener;
import com.tongminhnhut.luanvan.Model.DongHo;
import com.tongminhnhut.luanvan.Model.Order;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.ViewHolder.DongHoViewHolder;

public class LoadPriceDongHo extends DongHoActivity {
    static DatabaseReference db_DongHo = FirebaseDatabase.getInstance().getReference();
    public static FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> spinnerAdapter ;
    static Query query ;

    public static void loadDongHoByPrice(int Id, RecyclerView recyclerView, final Context context, final Intent intent, final SwipeRefreshLayout swipeRefreshLayout){
        if (Id == 1){
            query = db_DongHo.child("DongHo").orderByChild("priceId").equalTo("1");
        }else if (Id==2){
            query = db_DongHo.child("DongHo").orderByChild("priceId").equalTo("2");
        }else if (Id==3){
            query = db_DongHo.child("DongHo").orderByChild("priceId").equalTo("3");
        }else {
            query = db_DongHo.child("a");
            Toast.makeText(context, "Chưa có đồng hồ cho khoảng giá này", Toast.LENGTH_SHORT).show();
        }

        FirebaseRecyclerOptions<DongHo> options = new FirebaseRecyclerOptions.Builder<DongHo>()
                .setQuery(query, DongHo.class)
                .build();
        spinnerAdapter = new FirebaseRecyclerAdapter<DongHo, DongHoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DongHoViewHolder holder, final int position, @NonNull final DongHo model) {
                holder.txtTen.setText(model.getName());
                holder.txtGia.setText(model.getGia());
                Picasso.with(context).load(model.getImage()).into(holder.imgHinh);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("dongho", spinnerAdapter.getRef(position).getKey());
                        context.startActivity(intent);
                    }
                });

                final boolean isExist = new Database(context).checkExistDongHo(
                        spinnerAdapter.getRef(position).getKey(),
                        SignIn_DAL.curentUser.getPhone()
                );


                holder.btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isExist) {
                            new Database(context).addCarts(new Order(
                                    SignIn_DAL.curentUser.getPhone(),
                                    spinnerAdapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getGia(),
                                    model.getDiscount(),
                                    model.getImage()

                            ));
                        }else {
                            new Database(context).increaseCart(spinnerAdapter.getRef(position).getKey(),SignIn_DAL.curentUser.getPhone());
                        }

                        Toast.makeText(context, "Add to Cart !", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public DongHoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongho, parent, false);
                return new DongHoViewHolder(view);
            }
        };
        spinnerAdapter.startListening();
        recyclerView.setAdapter(spinnerAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        spinnerAdapter.startListening();
    }
}
