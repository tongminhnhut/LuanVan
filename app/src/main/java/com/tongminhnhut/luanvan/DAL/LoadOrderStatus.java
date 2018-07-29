package com.tongminhnhut.luanvan.DAL;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public static FirebaseRecyclerAdapter<RequestOrder, OrderViewHolder> adapter;
    public static void loadDonHang( final Context context, final String phone, final RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout){
        db_Reuqest = FirebaseDatabase.getInstance().getReference("RequestOrder");

        Query query = db_Reuqest.orderByChild("phone").equalTo(phone);
        FirebaseRecyclerOptions<RequestOrder> options = new FirebaseRecyclerOptions.Builder<RequestOrder>()
                .setQuery(query, RequestOrder.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<RequestOrder, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull RequestOrder model) {
                holder.txtId.setText(adapter.getRef(position).getKey());
                holder.txtAddress.setText(model.getAddress());
                holder.txtName.setText(model.getName());
                holder.txtPhone.setText(model.getPhone());
                holder.txtStatus.setText(ConvertToStatus.convertCodeStatus(model.getStatus()+""));
//                if (adapter.getItem(position).getStatus().equals("1")){
//                    holder.btnDelete.setEnabled(false);
//                }
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getItem(position).getStatus().equals("0")){
                            showDialogDelete(adapter.getRef(position).getKey(), context, adapter.getItem(position).getName());
                        }else {
                            Toast.makeText(context, "Không thể huỷ do đơn hàng của bạn đang vận chuyển !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    public static void showDialogDelete(final String key, final Context context, final String name) {
        db_Reuqest.child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, new StringBuilder("Đơn hàng ")
                                .append(name)
                                .append(" đã được huỷ").toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}
