package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
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
import com.tongminhnhut.luanvan.Model.DongHo;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.SaveActivity;
import com.tongminhnhut.luanvan.ViewHolder.DongHoViewHolder;

public class LoadProduct extends SaveActivity {
    public static DatabaseReference db_Save = FirebaseDatabase.getInstance().getReference("Save");
    static FirebaseRecyclerAdapter<DongHo, DongHoViewHolder> adapter ;

    public static void loadDongHo (final Context context, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout){
        Query query = db_Save.orderByChild("phone").equalTo(SignIn_DAL.curentUser.getPhone());
        FirebaseRecyclerOptions<DongHo> options = new FirebaseRecyclerOptions.Builder<DongHo>()
                .setQuery(query, DongHo.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<DongHo, DongHoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DongHoViewHolder holder, int position, @NonNull DongHo model) {
                holder.txtTen.setText(model.getName());
                holder.txtGia.setText(model.getGia());
                Picasso.with(context).load(model.getImage()).into(holder.imgHinh);
            }

            @Override
            public DongHoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_luu_product, parent, false);
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
        adapter.stopListening();
    }
}
