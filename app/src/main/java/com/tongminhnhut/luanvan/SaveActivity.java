package com.tongminhnhut.luanvan;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.LoadListDongHo;
import com.tongminhnhut.luanvan.DAL.LoadProduct;

public class SaveActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout ;
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        swipeRefreshLayout = findViewById(R.id.swipe_layout_SaveProduct);
        recyclerView = findViewById(R.id.recyclerView_LuuProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                    Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
                    LoadProduct.loadDongHo(getApplicationContext(), recyclerView, swipeRefreshLayout);

            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                    if (CheckConnection.isConnectedInternet(getBaseContext())) {
//                        Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
                        LoadProduct.loadDongHo(getApplicationContext(), recyclerView, swipeRefreshLayout);
                    } else {
                        Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                        return;
                    }


            }
        });

    }
}
