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
import com.tongminhnhut.luanvan.BLL.ConvertToStatus;
import com.tongminhnhut.luanvan.Model.RequestOrder;
import com.tongminhnhut.luanvan.OrderStatusActivity;
import com.tongminhnhut.luanvan.R;
import com.tongminhnhut.luanvan.ViewHolder.OrderViewHolder;

public class LoadOrderStatus extends OrderStatusActivity {
    static DatabaseReference db_Reuqest ;
    static FirebaseRecyclerAdapter<RequestOrder, OrderViewHolder> adapter;
    public static void loadDonHang(final String phone, final RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout){
        db_Reuqest = FirebaseDatabase.getInstance().getReference("RequestOrder");

        Query query = db_Reuqest.orderByChild("phone").equalTo(phone);
        FirebaseRecyclerOptions<RequestOrder> options = new FirebaseRecyclerOptions.Builder<RequestOrder>()
                .setQuery(query, RequestOrder.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<RequestOrder, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull RequestOrder model) {
                holder.txtId.setText(adapter.getRef(position).getKey());
                holder.txtAddress.setText(model.getAddress());
                holder.txtName.setText(model.getName());
                holder.txtPhone.setText(model.getPhone());
                holder.txtStatus.setText(ConvertToStatus.convertCodeStatus(model.getStatus()+""));
            }

            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderstatus, parent, false);
                return new OrderViewHolder(view);
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
