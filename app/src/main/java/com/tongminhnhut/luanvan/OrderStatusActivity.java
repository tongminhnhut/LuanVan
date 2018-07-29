package com.tongminhnhut.luanvan;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.BLL.ConvertToStatus;
import com.tongminhnhut.luanvan.DAL.LoadListDongHo;
import com.tongminhnhut.luanvan.DAL.LoadOrderStatus;
import com.tongminhnhut.luanvan.DAL.SignIn_DAL;
import com.tongminhnhut.luanvan.Model.RequestOrder;
import com.tongminhnhut.luanvan.ViewHolder.OrderViewHolder;

public class OrderStatusActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        recyclerView = findViewById(R.id.recyclerView_OrderStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //swipeRefresh
        swipeRefreshLayout = findViewById(R.id.swipe_layout_OrderStatus);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckConnection.isConnectedInternet(getApplicationContext())){
//                    if (getIntent() == null){
//                        LoadOrderStatus.loadDonHang(SignIn_DAL.curentUser.getPhone(), recyclerView, swipeRefreshLayout);
//                    }else {
//                        LoadOrderStatus.loadDonHang(getIntent().getStringExtra("userPhone"), recyclerView, swipeRefreshLayout);
//                    }
                    LoadOrderStatus.loadDonHang(getApplicationContext(),SignIn_DAL.curentUser.getPhone(), recyclerView, swipeRefreshLayout);

                } else Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (CheckConnection.isConnectedInternet(getApplicationContext())){
                    LoadOrderStatus.loadDonHang(getApplicationContext(),SignIn_DAL.curentUser.getPhone(), recyclerView, swipeRefreshLayout);

                } else Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
            }
        });

//        showDeleteDialog();

    }

    private void showDeleteDialog(String key, final RequestOrder requestOrder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Huỷ đơn hàng");
        alertDialog.setMessage("Bạn có chắc chắn muốn huỷ");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


}
