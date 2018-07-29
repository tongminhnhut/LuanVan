package com.tongminhnhut.luanvan;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tongminhnhut.luanvan.BLL.CheckConnection;
import com.tongminhnhut.luanvan.DAL.LoadListDongHo;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DongHoBrandActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference db_DongHo ;
    String brandId ="";
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("VNFFutura.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_dong_ho_brand);
        db_DongHo = FirebaseDatabase.getInstance().getReference("DongHo");

        recyclerView = findViewById(R.id.recyclerView_DHBrand);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipeRefresh
        swipeRefreshLayout = findViewById(R.id.swipe_layout_DongHoBrand);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null)
                    brandId = getIntent().getStringExtra("ID");
                if (!brandId.isEmpty() && brandId!=null){
                    Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
                    LoadListDongHo.loadDongHoBrand(brandId, recyclerView, getApplicationContext(),intent, swipeRefreshLayout);
                }
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null)
                    brandId = getIntent().getStringExtra("ID");
                if (!brandId.isEmpty() && brandId != null) {
                    if (CheckConnection.isConnectedInternet(getBaseContext())) {
                        Intent intent = new Intent(getApplicationContext(), DetailDongHoActivity.class);
                        LoadListDongHo.loadDongHoBrand(brandId, recyclerView, getApplicationContext(), intent, swipeRefreshLayout);
                    } else {
                        Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra internet", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });
    }
}
