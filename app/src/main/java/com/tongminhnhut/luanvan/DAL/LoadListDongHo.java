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
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.tongminhnhut.luanvan.DongHoActivity;
import com.tongminhnhut.luanvan.Model.DongHo;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.ViewHolder.DongHoViewHolder;

public class LoadListDongHo extends DongHoActivity {
    static DatabaseReference db_DongHo = FirebaseDatabase.getInstance().getReference("DongHo");
    static FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> adapter ;

    public static void loadDongHo(String Id, RecyclerView recyclerView, final Context context, final Intent intent, SwipeRefreshLayout swipeRefreshLayout){
        Query query = db_DongHo.orderByChild("menuId").equalTo(Id);

        FirebaseRecyclerOptions<DongHo> options = new FirebaseRecyclerOptions.Builder<DongHo>()
                .setQuery(query, DongHo.class)
                .build();
        FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> adapter = new FirebaseRecyclerAdapter<DongHo, DongHoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DongHoViewHolder holder, int position, @NonNull DongHo model) {
                holder.txtTen.setText(model.getName());
                holder.txtGia.setText(model.getGia());
                Picasso.with(context).load(model.getImage()).into(holder.imgHinh);
            }

            @Override
            public DongHoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongho, parent, false);
                return new DongHoViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
